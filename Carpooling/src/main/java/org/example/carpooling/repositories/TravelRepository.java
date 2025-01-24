package org.example.carpooling.repositories;

import org.example.carpooling.enums.TravelStatus;
import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Integer> {
    Optional<List<Travel>> findAllByDriverUserId(int driverId);
    Optional<Travel> findTravelByTravelId(int travelId);

    List<Travel> findTravelByTravelStatus(TravelStatus travelStatus);
}
