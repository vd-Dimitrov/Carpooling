package org.example.carpooling.services.interfaces;

import org.example.carpooling.models.Feedback;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface TravelService {
    Travel createTravel(Travel travel);

    List<Travel> getAllTravels();
    List<Travel> searchTravels(String startingPoint,
                               String endingPoint,
                               String departureTime,
                               String travelStatus,
                               int freeSpots);
    Page<Travel> searchTravelsPaginated(String startingPoint,
                                        String endingPoint,
                                        String departureTime,
                                        String travelStatus,
                                        int freeSpots,
                                        PageRequest pageRequest);
    List<Travel> getAllUpcomingTravels();
    Travel getById(int travelId);
    List<Travel> getByDriver(int driveId);

    void updateTravel(Travel updatedTravel, User requestingUser);
    void deleteTravel(int travelId, User requestingUser );
}
