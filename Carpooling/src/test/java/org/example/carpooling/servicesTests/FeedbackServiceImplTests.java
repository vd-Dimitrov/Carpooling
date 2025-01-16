package org.example.carpooling.servicesTests;

import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.User;
import org.example.carpooling.repositories.FeedbackRepository;
import org.example.carpooling.services.FeedbackServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.carpooling.Helpers.createMockFeedback;
import static org.example.carpooling.Helpers.createMockUser;

public class FeedbackServiceImplTests {
    @Mock
    FeedbackRepository mockFeedbackRepository;

    @InjectMocks
    FeedbackServiceImpl mockFeedbackService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createFeedback_Should_CallRepository_When_FeedbackWithSameIdExists(){
        // Arrange
        Feedback mockFeedback = createMockFeedback();

        Mockito.when(mockFeedbackRepository.findFeedbackByFeedbackId(Mockito.anyInt()))
                .thenThrow(EntityNotFoundException.class);

        mockFeedbackService.createFeedback(mockFeedback);

        Mockito.verify(mockFeedbackRepository, Mockito.times(1))
                .save(Mockito.any());
    }

    @Test
    void findById_Should_ReturnFeedback_When_FeedbackExists(){
        //Arrange
        Feedback mockFeedback = createMockFeedback();

        Mockito.when(mockFeedbackRepository.findFeedbackByFeedbackId(Mockito.anyInt()))
                .thenReturn(Optional.of(mockFeedback));

        // Act
        Feedback result = mockFeedbackService.findById(Mockito.anyInt());

        // Assert
        Assertions.assertEquals(mockFeedback, result);
    }

    @Test
    void getAllFeedbacks_Should_CallRepository(){
        // Arrange
        List<Feedback> mockFeedbackList = new ArrayList<>();

        Mockito.when(mockFeedbackRepository.findAll())
                .thenReturn(mockFeedbackList);
        // Act
        List<Feedback> result = mockFeedbackService.getAllFeedback();

        // Assert
        Assertions.assertEquals(mockFeedbackList, result);
    }

    @Test
    public void getByAuthor_Should_ReturnListOfFeedbacks_When_AuthorExists(){
        // Arrange
        User mockUser = createMockUser();
        List<Feedback> mockFeedbacks = List.of(createMockFeedback(), createMockFeedback());

        Mockito.when(mockFeedbackRepository.findFeedbackByAuthor(mockUser))
                .thenReturn(Optional.of(mockFeedbacks));

        // Act
        List<Feedback> result = mockFeedbackService.getFeedbackByAuthor(mockUser);

        // Assert
        Assertions.assertEquals(mockFeedbacks, result);
    }

    @Test
    void updateFeedback_Should_CallRepository_When_FeedbackExists(){
        Feedback mockFeedback = createMockFeedback();
        User mockUser = createMockUser();

        mockFeedbackService.updateFeedback(mockFeedback, mockUser);

        Mockito.verify(mockFeedbackRepository, Mockito.times(1))
                .save(mockFeedback);
    }

    @Test
    void updateFeedback_Should_Throw_When_UserIsNotAuthor(){
        Feedback mockFeedback = createMockFeedback();
        User mockUser = createMockUser();
        mockUser.setUserId(2);

        Assertions.assertThrows(AuthorizationException.class,
                () -> mockFeedbackService.updateFeedback(mockFeedback, mockUser));
    }

    @Test
    public void deleteFeedback_Should_CallRepository_When_UserIsAuthor(){
        Feedback mockFeedback = createMockFeedback();
        User mockUser = createMockUser();

        mockFeedbackService.deleteFeedback(mockFeedback, mockUser);

        Mockito.verify(mockFeedbackRepository, Mockito.times(1))
                .delete(mockFeedback);
    }
}
