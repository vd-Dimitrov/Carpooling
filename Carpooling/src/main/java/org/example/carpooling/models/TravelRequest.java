package org.example.carpooling.models;

import jakarta.persistence.*;
import org.example.carpooling.models.enums.TravelRequestStatus;

import java.util.Objects;

@Entity
@Table(name="travel_applications")
public class TravelRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="application_id")
    private int requestId;

    @OneToOne
    @JoinColumn(name = "applicant_id")
    private User applicant;

    @OneToOne
    @JoinColumn(name = "travel_id")
    private Travel appliedTravel;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_status")
    private TravelRequestStatus requestStatus;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int id) {
        this.requestId = id;
    }

    public User getApplicant() {
        return applicant;
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    public Travel getAppliedTravel() {
        return appliedTravel;
    }

    public void setAppliedTravel(Travel appliedTravel) {
        this.appliedTravel = appliedTravel;
    }

    public TravelRequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(TravelRequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TravelRequest travelRequest = (TravelRequest) o;
        return requestId == travelRequest.requestId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(requestId);
    }
}
