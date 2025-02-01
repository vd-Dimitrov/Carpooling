package org.example.carpooling.models.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TravelDtoOut {
    private String startingPoint;
    private String endingPoint;
    private String driverName;
    private String departureTime;
    private int freeSpots;
    private String travelStatus;
    private List<UserDtoOut> passengers;

    public TravelDtoOut() {
    }
    public String getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(String startingPoint) {
        this.startingPoint = startingPoint;
    }

    public String getEndingPoint() {
        return endingPoint;
    }

    public void setEndingPoint(String endingPoint) {
        this.endingPoint = endingPoint;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public int getFreeSpots() {
        return freeSpots;
    }

    public void setFreeSpots(int freeSpots) {
        this.freeSpots = freeSpots;
    }

    public String getTravelStatus() {
        return travelStatus;
    }

    public void setTravelStatus(String travelStatus) {
        this.travelStatus = travelStatus;
    }

    public List<UserDtoOut> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<UserDtoOut> passengers) {
        this.passengers = passengers;
    }
}
