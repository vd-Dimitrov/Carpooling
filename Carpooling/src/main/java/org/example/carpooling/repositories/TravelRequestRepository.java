package org.example.carpooling.repositories;

import org.example.carpooling.models.TravelRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelRequestRepository extends JpaRepository<TravelRequest, Integer> {
    Optional<TravelRequest> findTravelRequestByRequestId(int requestId);

    Optional<List<TravelRequest>> findTravelRequestsByAppliedTravelTravelId(int travelId);

    Optional<List<TravelRequest>> findTravelRequestsByApplicantUserId(int applicantId);

}
