package org.example.carpooling.controllers.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.helpers.AuthenticationHelper;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.dto.TravelSearchDto;
import org.example.carpooling.services.interfaces.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/travels")
public class TravelMvcController {
    public static final String LISTINGS_PER_PAGE = "8";
    private final TravelService travelService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public TravelMvcController(TravelService travelService, AuthenticationHelper authenticationHelper) {
        this.travelService = travelService;
        this.authenticationHelper = authenticationHelper;
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
                                @RequestParam(defaultValue = "title") String sortField,
                                @RequestParam(defaultValue = "asc") String sortDirection,
                                @Valid @ModelAttribute("travelSearchDto")TravelSearchDto searchDto,
                                Model model,
                                HttpSession httpSession) {
        try{
            authenticationHelper.tryGetCurrentUser(httpSession);

        if ("title".equals(sortField)) {
            sortField = "title";
        }
        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<Travel> travelPage = travelService.searchTravelsPaginated(
                searchDto.getTravelTitle(),
                searchDto.getStartingPoint(),
                searchDto.getEndingPoint(),
                searchDto.getDepartureTime(),
                searchDto.getFreeSpots(),
                pageRequest);
        model.addAttribute("travelsPaged", travelPage.getContent());
        model.addAttribute("travelsSize", travelPage.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("currentPage",travelPage.getNumber());
        model.addAttribute("totalPages",travelPage.getTotalPages());
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("travelSearchDto", searchDto);

        return "TravelsView";
        } catch(AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{travelId}")
    public String showSingleTravel(@PathVariable int travelId, Model model, HttpSession httpSession){
        try {
            authenticationHelper.tryGetCurrentUser(httpSession);
            model.addAttribute("travel", travelService.getById(travelId));

            return "ТravelView";
        } catch (AuthorizationException e){
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (EntityNotFoundException e){
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
}
