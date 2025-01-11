package org.example.carpooling.helpers;

import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {
    public User fromUserDto(UserDto userDto) {
        User user = new User();

        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());

        return user;
    }
}
