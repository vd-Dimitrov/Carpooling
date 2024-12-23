package org.example.carpooling.repositories;

import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Integer> {
    Travel findTravelByDriver(User driver);
}
