package org.example.carpooling.controllers;

import org.example.carpooling.helpers.AuthenticationHelper;
import org.example.carpooling.helpers.ModelMapper;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.services.interfaces.TravelService;
import org.example.carpooling.services.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.example.carpooling.Helpers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TravelRestControllerTests {
    @MockitoBean
    TravelService mockTravelService;

    @MockitoBean
    UserService mockUserService;

    @MockitoBean
    ModelMapper mockMapper;

    @MockitoBean
    AuthenticationHelper mockAuthenticationHelper;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getAllTravels_Should_ReturnStatusOk() throws Exception{
        // Act
        List<Travel> mockTravelList = new ArrayList<>();

        Mockito.when(mockTravelService.getAllTravels()).thenReturn(mockTravelList);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/travel"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllUsers_Should_ReturnStatusIsUnauthorized_When_InvalidAuthentication() throws Exception{
        // Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, null));

        // Act, Assert
        String body = toJson(createMockTravel());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/travel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
