package org.example.carpooling.models;

import jakarta.persistence.*;
import org.example.carpooling.enums.TravelStatus;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "travels")
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id")
    private int travelId;

    @Column(name = "starting_point", nullable = false)
    private String startingPoint;

    @Column(name = "ending_point", nullable = false)
    private String endingPoint;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "free_spots", nullable = false)
    private int freeSpots;

    @Enumerated(EnumType.STRING)
    @Column(name = "travel_status")
    private TravelStatus travelStatus;

    // ToDo make a JoinTable for user_passenger logging
    @OneToMany
    @JoinColumn(name = "user_id")
    private Set<User> passengers = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "travels_options",
            joinColumns = @JoinColumn(name = "travels_id"),
            inverseJoinColumns = @JoinColumn(name = "options_id"))
    private Set<Option> options = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "feedbacks_travels",
            joinColumns = @JoinColumn(name = "travels_id"),
            inverseJoinColumns = @JoinColumn(name = "feedbacks_id"))
    private Set<Feedback> feedbacks = new HashSet<>();

    public int getTravelId() {
        return travelId;
    }

    public void setTravelId(int travelId) {
        this.travelId = travelId;
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

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public int getFreeSpots() {
        return freeSpots;
    }

    public void setFreeSpots(int freeSpots) {
        this.freeSpots = freeSpots;
    }

    public TravelStatus getTravelStatus() {
        return travelStatus;
    }

    public void setTravelStatus(TravelStatus travelStatus) {
        this.travelStatus = travelStatus;
    }

    public Set<User> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<User> passengers) {
        this.passengers = passengers;
    }

    public Set<Option> getOptions() {
        return options;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    public Set<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(Set<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }
}
