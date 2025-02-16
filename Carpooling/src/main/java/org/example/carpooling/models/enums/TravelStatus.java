package org.example.carpooling.models.enums;

public enum TravelStatus {
    Upcoming,
    Ongoing,
    Complete,
    Cancelled;

    @Override
    public String toString() {
        return switch (this){
            case Upcoming -> "Upcoming";
            case Ongoing -> "Ongoing";
            case Complete -> "Complete";
            case Cancelled -> "Cancelled";
        };
    }
}
