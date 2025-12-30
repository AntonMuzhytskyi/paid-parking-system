package com.parking.samurai.service;

import com.parking.samurai.domain.entity.ParkingSpot;

import java.util.List;

public interface ParkingSpotService {

    ParkingSpot createParkingSpot(ParkingSpot spot);

    List<ParkingSpot> getAllParkingSpots();

    List<ParkingSpot> getAvailableParkingSpots();

    ParkingSpot getParkingSpotById(Long id);
}