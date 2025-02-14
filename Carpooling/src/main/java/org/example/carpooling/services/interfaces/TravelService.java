package org.example.carpooling.services.interfaces;

import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;

import java.util.List;

public interface TravelService {
    Travel createTravel(Travel travel);

    List<Travel> getAllTravels();
    List<Travel> searchTravels(String startingPoint,
                               String endingPoint,
                               String departureTime,
                               String travelStatus,
                               int freeSpots);
    List<Travel> getAllUpcomingTravels();
    Travel getById(int travelId);
    List<Travel> getByDriver(int driveId);

    void addFeedback(int userId, Feedback feedback);
    void updateTravel(Travel updatedTravel, User requestingUser);
    void deleteTravel(int travelId, User requestingUser );
}
