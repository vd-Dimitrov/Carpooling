package org.example.carpooling.services.interfaces;

import org.example.carpooling.models.Travel;
import org.example.carpooling.models.TravelRequest;
import org.example.carpooling.models.User;

import java.util.List;

public interface TravelRequestService {
    TravelRequest createTravelRequest(User requestingUser, Travel requestedTravel);

    TravelRequest getRequestByRequestId(int requestId);
    List<TravelRequest> getAllTravelRequests();
    List<TravelRequest> getAllTravelRequestsForTravel(int travelId);
    List<TravelRequest> getAllTravelRequestsForUser(int userId);
    TravelRequest approveRequest(User driver, int requestId, int travelId);
    TravelRequest rejectRequest(User driver, int requestId, int travelId);
    void updateTravelRequest(TravelRequest updatedRequest, User requestingUser);
    void deleteTravelRequest(int travelRequestId, User requestingUser);
}
