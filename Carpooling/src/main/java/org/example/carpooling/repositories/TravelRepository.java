package org.example.carpooling.repositories;

import org.example.carpooling.models.enums.TravelStatus;
import org.example.carpooling.models.Travel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface TravelRepository extends JpaRepository<Travel, Integer> {
    @Query("select t from Travel t where (:title is null or t.title like %:title%)" +
            "and (:startingPoint is null or t.startingPoint like %:startingPoint%)" +
            "and (:endingPoint is null or t.endingPoint like %:endingPoint%)" +
            "and (:departureTime is null or t.departureTime <= :departureTime)" +
            "and (:freeSpots is null or t.freeSpots >= :freeSpots)" +
            "and (:createdAt is null or t.createdAt <= :createAt)")
    List<Travel> searchTravels(@Param("title") String title,
                               @Param("startingPoint") String startingPoint,
                               @Param("endingPoint") String endingPoint,
                               @Param("departureTime") Timestamp departureTime,
                               @Param("freeSpots") int freeSpots,
                               @Param("createdAt") Timestamp timestamp);

    @Query("select t from Travel t where (:title is null or t.title like %:title%)" +
            "and (:startingPoint is null or t.startingPoint like %:startingPoint%)" +
            "and (:endingPoint is null or t.endingPoint like %:endingPoint%)" +
            "and (:departureTime is null or t.departureTime >= :departureTime)" +
            "and (:freeSpots is null or t.freeSpots >= :freeSpots)" +
            "and (:createdAt is null or t.createdAt <= :createdAt)")
    Page<Travel> searchTravelsPaginated(@Param("title") String title,
                                        @Param("startingPoint") String startingPoint,
                                        @Param("endingPoint") String endingPoint,
                                        @Param("departureTime") Timestamp departureTime,
                                        @Param("freeSpots") int freeSpots,
                                        @Param("createdAt") Timestamp createdAt,
                                        Pageable pageable);

    Optional<List<Travel>> findAllByDriverUserId(int driverId);

    Optional<Travel> findTravelByTravelId(int travelId);

    List<Travel> findTravelByTravelStatus(TravelStatus travelStatus);
}
