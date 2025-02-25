package org.example.carpooling.controllers.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.helpers.AuthenticationHelper;
import org.example.carpooling.helpers.ModelMapper;
import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.UserDtoUpdate;
import org.example.carpooling.models.dto.UserDtoUpdateOut;
import org.example.carpooling.services.interfaces.FeedbackService;
import org.example.carpooling.services.interfaces.TravelService;
import org.example.carpooling.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserMvcController {
    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final TravelService travelService;
    private final ModelMapper modelMapper;
    private final FeedbackService feedbackService;

    @Autowired
    public UserMvcController(AuthenticationHelper authenticationHelper, UserService userService, TravelService travelService, ModelMapper modelMapper, FeedbackService feedbackService) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.travelService = travelService;
        this.modelMapper = modelMapper;
        this.feedbackService = feedbackService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession httpSession) {
        return httpSession.getAttribute("currentUser") != null;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }


    @ModelAttribute("currentUsername")
    public String populateCurrentUsername(HttpSession httpSession) {
        Object currentUser = httpSession.getAttribute("currentUser");
        if (currentUser != null) {
            return httpSession.getAttribute("currentUser").toString();
        }

        return "";
    }

    @GetMapping("/profile")
    public String getUserProfile(Model model, HttpSession httpSession) {
        User user;

        try {
            user = authenticationHelper.tryGetCurrentUser(httpSession);
            List<Feedback> userFeedbacks = feedbackService.getFeedbackByReceiver(user);
            model.addAttribute("user", user);
            model.addAttribute("feedbackList", userFeedbacks);
            model.addAttribute("isOwner", true);
            return "UserProfileView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{userId}")
    public String showSingleUser(@PathVariable int userId,
                                 Model model,
                                 HttpSession httpSession) {
        User currentUser;
        try {
            currentUser = authenticationHelper.tryGetCurrentUser(httpSession);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        try {
            User displayedUser = userService.getById(userId);
            boolean isOwner = currentUser.equals(displayedUser);
            model.addAttribute("isOwner", isOwner);
            model.addAttribute("user", displayedUser);

            return "UserProfileView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/{userId}/update")
    public String getUpdateUserProfile(@PathVariable int userId,
                                       Model model,
                                       HttpSession httpSession) {
        try {
             authenticationHelper.tryGetCurrentUser(httpSession);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
        try {
            userService.getById(userId);
            model.addAttribute("user", new UserDtoUpdate());
            model.addAttribute("userId", userId);

            return "UserUpdateView";
        } catch (EntityNotFoundException e){
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/{userId}/update")
    public String handleUpdateUserProfile(@PathVariable int userId,
                                    @Valid @ModelAttribute("user") UserDtoUpdate userDtoUpdate,
                                    BindingResult bindingResult,
                                    Model model,
                                    HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDtoUpdate);
            return "UserUpdateView";
        }
        User authenticatedUser;
        try {
            authenticatedUser = authenticationHelper.tryGetCurrentUser(httpSession);
            User updatedUser = modelMapper.fromUserDtoUpdate(userDtoUpdate, userId);

            userService.updateUser(updatedUser, authenticatedUser);
            return "redirect:/users/profile";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{userId}/delete")
    public String deleteUser(@PathVariable int userId,
                             Model model,
                             HttpSession httpSession) {
        User authenticatedUser;
        try {
            authenticatedUser = authenticationHelper.tryGetCurrentUser(httpSession);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            userService.deleteUser(userId, authenticatedUser);

            return "redirect:/logout";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        } catch (AuthorizationException e) {
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return "ErrorView";
        }
    }
}
