package org.example.carpooling.models.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDto {

    @NotNull(message = "Username cannot be empty")
    @Size(min = 2, max = 20, message = "Username should be between 2 and 20 characters")
    private String username;

    @NotNull(message = "Password cannot be empty")
    @Size(min = 8, message = "Password should have at least 8 characters")
    private String password;

    @NotNull(message = "First name cannot be empty")
    @Size(min = 2, max = 20, message = "First name should be between 2 and 20 characters")
    private String firstName;

    @NotNull(message = "Last name cannot be empty")
    @Size(min = 2, max = 20, message = "Last name should be between 2 and 20 characters")
    private String lastName;

    @NotNull(message = "Email cannot be empty")
    private String email;

    @NotNull(message = "Phone number cannot be empty")
    @Size(min = 10, max = 10, message = "A valid phone number is 10 digits")
    private String phoneNumber;

    public void userDto(){

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
