package org.example.carpooling.services.interfaces;

import org.example.carpooling.models.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User getByUsername(String username);

    User getById(int id);

    User getByEmail(String email);

    User getByPhoneNumber(String phoneNumber);

    User getByTravelId(int travelId);

    List<User> getAllUsers();

    void updateUser(User updatedUser, User requestingUser);

    void deleteUser(int userId, User requestingUser);

    User searchUsers(String username, String email, String phoneNumber);
}
