package org.example.carpooling.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import org.example.carpooling.helpers.AuthenticationHelper;
import org.example.carpooling.models.dto.LoginDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
}
