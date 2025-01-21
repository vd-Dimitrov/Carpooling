package org.example.carpooling.controllers;

import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.helpers.AuthenticationHelper;
import org.example.carpooling.helpers.ModelMapper;
import org.example.carpooling.models.User;
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
public class UserRestControllerTests {
    @MockitoBean
    UserService mockService;

    @MockitoBean
    ModelMapper mockMapper;

    @MockitoBean
    AuthenticationHelper mockAuthenticationHelper;

    @Autowired
    MockMvc mockMvc;

    // ToDo fix unit test
    @Test
    public void getUserById_Should_ReturnStatusOk_When_UserExists() throws Exception {
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockService.getById(Mockito.anyInt()))
                .thenReturn(mockUser);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(mockUser.getUsername()));
    }

    @Test
    public void getUserById_Should_ReturnStatusNotFound_When_UserDoesNotExist() throws Exception {
        // Arrange
        Mockito.when(mockService.getById(Mockito.anyInt()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getUserById_Should_ReturnStatusInvalidAuthorization_When_InvalidAuthentication() throws Exception {
        // Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, null));

        // Act, Assert
        String body = toJson(createMockUserOutDto());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }

    @Test
    public void getAllUsers_Should_ReturnStatusOk() throws Exception{
        // Act
        User mockUser = createMockUser();

        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenReturn(mockUser);

        List<User> mockUserList = new ArrayList<>();

        Mockito.when(mockService.getAllUsers())
                .thenReturn(mockUserList);


        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllUsers_Should_ReturnStatusIsUnauthorized_When_InvalidAuthentication() throws Exception {
        // Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, null));

        // Act, Assert
        String body = toJson(createMockUserOutDto());
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

    }

    // ToDo fix unit test
    @Test
    public void registerUser_Should_ReturnStatusOk_When_CorrectRequest() throws Exception {
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockMapper.fromUserDto(Mockito.any()))
                .thenReturn(mockUser);

        // Act, Assert
        String body = toJson(createMockUserOutDto());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
