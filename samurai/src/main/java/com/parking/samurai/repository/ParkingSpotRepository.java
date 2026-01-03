package com.parking.samurai.repository;

import com.parking.samurai.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* Repository interface for managing ParkingSpot entities.
* Extends JpaRepository to provide standard CRUD operations.
* Includes a derived query method to retrieve parking spots by availability status.
*/

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {

    List<ParkingSpot> findByAvailable(boolean available);
}