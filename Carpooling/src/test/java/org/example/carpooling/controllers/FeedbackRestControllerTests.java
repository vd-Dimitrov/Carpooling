package org.example.carpooling.controllers;

import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.helpers.AuthenticationHelper;
import org.example.carpooling.helpers.ModelMapper;
import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.User;
import org.example.carpooling.services.interfaces.FeedbackService;
import org.example.carpooling.services.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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
public class FeedbackRestControllerTests {
    @MockitoBean
    FeedbackService mockFeedbackService;

    @MockitoBean
    UserService mockUserService;

    @MockitoBean
    ModelMapper mockMapper;

    @MockitoBean
    AuthenticationHelper mockAuthenticationHelper;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getAllFeedbacks_Should_ReturnStatusOk() throws Exception {
        // Arrange
        User mockUser = createMockUser();
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenReturn(mockUser);
        List<Feedback> mockFeedbackList = new ArrayList<>();
        Mockito.when(mockFeedbackService.getAllFeedback())
                .thenReturn(mockFeedbackList);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/feedback"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllFeedbacks_Should_ReturnStatusIsUnauthorized_When_InvalidAuthentication() throws Exception {
        // Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, null));
        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/feedback"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void getFeedbackById_Should_ReturnStatusOk_When_FeedbackExists() throws Exception {
        User mockUser = createMockUser();

        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenReturn(mockUser);

        Feedback mockFeedback = createMockFeedback();
        Mockito.when(mockFeedbackService.getFeedbackById(Mockito.anyInt()))
                .thenReturn(mockFeedback);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/feedback/{feedbackId}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getFeedbackById_Should_ReturnStatusNotFound_When_FeedbackDoesNotExist() throws Exception{
        // Arrange
        Mockito.when(mockFeedbackService.getFeedbackById(Mockito.anyInt()))
                .thenThrow(EntityNotFoundException.class);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/feedback/{feedbackId}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getFeedbackById_Should_ReturnStatusInvalidAuthorization_When_InvalidAuthentication() throws Exception{
        // Arrange
        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/feedback/{feedbackId}", 1))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void getFeedbacksByAuthor_Should_ReturnStatusOk() throws Exception{
        User mockUser = createMockUser();

        Mockito.when(mockAuthenticationHelper.tryGetUser(Mockito.any()))
                .thenReturn(mockUser);

        List<Feedback> mockFeedbackList=  new ArrayList<>();
        Mockito.when(mockFeedbackService.getFeedbackByAuthor(mockUser))
                .thenReturn(mockFeedbackList);

        // Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/feedback/author/{userId}", mockUser.getUserId()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
