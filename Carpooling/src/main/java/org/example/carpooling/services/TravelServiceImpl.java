package org.example.carpooling.services;

import org.example.carpooling.models.enums.TravelStatus;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.repositories.TravelRepository;
import org.example.carpooling.services.interfaces.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class TravelServiceImpl implements TravelService {
    public static final String MODIFY_ERROR_MESSAGE = "Only author can make changes to the travel information!";
    private final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final TravelRepository travelRepository;

    @Autowired
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
    public List<Travel> searchTravels(String title, String startingPoint, String endingPoint, String departureTime, int freeSpots, String createdAt) {
        return travelRepository.searchTravels(title, startingPoint, endingPoint, parseTimestamp(departureTime), freeSpots, parseTimestamp(createdAt));
    }

    @Override
    public Page<Travel> searchTravelsPaginated(String title, String startingPoint, String endingPoint, Timestamp departureTime, int freeSpots, Timestamp createdAt, PageRequest pageRequest) {
        return travelRepository.searchTravelsPaginated(title, startingPoint, endingPoint, departureTime, freeSpots, createdAt, pageRequest);
    }

    @Override
    public List<Travel> getAllUpcomingTravels() {
        return travelRepository.findTravelByTravelStatus(TravelStatus.Upcoming);
    }


    @Override
    public Travel getById(int travelId) {
        return travelRepository.findTravelByTravelId(travelId)
                .orElseThrow(() -> new EntityNotFoundException("Travel", travelId));
    }

    @Override
    public List<Travel> getByDriver(int driveId) {
        return travelRepository.findAllByDriverUserId(driveId)
                .orElseThrow(() -> new EntityNotFoundException("Driver", driveId));
    }

    @Override
    public void updateTravel(Travel updatedTravel, User requestingUser) {
        checkPermission(updatedTravel, requestingUser);
        travelRepository.save(updatedTravel);
    }

    @Override
    public void changeTravelStatusToFinished(Travel travel, User requestingUser) {
        checkPermission(travel, requestingUser);
        travel.setTravelStatus(TravelStatus.Complete);
        travelRepository.save(travel);
    }

    @Override
    public void changeTravelStatusToCancelled(Travel travel, User requestingUser) {
        checkPermission(travel, requestingUser);
        travel.setTravelStatus(TravelStatus.Cancelled);
        travelRepository.save(travel);
    }

    @Override
    public void deleteTravel(int id, User requestingUser) {
        Travel travelToDelete = getById(id);
        checkPermission(travelToDelete, requestingUser);
        travelRepository.delete(travelToDelete);
    }

    private void checkPermission(Travel updatedTravel, User requestingUser) {
        if (!requestingUser.equals(updatedTravel.getDriver())) {
            throw new AuthorizationException(MODIFY_ERROR_MESSAGE);
        }
    }

    private Timestamp parseTimestamp(String time) {
        try {
            return new Timestamp(DATE_TIME_FORMAT.parse(time).getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
