package org.example.carpooling.services;

import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.exceptions.IllegalArgumentException;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.TravelRequest;
import org.example.carpooling.models.User;
import org.example.carpooling.models.enums.TravelRequestStatus;
import org.example.carpooling.models.enums.TravelStatus;
import org.example.carpooling.repositories.TravelRepository;
import org.example.carpooling.repositories.TravelRequestRepository;
import org.example.carpooling.services.interfaces.TravelRequestService;
import org.example.carpooling.services.interfaces.TravelService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelRequestServiceImpl implements TravelRequestService {
    public static final String MODIFY_ERROR_MESSAGE = "Only owner can make changes to the travel request!";
    public static final String TRAVEL_REQUEST_ERROR_MESSAGE = "Only the driver may address requests.";
    public static final String NO_FREE_SPOTS_ERROR = "No free spots left";

    private final TravelRequestRepository requestRepository;
    private final TravelRepository travelRepository;
    private final TravelService travelService;

    public TravelRequestServiceImpl(TravelRequestRepository requestRepository, TravelRepository travelRepository, TravelRepository travelRepository1, TravelService travelService) {
        this.requestRepository = requestRepository;
        this.travelRepository = travelRepository1;
        this.travelService = travelService;
    }

    @Override
    public TravelRequest createTravelRequest(User requestingUser, Travel appliedTravel) {
        if (!appliedTravel.getTravelStatus().equals(TravelStatus.UPCOMING)) {
            throw new IllegalArgumentException("Too late to apply for this travel.");
        }

        if (appliedTravel.getPassengers().contains(requestingUser)){
            throw new IllegalArgumentException("You are already accepted for the travel!");
        }
        TravelRequest request = new TravelRequest();
        request.setApplicant(requestingUser);
        request.setAppliedTravel(appliedTravel);
        request.setRequestStatus(TravelRequestStatus.WAITING);
        return requestRepository.save(request);
    }

    @Override
    public TravelRequest getRequestByRequestId(int requestId) {
        return requestRepository.findTravelRequestByRequestId(requestId)
                .orElseThrow( () -> new EntityNotFoundException("request", requestId));
    }

    @Override
    public List<TravelRequest> getAllTravelRequests() {
        return requestRepository.findAll();
    }

    @Override
    public List<TravelRequest> getAllTravelRequestsForTravel(int travelId) {
        return requestRepository.findTravelRequestsByAppliedTravelTravelId(travelId)
                .orElseThrow( () -> new EntityNotFoundException("Travel", travelId));
    }

    @Override
    public List<TravelRequest> getAllTravelRequestsForUser(int userId) {
        return requestRepository.findTravelRequestsByApplicantUserId(userId)
                .orElseThrow( () -> new EntityNotFoundException("User", userId));
    }

    @Override
    public TravelRequest approveRequest(User driver, int travelId, int requestId) {
        TravelRequest travelRequest = getRequestByRequestId(requestId);
        checkRequestManagementPermission(driver, travelRequest, travelId);

        Travel travel = travelService.getById(travelId);
        if (travel.getFreeSpots() == 0){
            throw new IllegalArgumentException(NO_FREE_SPOTS_ERROR);
        }

        travelRequest.setRequestStatus(TravelRequestStatus.ACCEPTED);
        travel.getPassengers().add(travelRequest.getApplicant());
        travel.setFreeSpots(travel.getFreeSpots()-1);

        travelRepository.save(travel);
        return requestRepository.save(travelRequest);
    }

    @Override
    public TravelRequest rejectRequest(User driver, int travelId, int requestId) {
        TravelRequest travelRequest = getRequestByRequestId(requestId);
        checkRequestManagementPermission(driver, travelRequest, travelId);
        Travel travel = travelService.getById(travelId);

        travelRequest.setRequestStatus(TravelRequestStatus.REJECTED);


        travel.setFreeSpots(travel.getFreeSpots()+1);
        travel.getPassengers().remove(travelRequest.getApplicant());
        travelRepository.save(travel);

        return requestRepository.save(travelRequest);
    }

    @Override
    public void updateTravelRequest(TravelRequest updatedRequest, User requestingUser) {
        checkPermission(updatedRequest, requestingUser);
        requestRepository.save(updatedRequest);
    }

    @Override
    public void deleteTravelRequest(int travelRequestId, User requestingUser) {
        TravelRequest requestToDelete = getRequestByRequestId(travelRequestId);
        checkPermission(requestToDelete, requestingUser);
        requestRepository.delete(requestToDelete);
    }

    private void checkPermission(TravelRequest travelRequest, User requestingUser){
        if (requestingUser.getUserId()!=travelRequest.getApplicant().getUserId()){
            throw new AuthorizationException(MODIFY_ERROR_MESSAGE);
        }
    }

    private void checkRequestManagementPermission(User requestingUser, TravelRequest travelRequest, int travelId){
        if (travelRequest.getAppliedTravel().getTravelId() != travelId){
            throw new IllegalArgumentException("Invalid request");
        }

        if (!travelRequest.getAppliedTravel().getDriver().equals(requestingUser)){
            throw new AuthorizationException(TRAVEL_REQUEST_ERROR_MESSAGE);
        }
    }


}
