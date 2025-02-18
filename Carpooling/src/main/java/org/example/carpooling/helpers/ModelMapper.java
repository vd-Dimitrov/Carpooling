package org.example.carpooling.helpers;

import org.example.carpooling.models.enums.TravelStatus;
import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.*;
import org.example.carpooling.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ModelMapper {
    public static final String TIME_FORMAT = "yyyy-MM-dd'T'HH:mm";
    private final UserService userService;
    private final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(TIME_FORMAT);

    @Autowired
    public ModelMapper(UserService userService) {
        this.userService = userService;
    }

    public User fromUserDto(UserDtoIn userDtoIn) {
        User user = new User();

        user.setUsername(userDtoIn.getUsername());
        user.setPassword(userDtoIn.getPassword());
        user.setFirstName(userDtoIn.getFirstName());
        user.setLastName(userDtoIn.getLastName());
        user.setEmail(userDtoIn.getEmail());
        user.setPhoneNumber(userDtoIn.getPhoneNumber());
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        user.setCreateAt(parseTimestamp(now.format(formatter)));

        return user;
    }

    public User fromUserDtoUpdate(UserDtoUpdate userDtoUpdate, int id){
        User user = new User();
        user.setUserId(id);
        user.setUsername(userService.getById(id).getUsername());
        user.setPassword(userDtoUpdate.getPassword());
        user.setFirstName(userDtoUpdate.getFirstName());
        user.setLastName(userDtoUpdate.getLastName());
        user.setEmail(userDtoUpdate.getEmail());
        user.setPhoneNumber(userDtoUpdate.getPhoneNumber());
        user.setUserRating(userService.getById(id).getUserRating());
        user.setTravels(userService.getById(id).getTravels());
        user.setAdmin(userService.getById(id).isAdmin());
        user.setCreateAt(userService.getById(id).getCreateAt());

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

    public UserDtoUpdateOut fromUserToUserDtoUpdateOut(User user){
        UserDtoUpdateOut userDtoUpdateOut = new UserDtoUpdateOut();

        userDtoUpdateOut.setUserId(user.getUserId());
        userDtoUpdateOut.setPassword(user.getPassword());
        userDtoUpdateOut.setFirstName(user.getFirstName());
        userDtoUpdateOut.setLastName(user.getLastName());
        userDtoUpdateOut.setEmail(user.getEmail());
        userDtoUpdateOut.setPhoneNumber(user.getPhoneNumber());

        return userDtoUpdateOut;
    }

    public List<UserDtoOut> fromListUsersToListUserDto(List<User> users){
        if (users == null){
            return new ArrayList<>();
        }

        return users.stream()
                .map(this::fromUserToUserDto)
                .collect(Collectors.toList());
    }

    public List<UserDtoOut> fromSetUsersToListUserDtoOut(Set<User> users){
        if (users == null){
            return new ArrayList<>();
        }

        return users.stream().map(this::fromUserToUserDto)
                .collect(Collectors.toList());
    }

    public TravelDtoOut fromTravelToTravelDtoOut(Travel travel){
        TravelDtoOut travelDtoOut = new TravelDtoOut();

        travelDtoOut.setTitle(travel.getTitle());
        travelDtoOut.setStartingPoint(travel.getStartingPoint());
        travelDtoOut.setEndingPoint(travel.getEndingPoint());
        travelDtoOut.setDriverName(travel.getDriver().getUsername());
        travelDtoOut.setDepartureTime(travel.getDepartureTime().toString());
        travelDtoOut.setFreeSpots(travel.getFreeSpots());
        travelDtoOut.setPassengers(fromSetUsersToListUserDtoOut(travel.getPassengers()));

        return travelDtoOut;
    }

    public Travel fromTravelDtoInToTravel(TravelDtoIn travelDto, User user){
        Travel travel = new Travel();
        travel.setTitle(travelDto.getTitle());
        travel.setStartingPoint(travelDto.getStartingPoint());
        travel.setEndingPoint(travelDto.getEndingPoint());
        travel.setDepartureTime(parseTimestamp(travelDto.getDepartureTime()));
        travel.setDriver(user);
        travel.setFreeSpots(travelDto.getFreeSpots());
        travel.setTravelStatus(TravelStatus.Upcoming);

        return travel;
    }

    public Travel fromTravelDtoUpdateToTravel(TravelDtoUpdate travelDto, User user, int id){
        Travel travel = new Travel();

        travel.setTravelId(id);
        travel.setDriver(user);
        travel.setTitle(travelDto.getTitle());
        travel.setDepartureTime(Timestamp.valueOf(travelDto.getDepartureTime()));
        travel.setStartingPoint(travelDto.getStartingPoint());
        travel.setEndingPoint(travelDto.getEndingPoint());
        travel.setFreeSpots(travelDto.getFreeSpots());
        travel.setTravelStatus(TravelStatus.valueOf(travelDto.getDepartureTime().toUpperCase()));

        return travel;
    }

    public List<TravelDtoOut> fromListTravelsToListTravelDtoOut(List<Travel> travels){
        if (travels == null){
            return new ArrayList<>();
        }

        return travels.stream().map(this::fromTravelToTravelDtoOut)
                    .collect(Collectors.toList());
    }

    public FeedbackDtoOut fromFeedbackToFeedbackDtoOut(Feedback feedback){
        FeedbackDtoOut feedbackDtoOut = new FeedbackDtoOut();

        feedbackDtoOut.setRating(feedback.getRating());
        feedbackDtoOut.setComment(feedback.getComment());
        feedbackDtoOut.setAuthor(feedback.getAuthor().getUsername());
        feedbackDtoOut.setReceiver(feedback.getReceiver().getUsername());
        return feedbackDtoOut;
    }

    public List<FeedbackDtoOut> fromListFeedbackToListFeedbackDtoOut(List<Feedback> feedbacks){
        if (feedbacks == null){
            return new ArrayList<>();
        }

        return feedbacks.stream().map(this::fromFeedbackToFeedbackDtoOut)
                .collect(Collectors.toList());
    }

    public Feedback fromFeedbackDtoInToFeedback(FeedbackDtoIn feedbackDtoIn, User feedbackAuthor, User feedbackReceiver) {
        Feedback feedback = new Feedback();

        feedback.setAuthor(feedbackAuthor);
        feedback.setRating(feedbackDtoIn.getRating());
        if (!feedbackDtoIn.getComment().isEmpty()){
            feedback.setComment(feedbackDtoIn.getComment());
        }
        feedback.setReceiver(feedbackReceiver);

        return feedback;
    }

    private Timestamp parseTimestamp(String time) {
        try {
            return new Timestamp(DATE_TIME_FORMAT.parse(time).getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
