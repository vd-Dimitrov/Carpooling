package org.example.carpooling.controllers;

import jakarta.validation.Valid;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityDuplicateException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.helpers.AuthenticationHelper;
import org.example.carpooling.helpers.ModelMapper;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.UserDtoIn;
import org.example.carpooling.models.dto.UserDtoOut;
import org.example.carpooling.models.dto.UserDtoUpdate;
import org.example.carpooling.services.interfaces.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;

    public UserRestController(UserService userService, ModelMapper modelMapper, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }
    @GetMapping("/{id}")
    public UserDtoOut getUserById(@RequestHeader HttpHeaders httpHeaders, @PathVariable int id){
        try {
            authenticationHelper.tryGetUser(httpHeaders);
            User user = userService.getById(id);

            return modelMapper.fromUserToUserDto(user);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping
    public List<UserDtoOut> getAllUsers(@RequestHeader HttpHeaders httpHeaders){
        try {
            authenticationHelper.tryGetUser(httpHeaders);
            List<User> users = userService.getAllUsers();

            return modelMapper.fromListUsersToListUserDto(users);
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
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

    @PutMapping("/{id}")
    public void updateUser(@RequestHeader HttpHeaders httpHeaders,
                           @PathVariable int id,
                           @Valid @RequestBody UserDtoUpdate userDtoUpdate){
        try{
            User authenticatedUser = authenticationHelper.tryGetUser(httpHeaders);
            User updatedUser = modelMapper.fromUserDtoUpdate(userDtoUpdate, id);

            userService.updateUser(updatedUser, authenticatedUser);
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@RequestHeader HttpHeaders httpHeaders, @PathVariable int id){
        try{
            User userToDelete = userService.getById(id);
            User currentUser = authenticationHelper.tryGetUser(httpHeaders);

            userService.deleteUser(userToDelete, currentUser);
        } catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
