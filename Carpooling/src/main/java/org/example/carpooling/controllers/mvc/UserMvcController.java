package org.example.carpooling.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.helpers.AuthenticationHelper;
import org.example.carpooling.helpers.ModelMapper;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.UserDtoUpdate;
import org.example.carpooling.models.dto.UserDtoUpdateOut;
import org.example.carpooling.services.interfaces.TravelService;
import org.example.carpooling.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserMvcController {
    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final TravelService travelService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserMvcController(AuthenticationHelper authenticationHelper, UserService userService, TravelService travelService, ModelMapper modelMapper) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.travelService = travelService;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession httpSession) {
        return httpSession.getAttribute("currentUser") != null;
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
        List<Travel> userTravels;

        try {
            user = authenticationHelper.tryGetCurrentUser(httpSession);
            userTravels = travelService.getByDriver(user.getUserId());
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("userTravels", userTravels);

        return "UserProfileView";
    }

    @GetMapping("/{userId}")
    public String showSingleUser(@PathVariable int userId,
                                 Model model,
                                 HttpSession httpSession) {
        try {
            authenticationHelper.tryGetCurrentUser(httpSession);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        User displayedUser = userService.getById(userId);

        try {
            model.addAttribute("user", displayedUser);

            return "SingleUserView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @GetMapping("/update")
    public String getUpdateUserProfile(Model model, HttpSession httpSession) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(httpSession);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        UserDtoUpdateOut updateUser = modelMapper.fromUserToUserDtoUpdateOut(user);
        model.addAttribute("user", updateUser);

        return "userUpdateProfileView";
    }

    @PostMapping("/update")
    public String updateUserProfile(@Valid @ModelAttribute("user") UserDtoUpdate userDtoUpdate,
                                    BindingResult bindingResult,
                                    Model model,
                                    HttpSession httpSession) {
        User authenticatedUser;
        try {
            authenticatedUser = authenticationHelper.tryGetCurrentUser(httpSession);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDtoUpdate);
            return "UserUpdateProfileView";
        }

        try {
            User updatedUser = modelMapper.fromUserDtoUpdate(userDtoUpdate, authenticatedUser.getUserId());

            userService.updateUser(updatedUser, authenticatedUser);
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());

            return "ErrorView";
        }

        UserDtoUpdateOut userDtoUpdateOut = modelMapper.fromUserToUserDtoUpdateOut(authenticatedUser);
        return "UserUpdateProfileView";
    }

    @GetMapping("/delete/{userId}")
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
