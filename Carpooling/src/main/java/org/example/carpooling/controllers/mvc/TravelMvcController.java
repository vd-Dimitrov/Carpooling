package org.example.carpooling.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import org.example.carpooling.services.interfaces.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/travels")
public class TravelMvcController {
    private final TravelService travelService;

    @Autowired
    public TravelMvcController(TravelService travelService) {
        this.travelService = travelService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("isAuthenticated") != null;
    }

}
