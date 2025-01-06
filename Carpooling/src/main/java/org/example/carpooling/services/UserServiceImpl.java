package org.example.carpooling.services;

import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityDuplicateException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.models.User;
import org.example.carpooling.repositories.UserRepository;
import org.example.carpooling.services.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    public static final String MODIFY_ERROR_MESSAGE = "Only owner can make changes to the User's information!";
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        boolean duplicateExists = true;
        try {
            getByUsername(user.getUsername());
        } catch (EntityNotFoundException e){
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        } else {
            return userRepository.save(user);
        }
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow( () -> new EntityNotFoundException("User", "username", username));
    }

    @Override
    public User getById(int id) {
        return userRepository.findUserByUserId(id)
                .orElseThrow( () -> new EntityNotFoundException("User", id));
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow( () -> new EntityNotFoundException("User", "email", email));
    }

    @Override
    public User getByPhoneNumber(String phoneNumber) {
        return userRepository.findUserByPhoneNumber(phoneNumber)
                .orElseThrow( () -> new EntityNotFoundException("User", "phone number", phoneNumber));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(User updatedUser, User requestingUser) {
        checkPermission(requestingUser, updatedUser);
        userRepository.save(updatedUser);
    }

    @Override
    public void deleteUser(User userToDelete, User requestingUser) {
        checkPermission(userToDelete, requestingUser);
        userRepository.delete(userToDelete);
    }

    private void checkPermission(User updatedUser, User requestingUser){
        if (requestingUser.getUserId()!=updatedUser.getUserId()){
            throw new AuthorizationException(MODIFY_ERROR_MESSAGE);
        }
    }


}
