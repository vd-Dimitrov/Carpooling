package org.example.carpooling.models.enums;

public enum TravelStatus {
    Upcoming,
    Complete,
    Cancelled;

    @Override
    public String toString() {
        return switch (this) {
            case Upcoming -> "Upcoming";
            case Complete -> "Complete";
            case Cancelled -> "Cancelled";
        };
    }
}
