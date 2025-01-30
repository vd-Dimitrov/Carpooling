package org.example.carpooling;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.carpooling.enums.TravelStatus;
import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.TravelDtoOut;
import org.example.carpooling.models.dto.UserDtoIn;
import org.example.carpooling.models.dto.UserDtoOut;
import org.example.carpooling.models.dto.UserDtoUpdate;

import java.time.LocalDateTime;


public class Helpers {

    public static final double MOCK_RATING = 5.0;
    public static final int MOCK_ID = 1;

    public static User createMockUser(){
        User mockUser = new User();
        mockUser.setUserId(MOCK_ID);
        mockUser.setUsername("MockUsername");
        mockUser.setPassword("MockPassword");
        mockUser.setFirstName("MockFirstName");
        mockUser.setLastName("MockLastName");
        mockUser.setPhoneNumber("0123456789");
        mockUser.setEmail("MockEmail@mock.com");

        return mockUser;
    }

    public static User createMockUserInvalidEmail(){
        User mockUser = new User();
        mockUser.setUserId(MOCK_ID);
        mockUser.setUsername("MockUsername");
        mockUser.setPassword("MockPassword");
        mockUser.setFirstName("MockFirstName");
        mockUser.setLastName("MockLastName");
        mockUser.setPhoneNumber("0123456789");
        mockUser.setEmail("InvalidMockEmail");

        return mockUser;
    }

    public static Feedback createMockFeedback(){
        Feedback mockFeedback = new Feedback();
        mockFeedback.setFeedbackId(MOCK_ID);
        mockFeedback.setAuthor(createMockUser());
        mockFeedback.setRating(MOCK_RATING);
        mockFeedback.setComment("Mock Comment");

        return mockFeedback;
    }

    public static UserDtoOut createMockUserOutDto(){
        UserDtoOut mockUser = new UserDtoOut();
        mockUser.setUsername("MockUsernameD");
        mockUser.setFirstName("MockFirstName");
        mockUser.setLastName("MockLastName");
        mockUser.setPhoneNumber("0123456789");
        mockUser.setEmail("MockEmail@mock.com");

        return mockUser;
    }

    public static UserDtoIn createMockUserDtoIn(){
        UserDtoIn mockUserDto = new UserDtoIn();
        mockUserDto.setUsername("MockUsername");
        mockUserDto.setPassword("MockPassword");
        mockUserDto.setFirstName("MockFirstName");
        mockUserDto.setLastName("MockLastName");
        mockUserDto.setEmail("MockEmail@mock.com");
        mockUserDto.setPhoneNumber("0123456789");

        return mockUserDto;
    }

    public static UserDtoUpdate createMockUserDtoUpdate(){
        UserDtoUpdate mockUserDto = new UserDtoUpdate();
        mockUserDto.setPassword("MockPassword");
        mockUserDto.setFirstName("MockFirstName");
        mockUserDto.setLastName("MockLastName");
        mockUserDto.setEmail("MockEmail@mock.com");
        mockUserDto.setPhoneNumber("0123456789");

        return mockUserDto;
    }

    public static Travel createMockTravel(){
        Travel mockTravel = new Travel();
        mockTravel.setTravelId(MOCK_ID);
        mockTravel.setStartingPoint("Mock starting point");
        mockTravel.setEndingPoint("Mock ending point");
        mockTravel.setDriver(createMockUser());
        mockTravel.setDepartureTime(LocalDateTime.now());
        mockTravel.setFreeSpots(0);
        mockTravel.setTravelStatus(TravelStatus.UPCOMING);

        return mockTravel;
    }

    public static TravelDtoOut createMockTravelDtoOut(){
        TravelDtoOut mockTravel = new TravelDtoOut();

        mockTravel.setStartingPoint("Mock starting point");
        mockTravel.setEndingPoint("Mock ending point");
        mockTravel.setDriverName(createMockUser().getUsername());
        mockTravel.setDepartureTime(LocalDateTime.now());
        mockTravel.setFreeSpots(0);
        mockTravel.setTravelStatus(TravelStatus.UPCOMING.toString());

        return mockTravel;
    }

    public static String toJson(final Object obj){
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
