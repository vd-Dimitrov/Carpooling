package org.example.carpooling.services.interfaces;

import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;

import java.util.List;

public interface TravelService {
    Travel createTravel(Travel travel);

    Travel getById(int travelId);
    List<Travel> getByDriver(int driveId);

    void updateTravel(Travel updatedTravel, User requestingUser);
    void deleteTravel(Travel travelToDelete, User requestingUser );
}
