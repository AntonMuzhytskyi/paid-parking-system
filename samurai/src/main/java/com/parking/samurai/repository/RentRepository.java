package com.parking.samurai.repository;

import com.parking.samurai.entity.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
* Repository interface for managing Rent entities.
* Extends JpaRepository to provide standard CRUD operations.
* Contains custom derived query methods for rental lifecycle management and business logic.
*/

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {

    // Finds all rents for a specific parking spot filtered by active status.
    // Useful for checking whether a spot is currently rented.
    List<Rent> findByParkingSpotIdAndActive(Long spotId, boolean active);

    // Finds all active rents without an end time that started before the given cutoff.
    // Can be used for scheduled tasks (e.g. auto-expiration or timeout handling).
    List<Rent> findByActiveTrueAndEndTimeIsNullAndStartTimeBefore(LocalDateTime cutoff);

    // Retrieves the current active rent for a specific user, if any.
    // Used to ensure that a user can have only one active rent at a time.
    Optional<Rent> findTopByUserIdAndActiveTrue(Long userId);

}