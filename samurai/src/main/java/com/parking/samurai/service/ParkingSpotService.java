package com.parking.samurai.service;

import com.parking.samurai.entity.ParkingSpot;

import java.util.List;

/**
* Defines business operations related to parking spot management.
* Encapsulates parking availability logic and hides persistence details.
*/

public interface ParkingSpotService {

    ParkingSpot createParkingSpot(ParkingSpot spot);

    List<ParkingSpot> getAllParkingSpots();

    List<ParkingSpot> getAvailableParkingSpots();

    ParkingSpot getParkingSpotById(Long id);
}