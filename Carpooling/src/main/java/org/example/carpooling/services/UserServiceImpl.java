package org.example.carpooling.services;

import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityDuplicateException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.models.User;
import org.example.carpooling.repositories.UserRepository;
import org.example.carpooling.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {
    public static final String MODIFY_ERROR_MESSAGE = "Only owner can make changes to the User's information!";
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        checkUniqueUser(user);
        checkValidEmailPattern(user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User", "username", username));
    }

    @Override
    public User getById(int id) {
        return userRepository.findUserByUserId(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id));
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User", "email", email));
    }

    @Override
    public User getByPhoneNumber(String phoneNumber) {
        return userRepository.findUserByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new EntityNotFoundException("User", "phone number", phoneNumber));
    }

    @Override
    public User getByTravelId(int travelId) {
        return userRepository.findUserByTravelId(travelId)
                .orElseThrow(() -> new EntityNotFoundException("Travel", travelId));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(User updatedUser, User requestingUser) {
        checkPermission(requestingUser, updatedUser);
        checkUniqueUser(updatedUser);
        checkValidEmailPattern(updatedUser.getEmail());
        userRepository.save(updatedUser);
    }

    @Override
    public void deleteUser(User userToDelete, User requestingUser) {
        checkPermission(userToDelete, requestingUser);
        userRepository.delete(userToDelete);
    }

    @Override
    public User searchUsers(String username, String email, String phoneNumber) {
        return userRepository.searchUsers(username, email, phoneNumber);
    }

    private void checkPermission(User updatedUser, User requestingUser) {
        if (requestingUser.getUserId() != updatedUser.getUserId()) {
            throw new AuthorizationException(MODIFY_ERROR_MESSAGE);
        }
    }

    private void checkUniqueUser(User user) {
        boolean duplicateUsernameExists = true;
        boolean duplicateEmailExists = true;
        boolean duplicatePhoneExists = true;
        try {
            getByUsername(user.getUsername());
        } catch (EntityNotFoundException e) {
            duplicateUsernameExists = false;
        }
        try {
            getByEmail(user.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateEmailExists = false;
        }
        try {
            getByPhoneNumber(user.getPhoneNumber());
        } catch (EntityNotFoundException e) {
            duplicatePhoneExists = false;
        }
        if (duplicateUsernameExists) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        } else if (duplicateEmailExists) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        } else if (duplicatePhoneExists) {
            throw new EntityDuplicateException("User", "phone number", user.getPhoneNumber());
        }
    }

    public void checkValidEmailPattern(String emailAddress) {
        String REGEX_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        if (!Pattern.compile(REGEX_PATTERN)
                .matcher(emailAddress)
                .matches()) {
            throw new IllegalArgumentException("Enter valid email address");
        }
    }

}
