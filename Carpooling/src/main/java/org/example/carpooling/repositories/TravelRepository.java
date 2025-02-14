package org.example.carpooling.repositories;

import org.example.carpooling.models.enums.TravelStatus;
import org.example.carpooling.models.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface TravelRepository extends JpaRepository<Travel, Integer> {
    @Query("select t from Travel t where (:startingPoint is null or t.startingPoint like %:startingPoint)" +
                                    "and (:endingPoint is null or t.endingPoint like %:endingPoint%)" +
                                    "and (:departureTime is null or t.departureTime >= :departureTime)" +
                                    "and (:travelStatus is null or t.travelStatus = :#{#travelStatus?.name()})" +
                                    "and (:freeSpots is null or t.freeSpots >= :freeSpots)")
    List<Travel> searchTravels(@Param("startingPoint") String startingPoint,
                               @Param("endingPoint") String endingPoint,
                               @Param("departureTime") LocalDateTime departureTime,
                               @Param("travelStatus") TravelStatus travelStatus,
                               @Param("freeSpots") int freeSpots);
    Optional<List<Travel>> findAllByDriverUserId(int driverId);
    Optional<Travel> findTravelByTravelId(int travelId);

    List<Travel> findTravelByTravelStatus(TravelStatus travelStatus);
}
