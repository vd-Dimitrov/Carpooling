package org.example.carpooling.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.helpers.AuthenticationHelper;
import org.example.carpooling.services.interfaces.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/travels")
public class TravelMvcController {
    private final TravelService travelService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public TravelMvcController(TravelService travelService, AuthenticationHelper authenticationHelper) {
        this.travelService = travelService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("isAuthenticated") != null;
    }

    @GetMapping
    public String getAllTravels(HttpSession httpSession){
        try{
            authenticationHelper.tryGetCurrentUser(httpSession);
        } catch(AuthorizationException e){
            return "redirect:/auth/login";
        }

        return "travel/travel-view";
    }
}
