package org.example.carpooling.services;

import org.example.carpooling.enums.TravelStatus;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.repositories.TravelRepository;
import org.example.carpooling.services.interfaces.TravelService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelServiceImpl implements TravelService {
    public static final String MODIFY_ERROR_MESSAGE = "Only author can make changes to the travel information!";
    private final TravelRepository travelRepository;

    public TravelServiceImpl(TravelRepository travelRepository) {
        this.travelRepository = travelRepository;
    }

    @Override
    public Travel createTravel(Travel travel) {
        return travelRepository.save(travel);
    }

    @Override
    public List<Travel> getAllTravels() {
        return travelRepository.findAll();
    }

    @Override
    public List<Travel> getAllUpcomingTravels() {
        return travelRepository.findTravelByTravelStatus(TravelStatus.UPCOMING);
    }


    @Override
    public Travel getById(int travelId) {
        return travelRepository.findTravelByTravelId(travelId)
                .orElseThrow( () -> new EntityNotFoundException("Travel", travelId));
    }

    @Override
    public List<Travel> getByDriver(int driveId) {
        return travelRepository.findAllByDriverUserId(driveId)
                .orElseThrow( () -> new EntityNotFoundException("Driver", driveId));
    }

    @Override
    public void updateTravel(Travel updatedTravel, User requestingUser) {
        checkPermission(updatedTravel, requestingUser);
        travelRepository.save(updatedTravel);
    }

    @Override
    public void deleteTravel(Travel travelToDelete, User requestingUser) {
        checkPermission(travelToDelete, requestingUser);
        travelRepository.delete(travelToDelete);
    }
    
    private void checkPermission(Travel updatedTravel, User requestingUser){
        if (requestingUser.getUserId()!=updatedTravel.getDriver().getUserId()){
            throw new AuthorizationException(MODIFY_ERROR_MESSAGE);
        }
    }
}
