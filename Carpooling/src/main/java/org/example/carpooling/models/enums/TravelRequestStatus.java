package org.example.carpooling.models.enums;

public enum TravelRequestStatus {
    Waiting,
    Accepted,
    Rejected;

    @Override
    public String toString() {
        return switch (this){
            case Waiting -> "Waiting";
            case Accepted -> "Accepted";
            case Rejected -> "Rejected";
        };
    }
}
