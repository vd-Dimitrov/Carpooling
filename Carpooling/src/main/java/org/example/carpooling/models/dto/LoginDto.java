package org.example.carpooling.models.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class LoginDto {
    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 2, max = 20, message = "username should be between 2 and 20 characters")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min=8, message = "Password should be at least 8 characters")
    private String password;

    public LoginDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
