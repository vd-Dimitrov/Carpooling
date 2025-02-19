package org.example.carpooling.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityDuplicateException;
import org.example.carpooling.helpers.AuthenticationHelper;
import org.example.carpooling.helpers.ModelMapper;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.LoginDto;
import org.example.carpooling.models.dto.UserDtoIn;
import org.example.carpooling.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationMvcController {
    private final AuthenticationHelper authenticationHelper;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Autowired
    public AuthenticationMvcController(AuthenticationHelper authenticationHelper, ModelMapper modelMapper, UserService userService) {
        this.authenticationHelper = authenticationHelper;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession httpSession) {
        return httpSession.getAttribute("currentUser") != null;
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("login", new LoginDto());
        return "LoginView";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto login,
                              BindingResult bindingResult,
                              HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return "LoginView";
        }

        try {
            authenticationHelper.verifyUser(login.getUsername(), login.getPassword());
            httpSession.setAttribute("currentUser", login.getUsername());
            return "redirect:/";
        } catch (AuthorizationException e) {
            bindingResult.rejectValue("username", "auth_error", e.getMessage());
            return "LoginView";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession httpSession) {
        httpSession.removeAttribute("currentUser");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new UserDtoIn());
        return "RegisterView";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") UserDtoIn userDtoIn,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "RegisterView";
        }

        if (!userDtoIn.getPassword().equals(userDtoIn.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "password_error", "Passwords should match");
            return "RegisterView";
        }

        try {
            User user = modelMapper.fromUserDto(userDtoIn);
            userService.createUser(user);
            return "redirect:/auth/login";
        } catch (EntityDuplicateException e) {
            String rejectedValue = duplicateErrorCause(e.getMessage());
            bindingResult.rejectValue(rejectedValue, rejectedValue + "_error", e.getMessage());
            return "RegisterView";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("email", "email_error", e.getMessage());
            return "RegisterView";
        }
    }


    private String duplicateErrorCause(String errorMessage) {
        String[] splitStr = errorMessage.split("\\s+");
        return switch (splitStr[1]) {
            case "username" -> "username";
            case "email" -> "email";
            case "phone" -> "phone number";
            default -> "No error";
        };
    }
}
