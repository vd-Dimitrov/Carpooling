package org.example.carpooling;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.carpooling.models.enums.TravelStatus;
import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.*;
import org.example.carpooling.services.interfaces.UserService;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Helpers {

    public static final double MOCK_RATING = 5.0;
    public static final int MOCK_ID = 1;
    public static final String TIME_FORMAT = "yyyy-MM-dd'T'HH:mm";
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(TIME_FORMAT);

    public static User createMockUser(){
        User mockUser = new User();
        mockUser.setUserId(MOCK_ID);
        mockUser.setUsername("MockUsername");
        mockUser.setPassword("MockPassword!1");
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
        mockUser.setPassword("MockPassword!1");
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
        mockFeedback.setReceiver(createMockUser());
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
        mockUserDto.setPassword("MockPassword!1");
        mockUserDto.setPasswordConfirm("MockPassword!1");
        mockUserDto.setFirstName("MockFirstName");
        mockUserDto.setLastName("MockLastName");
        mockUserDto.setEmail("MockEmail@mock.com");
        mockUserDto.setPhoneNumber("0123456789");

        return mockUserDto;
    }

    public static UserDtoUpdate createMockUserDtoUpdate(){
        UserDtoUpdate mockUserDto = new UserDtoUpdate();
        mockUserDto.setPassword("MockPassword!1");
        mockUserDto.setFirstName("MockFirstName");
        mockUserDto.setLastName("MockLastName");
        mockUserDto.setEmail("MockEmail@mock.com");
        mockUserDto.setPhoneNumber("0123456789");

        return mockUserDto;
    }

    public static Travel createMockTravel(){
        Travel mockTravel = new Travel();
        mockTravel.setTravelId(MOCK_ID);
        mockTravel.setTitle("Mock title");
        mockTravel.setStartingPoint("Mock starting point");
        mockTravel.setEndingPoint("Mock ending point");
        mockTravel.setDriver(createMockUser());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        mockTravel.setDepartureTime(parseTimestamp(now.format(formatter)));
        mockTravel.setFreeSpots(0);
        mockTravel.setTravelStatus(TravelStatus.Upcoming);

        return mockTravel;
    }

    public static TravelDtoOut createMockTravelDtoOut(){
        TravelDtoOut mockTravel = new TravelDtoOut();
        mockTravel.setTitle("Mock title");
        mockTravel.setStartingPoint("Mock starting point");
        mockTravel.setEndingPoint("Mock ending point");
        mockTravel.setDriverName(createMockUser().getUsername());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        mockTravel.setDepartureTime(parseTimestamp(now.format(formatter)).toString());
        mockTravel.setFreeSpots(0);
        mockTravel.setTravelStatus(TravelStatus.Upcoming.toString());

        return mockTravel;
    }

    public static TravelDtoIn createMockTravelDtoIn(){
        TravelDtoIn mockTravel = new TravelDtoIn();
        mockTravel.setTitle("Mock title");
        mockTravel.setStartingPoint("Mock starting point");
        mockTravel.setEndingPoint("Mock ending point");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        mockTravel.setDepartureTime(parseTimestamp(now.format(formatter)).toString());
        mockTravel.setFreeSpots(0);

        return mockTravel;
    }

    public static FeedbackDtoOut createMockFeedbackDtoOut(){
        FeedbackDtoOut mockFeedback = new FeedbackDtoOut();

        mockFeedback.setAuthor("MockUsername");
        mockFeedback.setReceiver("MockReceiver");
        mockFeedback.setRating(5.0);
        mockFeedback.setComment("Mock comment");

        return mockFeedback;
    }

    public static String toJson(final Object obj){
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Timestamp parseTimestamp(String time) {
        try {
            return new Timestamp(DATE_TIME_FORMAT.parse(time).getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
