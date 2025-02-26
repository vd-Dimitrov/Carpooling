package org.example.carpooling.controllers.rest;

import jakarta.validation.Valid;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.exceptions.IllegalArgumentException;
import org.example.carpooling.helpers.AuthenticationHelper;
import org.example.carpooling.helpers.ModelMapper;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.*;
import org.example.carpooling.services.interfaces.TravelRequestService;
import org.example.carpooling.services.interfaces.TravelService;
import org.example.carpooling.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/travel")
public class TravelRestController {
    private final TravelService travelService;
    private final TravelRequestService requestService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;


    public TravelRestController(TravelService travelService, TravelRequestService requestService, UserService userService, ModelMapper modelMapper,
                                AuthenticationHelper authenticationHelper) {
        this.travelService = travelService;
        this.requestService = requestService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<TravelDtoOut> getAllTravels(@RequestHeader HttpHeaders httpHeaders) {
        try {
            authenticationHelper.tryGetUser(httpHeaders);
            List<Travel> travelList = travelService.getAllTravels();

            return modelMapper.fromListTravelsToListTravelDtoOut(travelList);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public TravelDtoOut getTravelById(@RequestHeader HttpHeaders httpHeaders,
                                      @PathVariable int id) {
        try {
            authenticationHelper.tryGetUser(httpHeaders);

            return modelMapper.fromTravelToTravelDtoOut(travelService.getById(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/search")
    public List<TravelDtoOut> searchTravels(@RequestHeader HttpHeaders httpHeaders,
                                            @RequestParam(required = false) String title,
                                            @RequestParam(required = false) String startingPoint,
                                            @RequestParam(required = false) String endingPoint,
                                            @RequestParam(required = false) String departureTime,
                                            @RequestParam(required = false) String travelStatus,
                                            @RequestParam(required = false) int freeSpots,
                                            @RequestParam(required = false) String createdAt) {
        try {
            authenticationHelper.tryGetUser(httpHeaders);
            List<Travel> travelList = travelService.searchTravels(title, startingPoint, endingPoint, departureTime, freeSpots, createdAt);

            return modelMapper.fromListTravelsToListTravelDtoOut(travelList);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/user/{id}")
    public List<TravelDtoOut> getAllTravelsOfUser(@RequestHeader HttpHeaders httpHeaders,
                                                  @PathVariable int id) {
        try {
            authenticationHelper.tryGetUser(httpHeaders);
            List<Travel> travels = travelService.getByDriver(id);

            return modelMapper.fromListTravelsToListTravelDtoOut(travels);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/create")
    public TravelDtoOut createTravel(@RequestHeader HttpHeaders httpHeaders,
                                     @Valid @RequestBody TravelDtoIn travelDto) {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            Travel travel = modelMapper.fromTravelDtoInToTravel(travelDto, user);
            travelService.createTravel(travel);
            return modelMapper.fromTravelToTravelDtoOut(travel);
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public TravelDtoOut updateTravel(@RequestHeader HttpHeaders headers,
                                     @Valid @RequestBody TravelDtoIn travelDtoIn,
                                     @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Travel travel = modelMapper.fromTravelDtoInToTravel(
                    travelDtoIn,
                    user,
                    id);
            travelService.updateTravel(travel, user);

            return modelMapper.fromTravelToTravelDtoOut(travel);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTravel(@RequestHeader HttpHeaders headers,
                             @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            travelService.deleteTravel(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/{travelId}/apply")
    public void applyForTravel(@RequestHeader HttpHeaders httpHeaders,
                               @PathVariable int travelId) {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            Travel travel = travelService.getById(travelId);
            requestService.createTravelRequest(user, travel);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{travelId}/apply/{requestId}/approve")
    public void approveRequest(HttpHeaders httpHeaders,
                               @PathVariable int travelId,
                               @PathVariable int requestId) {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            requestService.approveRequest(user, travelId, requestId);

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{travelId}/apply/{requestId}/reject")
    public void rejectRequest(HttpHeaders httpHeaders,
                              @PathVariable int travelId,
                              @PathVariable int requestId) {
        try {
            User user = authenticationHelper.tryGetUser(httpHeaders);
            requestService.rejectRequest(user, travelId, requestId);

        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
