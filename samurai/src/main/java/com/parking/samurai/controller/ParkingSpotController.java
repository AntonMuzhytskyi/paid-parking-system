package com.parking.samurai.controller;

import com.parking.samurai.domain.entity.ParkingSpot;
import com.parking.samurai.service.ParkingSpotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Parking Spots", description = "API для управления парковочными местами")
@RestController
@RequestMapping("/api/v1/parking-spots")
@RequiredArgsConstructor
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;

    @Operation(summary = "Создать новое парковочное место")
    @PostMapping
    public ResponseEntity<ParkingSpot> create(@RequestBody ParkingSpot spot) {
        ParkingSpot created = parkingSpotService.createParkingSpot(spot);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Получить все парковочные места")
    @GetMapping
    public ResponseEntity<List<ParkingSpot>> getAll() {
        return ResponseEntity.ok(parkingSpotService.getAllParkingSpots());
    }

    @Operation(summary = "Получить только доступные места")
    @GetMapping("/available")
    public ResponseEntity<List<ParkingSpot>> getAvailable() {
        return ResponseEntity.ok(parkingSpotService.getAvailableParkingSpots());
    }

    @Operation(summary = "Получить место по ID")
    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpot> getById(@PathVariable Long id) {
        return ResponseEntity.ok(parkingSpotService.getParkingSpotById(id));
    }
}