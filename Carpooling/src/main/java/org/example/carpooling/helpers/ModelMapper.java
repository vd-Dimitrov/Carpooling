package org.example.carpooling.helpers;

import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.UserDtoIn;
import org.example.carpooling.models.dto.UserDtoOut;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ModelMapper {
    public User fromUserDto(UserDtoIn userDtoIn) {
        User user = new User();

        user.setUsername(userDtoIn.getUsername());
        user.setPassword(userDtoIn.getPassword());
        user.setFirstName(userDtoIn.getFirstName());
        user.setLastName(userDtoIn.getLastName());
        user.setEmail(userDtoIn.getEmail());
        user.setPhoneNumber(userDtoIn.getPhoneNumber());

        return user;
    }


    public UserDtoOut fromUserToUserDto(User user){
        UserDtoOut userDtoOut = new UserDtoOut();

        userDtoOut.setUsername(user.getUsername());
        userDtoOut.setFirstName(user.getFirstName());
        userDtoOut.setLastName(user.getLastName());
        userDtoOut.setEmail(user.getEmail());
        userDtoOut.setPhoneNumber(user.getPhoneNumber());

        return userDtoOut;
    }

    public List<UserDtoOut> fromListUsersToListUserDto(List<User> users){
        if (users == null){
            return new ArrayList<>();
        }

        return users.stream()
                .map(this::fromUserToUserDto)
                .collect(Collectors.toList());
    }
}
