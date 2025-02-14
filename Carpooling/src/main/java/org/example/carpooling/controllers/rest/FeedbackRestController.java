package org.example.carpooling.controllers.rest;

import jakarta.validation.Valid;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.helpers.AuthenticationHelper;
import org.example.carpooling.helpers.ModelMapper;
import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.FeedbackDtoIn;
import org.example.carpooling.models.dto.FeedbackDtoOut;
import org.example.carpooling.services.interfaces.FeedbackService;
import org.example.carpooling.services.interfaces.TravelService;
import org.example.carpooling.services.interfaces.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackRestController {
    private final FeedbackService feedbackService;
    private final AuthenticationHelper authenticationHelper;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final TravelService travelService;

    public FeedbackRestController(FeedbackService feedbackService, AuthenticationHelper authenticationHelper, ModelMapper modelMapper, UserService userService, TravelService travelService) {
        this.feedbackService = feedbackService;
        this.authenticationHelper = authenticationHelper;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.travelService = travelService;
    }

    @GetMapping("/{feedbackId}")
    public FeedbackDtoOut getFeedbackById(@RequestHeader HttpHeaders httpHeaders,
                                          @PathVariable int feedbackId) {
        try {
            authenticationHelper.tryGetUser(httpHeaders);

            return modelMapper.fromFeedbackToFeedbackDtoOut(feedbackService.getFeedbackById(feedbackId));
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/author/{userId}")
    public List<FeedbackDtoOut> getFeedbacksByAuthor(@RequestHeader HttpHeaders httpHeaders,
                                                     @PathVariable int userId){
        try{
            authenticationHelper.tryGetUser(httpHeaders);

            User requestedUser = userService.getById(userId);
            List<Feedback> feedbacks = feedbackService.getFeedbackByAuthor(requestedUser);
            return modelMapper.fromListFeedbackToListFeedbackDtoOut(feedbacks);
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public List<FeedbackDtoOut> getAllFeedbacks(@RequestHeader HttpHeaders httpHeaders){
        try {
            authenticationHelper.tryGetUser(httpHeaders);
            List<Feedback> feedbacks = feedbackService.getAllFeedback();
            return modelMapper.fromListFeedbackToListFeedbackDtoOut(feedbacks);
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping("/{travelId}")
    public FeedbackDtoOut createNewFeedback(@RequestHeader HttpHeaders httpHeaders,
                                                  @Valid @RequestBody FeedbackDtoIn feedbackDtoIn,
                                                  @PathVariable int travelId){
        try {
            User feedbackAuthor = authenticationHelper.tryGetUser(httpHeaders);
            Feedback feedback = modelMapper.fromFeedbackDtoInToFeedback(feedbackDtoIn, feedbackAuthor);
            travelService.addFeedback(travelId, feedback);

            return modelMapper.fromFeedbackToFeedbackDtoOut(feedback);
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{feedbackId}")
    public FeedbackDtoOut updateFeedback(@RequestHeader HttpHeaders httpHeaders,
                                         @Valid @RequestBody FeedbackDtoIn feedbackDtoIn,
                                         @PathVariable int feedbackId){
        try {
            User feedbackAuthor = authenticationHelper.tryGetUser(httpHeaders);
            Feedback feedback = modelMapper.fromFeedbackDtoInToFeedback(feedbackDtoIn, feedbackAuthor);
            feedbackService.updateFeedback(feedback, feedbackAuthor);

            return modelMapper.fromFeedbackToFeedbackDtoOut(feedback);
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @DeleteMapping("/{feedbackId}")
    public void deleteFeedback(@RequestHeader HttpHeaders httpHeaders,
                               @PathVariable int feedbackId){
        try {
            User requestingUser = authenticationHelper.tryGetUser(httpHeaders);
            Feedback deletedFeedback = feedbackService.getFeedbackById(feedbackId);
            feedbackService.deleteFeedback(deletedFeedback, requestingUser);
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
