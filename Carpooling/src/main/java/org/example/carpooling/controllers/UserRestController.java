package org.example.carpooling.controllers;

import jakarta.validation.Valid;
import org.example.carpooling.exceptions.EntityDuplicateException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.helpers.ModelMapper;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.UserDtoIn;
import org.example.carpooling.models.dto.UserDtoOut;
import org.example.carpooling.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserRestController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserRestController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }
    @GetMapping("/{id}")
    public UserDtoOut getUserById( @PathVariable int id){
        try {
            User user = userService.getById(id);

            return modelMapper.fromUserToUserDto(user);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public List<UserDtoOut> getAllUsers(){
        List<User> users = userService.getAllUsers();

        return modelMapper.fromListUsersToListUserDto(users);
    }

    @PostMapping("/register")
    public void registerUser(@Valid @RequestBody UserDtoIn userDtoIn) {
        try {
            User user = modelMapper.fromUserDto(userDtoIn);
            userService.createUser(user);
        } catch (EntityDuplicateException e ) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
