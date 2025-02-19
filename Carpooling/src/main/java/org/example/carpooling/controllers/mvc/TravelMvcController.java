package org.example.carpooling.controllers.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.helpers.AuthenticationHelper;
import org.example.carpooling.helpers.ModelMapper;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.TravelRequest;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.TravelDtoIn;
import org.example.carpooling.models.dto.TravelDtoOut;
import org.example.carpooling.models.dto.TravelDtoUpdate;
import org.example.carpooling.models.dto.TravelSearchDto;
import org.example.carpooling.models.enums.TravelStatus;
import org.example.carpooling.services.interfaces.TravelRequestService;
import org.example.carpooling.services.interfaces.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.example.carpooling.exceptions.IllegalArgumentException;

import java.util.List;

@Controller
@RequestMapping("/travels")
public class TravelMvcController {
    public static final String LISTINGS_PER_PAGE = "8";
    private final TravelService travelService;
    private final AuthenticationHelper authenticationHelper;
    private final TravelRequestService travelRequestService;
    private final ModelMapper modelMapper;

    @Autowired
    public TravelMvcController(TravelService travelService, AuthenticationHelper authenticationHelper, TravelRequestService travelRequestService, ModelMapper modelMapper) {
        this.travelService = travelService;
        this.authenticationHelper = authenticationHelper;
        this.travelRequestService = travelRequestService;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping
    public String getAllTravels(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = LISTINGS_PER_PAGE) int size,
                                @RequestParam(defaultValue = "createdAt") String sortField,
                                @RequestParam(defaultValue = "desc") String sortDirection,
                                @Valid @ModelAttribute("travelSearchDto") TravelSearchDto searchDto,
                                Model model,
                                HttpSession httpSession) {
        try {
            authenticationHelper.tryGetCurrentUser(httpSession);

            if ("createdAt".equals(sortField)) {
                sortField = "createdAt";
            }
            Sort sort = Sort.by(sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
            PageRequest pageRequest = PageRequest.of(page, size, sort);

            Page<Travel> travelPage = travelService.searchTravelsPaginated(
                    searchDto.getTravelTitle(),
                    searchDto.getStartingPoint(),
                    searchDto.getEndingPoint(),
                    searchDto.getDepartureTime(),
                    searchDto.getFreeSpots(),
                    searchDto.getCreatedAt(),
                    pageRequest);
            model.addAttribute("travelsPaged", travelPage.getContent());
            model.addAttribute("travelsSize", travelPage.getTotalElements());
            model.addAttribute("sortField", sortField);
            model.addAttribute("currentPage", travelPage.getNumber());
            model.addAttribute("totalPages", travelPage.getTotalPages());
            model.addAttribute("sortDirection", sortDirection);
            model.addAttribute("travelSearchDto", searchDto);

            return "TravelsView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{travelId}")
    public String showSingleTravel(@PathVariable int travelId, Model model, HttpSession httpSession) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(httpSession);
            Travel travel = travelService.getById(travelId);
            List<TravelRequest> populateTravelRequests = travelRequestService.getTravelRequestsForPopulate(currentUser, travelId);

            boolean hasApplied = !populateTravelRequests.isEmpty();
            boolean isDriver = currentUser.getUserId() == travel.getDriver().getUserId();
            boolean isPassenger = travel.getPassengers().contains(currentUser);
            boolean isCancelled = travel.getTravelStatus().equals(TravelStatus.Cancelled);
            boolean isCompleted = travel.getTravelStatus().equals(TravelStatus.Complete);

            model.addAttribute("hasApplied", hasApplied);
            model.addAttribute("isDriver", isDriver);
            model.addAttribute("isPassenger", isPassenger);
            model.addAttribute("isCancelled", isCancelled);
            model.addAttribute("isCompleted", isCompleted);
            model.addAttribute("requests", populateTravelRequests);
            model.addAttribute("travel", travel);

            return "TravelView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{travelId}/apply")
    public String userApplyForTravel(@PathVariable int travelId,
                                     Model model,
                                     HttpSession httpSession) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(httpSession);
            Travel travel = travelService.getById(travelId);
            travelRequestService.createTravelRequest(currentUser, travel);

            return "redirect:/travels";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (IllegalArgumentException e) {
            model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{travelId}/cancel-request")
    public String cancelRequestForTravel(@PathVariable int travelId,
                                         Model model,
                                         HttpSession httpSession) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(httpSession);
            Travel travel = travelService.getById(travelId);
            if (travel.getPassengers().remove(currentUser)) {
                travel.setFreeSpots(travel.getFreeSpots() + 1);
            }
            travelRequestService.deleteTravelRequestByTravelId(travelId, currentUser);

            return "redirect:/travels";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (IllegalArgumentException e) {
            model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{travelId}/requests/{requestId}/accept")
    public String acceptTravel(@PathVariable int travelId,
                               @PathVariable int requestId,
                               Model model,
                               HttpSession httpSession) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(httpSession);
            travelRequestService.approveRequest(currentUser, travelId, requestId);

            return "redirect:/travels/" + travelId;
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (IllegalArgumentException e) {
            model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{travelId}/requests/{requestId}/reject")
    public String rejectRequest(@PathVariable int travelId,
                                @PathVariable int requestId,
                                Model model,
                                HttpSession httpSession) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(httpSession);
            travelRequestService.rejectRequest(currentUser, travelId, requestId);

            return "redirect:/travels/" + travelId;
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (IllegalArgumentException e) {
            model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{travelId}/complete")
    public String completeTravel(@PathVariable int travelId, Model model, HttpSession httpSession) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(httpSession);
            Travel travel = travelService.getById(travelId);
            travelService.changeTravelStatusToFinished(travel, currentUser);

            return "redirect:/travels";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (IllegalArgumentException e) {
            model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/create")
    public String showTravelCreate(Model model, HttpSession httpSession) {
        try {
            authenticationHelper.tryGetCurrentUser(httpSession);
            model.addAttribute("travel", new TravelDtoIn());
            return "TravelCreateView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/create")
    public String handleCreateTravel(@Valid @ModelAttribute("travel") TravelDtoIn travelDtoIn,
                                     BindingResult bindingResult,
                                     Model model,
                                     HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return "TravelCreateView";
        }
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(httpSession);

            Travel travel = modelMapper.fromTravelDtoInToTravel(travelDtoIn, currentUser);

            travelService.createTravel(travel);
            return "redirect:/travels";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "TravelCreateView";
        }
    }

    @GetMapping("/{travelId}/update")
    public String showTravelUpdate(@PathVariable int travelId,
                                   Model model,
                                   HttpSession httpSession) {
        try {
            authenticationHelper.tryGetCurrentUser(httpSession);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            travelService.getById(travelId);
            authenticationHelper.tryGetCurrentUser(httpSession);
            model.addAttribute("travel", new TravelDtoIn());
            model.addAttribute("travelId", travelId);

            return "TravelUpdateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{travelId}/update")
    public String handleTravelUpdate(@PathVariable int travelId,
                                     @Valid @ModelAttribute("travel") TravelDtoIn travelDto,
                                     BindingResult bindingResult,
                                     HttpSession httpSession,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            return "TravelCreateView";
        }
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(httpSession);

            Travel travel = modelMapper.fromTravelDtoInUpdateToTravel(travelDto, travelId);

            travelService.updateTravel(travel, currentUser);
            return "redirect:/travels";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "TravelCreateView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{travelId}/cancel")
    public String cancelTravel(@PathVariable int travelId,
                               Model model,
                               HttpSession httpSession) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(httpSession);
            Travel travel = travelService.getById(travelId);
            travelService.changeTravelStatusToCancelled(travel, currentUser);
            return "redirect:/travels";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
}
