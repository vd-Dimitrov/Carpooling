package org.example.carpooling.servicesTests;

import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityDuplicateException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.models.Feedback;
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

import java.util.ArrayList;
import java.util.List;
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

    @Test
    public void createUser_Should_Throw_When_UserWithSameUsernameExist(){
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockUserRepository.findUserByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        // Act, Assert
        Assertions.assertThrows(EntityDuplicateException.class,
                () -> mockUserService.createUser(mockUser));
    }

    @Test
    public void createUser_Should_Throw_When_UserWithSameEmailExists(){
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockUserRepository.findUserByEmail(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        // Act, Assert
        Assertions.assertThrows(EntityDuplicateException.class,
                () -> mockUserService.createUser(mockUser));

    }

    @Test
    public void createUser_Should_Throw_When_UserWithSamePhoneNumberExists(){
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockUserRepository.findUserByPhoneNumber(Mockito.anyString()))
                .thenReturn(Optional.of(mockUser));

        // Act, Assert
        Assertions.assertThrows(EntityDuplicateException.class,
                () -> mockUserService.createUser(mockUser));

    }

    @Test
    public void createUser_Should_Throw_When_EmailIsInvalid(){
        // Arrange
        User mockUser = createMockUserInvalidEmail();

        // Act, Assert
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> mockUserService.createUser(mockUser));
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
    public void getAllUsers_Should_CallRepository(){
        // Arrange
        List<User> mockUserList = new ArrayList<>();

        Mockito.when(mockUserRepository.findAll())
                .thenReturn(mockUserList);

        //Act
        List<User> result = mockUserService.getAllUsers();

        // Assert
        Assertions.assertEquals(mockUserList, result);
    }
    @Test
    public void getById_Should_ReturnUser_When_IdExists(){
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockUserRepository.findUserByUserId(Mockito.anyInt()))
                .thenReturn(Optional.of(mockUser));

        // Act
        User result = mockUserService.getById(Mockito.anyInt());

        // Assert
        Assertions.assertEquals(mockUser, result);
    }

    @Test
    public void updateUser_Should_CallRepository_When_UserIsOwner(){
        // Arrange
        User mockUser = createMockUser();

        // Act
        mockUserService.updateUser(mockUser, mockUser);

        // Assert
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .save(mockUser);
    }

    @Test
    public void updateUser_Should_Throw_When_UserIsNotOwner(){
        // Arrange
        User mockUser = createMockUser();
        User mockInvalidUser = createMockUser();
        mockInvalidUser.setUserId(2);

        // Act, Assert
        Assertions.assertThrows(AuthorizationException.class,
                () -> mockUserService.updateUser(mockUser, mockInvalidUser));
    }

    @Test
    public void deleteUser_Should_CallRepository_When_UserIsOwner(){
        // Arrange
        User mockUser = createMockUser();

        Mockito.when(mockUserRepository.findUserByUserId(Mockito.anyInt()))
                .thenReturn(Optional.of(mockUser));

        // Act
        mockUserService.deleteUser(1, mockUser);

        // Assert
        Mockito.verify(mockUserRepository, Mockito.times(1))
                .delete(mockUser);
    }

    @Test
    public void deleteUser_Should_Throw_When_UserIsNotOwner(){
        // Arrange
        User mockUser = createMockUser();
        Mockito.when(mockUserRepository.findUserByUserId(Mockito.anyInt()))
                .thenReturn(Optional.of(mockUser));

        User mockInvalidUser = Mockito.mock(User.class);

        // Act, Assert
        Assertions.assertThrows(AuthorizationException.class,
                () -> mockUserService.deleteUser(1, mockInvalidUser));
    }

}
