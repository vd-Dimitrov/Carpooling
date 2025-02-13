package org.example.carpooling.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.helpers.AuthenticationHelper;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.LoginDto;
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

    @Autowired
    public AuthenticationMvcController(AuthenticationHelper authenticationHelper) {
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession httpSession){
        return httpSession.getAttribute("currentUser") != null;
    }

    @GetMapping("/login")
    public String showLogin(Model model){
        model.addAttribute("login", new LoginDto());
        return "LoginView";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto login,
                              BindingResult bindingResult,
                              HttpSession httpSession){
       if (bindingResult.hasErrors()){
           return "LoginView";
       }

       try {
            User user = authenticationHelper.verifyUser(login.getUsername(), login.getPassword());
            httpSession.setAttribute("currentUser", login.getUsername());
            return "redirect:/";
       } catch (AuthorizationException e){
           bindingResult.rejectValue("username", "auth_error", e.getMessage());
           return "LoginView";
       }
    }
}
