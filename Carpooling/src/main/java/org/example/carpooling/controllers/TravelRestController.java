package org.example.carpooling.controllers;

import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.helpers.AuthenticationHelper;
import org.example.carpooling.helpers.ModelMapper;
import org.example.carpooling.models.dto.TravelDtoOut;
import org.example.carpooling.services.interfaces.TravelService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/travel")
public class TravelRestController {
    private final TravelService travelService;
    private final ModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;


    public TravelRestController(TravelService travelService, ModelMapper modelMapper,
                                AuthenticationHelper authenticationHelper) {
        this.travelService = travelService;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/{id}")
    public TravelDtoOut getTravelById(@RequestHeader HttpHeaders httpHeaders,
                                      @PathVariable int id){
        try{
            authenticationHelper.tryGetUser(httpHeaders);

            return modelMapper.fromTravelToTravelDtoOut(travelService.getById(id));
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
