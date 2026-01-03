package com.parking.samurai.service.impl;

import com.parking.samurai.entity.ParkingSpot;
import com.parking.samurai.repository.ParkingSpotRepository;
import com.parking.samurai.service.ParkingSpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* Implementation of ParkingSpotService.
* Encapsulates all business logic related to parking spot management.
* Provides methods for creating, retrieving, and filtering parking spots.
* Uses @Transactional for operations that modify data.
*/

@Service
@RequiredArgsConstructor
public class ParkingSpotServiceImpl implements ParkingSpotService {

    private final ParkingSpotRepository parkingSpotRepository;

    @Override
    @Transactional
    public ParkingSpot createParkingSpot(ParkingSpot spot) {
        // Business validations can be added here if needed (e.g., check unique location)
        return parkingSpotRepository.save(spot);
    }

    @Override
    public List<ParkingSpot> getAllParkingSpots() {
        return parkingSpotRepository.findAll();
    }

    @Override
    public List<ParkingSpot> getAvailableParkingSpots() {
        return parkingSpotRepository.findByAvailable(true);
    }

    @Override
    public ParkingSpot getParkingSpotById(Long id) {
        return parkingSpotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking spot not found with id: " + id));
    }
}