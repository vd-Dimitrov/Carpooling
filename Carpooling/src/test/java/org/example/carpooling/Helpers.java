package org.example.carpooling;

import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.User;

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
}
