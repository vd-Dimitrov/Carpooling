package org.example.carpooling.servicesTests;

import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.models.User;
import org.example.carpooling.repositories.UserRepository;
import org.example.carpooling.services.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.example.carpooling.Helpers.*;
public class UserServiceImplTests {
    @Mock
    UserRepository mockUserRepository;

    @InjectMocks
    UserServiceImpl mockUserService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getByUsername_Should_ReturnUser_When_UsernameExists(){
        // Arrange
        User mockUser = createMockUser();


        Mockito.when(mockUserRepository.findUserByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        // Act
        User result = mockUserService.getByUsername(Mockito.anyString());

        // Assert
        Assertions.assertEquals(mockUser, result);
    }

    @Test
    void createUser_should_CallRepository_When_UserWithSameUsernameExist(){
        // Arrange
        User mockUser = createMockUser();


        Mockito.when(mockUserRepository.findUserByUsername(Mockito.anyString()))
                .thenThrow(EntityNotFoundException.class);
        // Act
        mockUserService.createUser(mockUser);

        // Assert
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .save(mockUser);
    }


}
