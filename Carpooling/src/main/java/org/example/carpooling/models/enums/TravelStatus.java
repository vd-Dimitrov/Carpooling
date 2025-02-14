package org.example.carpooling.models.enums;

public enum TravelStatus {
    UPCOMING,
    ONGOING,
    COMPLETE,
    CANCELLED;

    @Override
    public String toString() {
        return switch (this){
            case UPCOMING -> "Upcoming";
            case ONGOING -> "Ongoing";
            case COMPLETE -> "Complete";
            case CANCELLED -> "Cancelled";
        };
    }
}
