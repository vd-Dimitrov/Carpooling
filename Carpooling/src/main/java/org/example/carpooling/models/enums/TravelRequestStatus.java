package org.example.carpooling.models.enums;

public enum TravelRequestStatus {
    WAITING,
    ACCEPTED,
    REJECTED;

    @Override
    public String toString() {
        return switch (this){
            case WAITING -> "Waiting";
            case ACCEPTED -> "Accepted";
            case REJECTED -> "Rejected";
        };
    }
}
