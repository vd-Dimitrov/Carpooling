package org.example.carpooling.controllers;

import org.example.carpooling.exceptions.EntityNotFoundException;
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
        // Arrange
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
        String body = toJson(createMockTravelDtoOut());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/travel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void getTravelById_Should_ReturnStatusOk_When_UserExists() throws Exception{
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockUserService.getById(Mockito.anyInt()))
                .thenReturn(mockUser);

        Travel mockTravel = createMockTravel();
        Mockito.when(mockTravelService.getById(Mockito.anyInt()))
                .thenReturn(mockTravel);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/travel/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getTravelById_Should_ReturnStatusNotFound_When_TravelDoesNotExist() throws Exception{
        // Arrange
        Mockito.when(mockTravelService.getById(Mockito.anyInt()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/travel/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getTravelById_Should_ReturnStatusInvalidAuthorization_When_InvalidAuthentication() throws  Exception{
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        String body = toJson(createMockTravelDtoOut());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/travel/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void getAllTravelsOfUser_Should_ReturnStatusOk() throws Exception{
        User mockUser = createMockUser();

        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenReturn(mockUser);

        List<Travel> mockTravelList = new ArrayList<>();

        Mockito.when(mockTravelService.getAllTravels())
                .thenReturn(mockTravelList);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/travel/user/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllTravelsOfUser_Should_ReturnStatusEntityNotFound_When_UserDoesNotExist() throws Exception{
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenThrow(EntityNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/travel/user/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void createTravel_Should_ReturnStatusOk_When_CorrectRequest() throws Exception{
        String body = toJson(createMockTravelDtoIn());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/travel/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
