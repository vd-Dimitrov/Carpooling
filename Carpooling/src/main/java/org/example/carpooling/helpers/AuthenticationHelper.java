package org.example.carpooling.helpers;

import jakarta.servlet.http.HttpSession;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.models.User;
import org.example.carpooling.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String INVALID_AUTHENTICATION_ERROR = "Incorrect password or username";
    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers){
        if (!headers.containsKey(AUTHORIZATION_HEADER)) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        try {
            String userInfo = headers.getFirst(AUTHORIZATION_HEADER);
            String username = getUsername(userInfo);
            String password = getPassword(userInfo);
            User user = userService.getByUsername(username);
            if (!user.getPassword().equals(password)){
                throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
            }
            return user;
        } catch (EntityNotFoundException e){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
    }

    public User verifyUser(String username, String password){
        try{
            User user = userService.getByUsername(username);
            if(!user.getPassword().equals(password)){
                throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
            }
            return user;
        } catch (EntityNotFoundException e){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
    }

    public User tryGetCurrentUser(HttpSession session){
        String currentUsername = session.getAttribute("currentUser").toString();
        if (currentUsername == null){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        return userService.getByUsername(currentUsername);
    }


    private String getUsername(String userInformation) {
        int firstSpace = userInformation.indexOf(" ");
        if (firstSpace == -1){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        return userInformation.substring(0, firstSpace);
    }

    private String getPassword(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == -1){
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        return userInfo.substring(firstSpace + 1);
    }
}
