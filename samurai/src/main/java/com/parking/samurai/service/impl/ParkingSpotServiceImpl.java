package com.parking.samurai.service.impl;

import com.parking.samurai.domain.entity.ParkingSpot;
import com.parking.samurai.repository.ParkingSpotRepository;
import com.parking.samurai.service.ParkingSpotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingSpotServiceImpl implements ParkingSpotService {

    private final ParkingSpotRepository parkingSpotRepository;

    @Override
    @Transactional
    public ParkingSpot createParkingSpot(ParkingSpot spot) {
        // Можно добавить здесь дополнительную валидацию бизнес-логики
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