package org.example.carpooling.models.dto;

import jakarta.validation.constraints.NotEmpty;

public class TravelDtoIn {
    @NotEmpty
    private String title;
    @NotEmpty
    private String startingPoint;
    @NotEmpty
    private String endingPoint;
    @NotEmpty
    private String departureTime;
    private int freeSpots;

    public TravelDtoIn() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
