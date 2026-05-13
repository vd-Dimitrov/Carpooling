package org.example.carpooling.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.helpers.AuthenticationHelper;
import org.example.carpooling.models.User;
import org.example.carpooling.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminMvcController {
    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;

    @Autowired
    public AdminMvcController(AuthenticationHelper authenticationHelper, UserService userService) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin(HttpSession session) {
        try {
            return authenticationHelper.tryGetCurrentUser(session).isAdmin();
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("/users")
    public String getAllUsers(Model model, HttpSession session) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(session);
            if (!currentUser.isAdmin()) {
                model.addAttribute("statusCode", HttpStatus.FORBIDDEN.getReasonPhrase());
                model.addAttribute("error", "Admin access required.");
                return "ErrorView";
            }
            List<User> users = userService.getAllUsers();
            model.addAttribute("users", users);
            return "AdminUsersView";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/users/{userId}/suspend")
    public String suspendUser(@PathVariable int userId,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate suspendUntil,
                              HttpSession session,
                              Model model) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(session);
            userService.suspendUser(userId, suspendUntil, currentUser);
            return "redirect:/admin/users";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }

    @PostMapping("/users/{userId}/delete")
    public String deleteUser(@PathVariable int userId,
                             HttpSession session,
                             Model model) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(session);
            userService.deleteUser(userId, currentUser);
            return "redirect:/admin/users";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "ErrorView";
        }
    }
}