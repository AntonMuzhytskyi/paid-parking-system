package com.parking.samurai.controller;

import com.parking.samurai.entity.ParkingSpot;
import com.parking.samurai.service.ParkingSpotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* This REST controller provides endpoints to manage parking spots in the system.
* Supports creating new spots, retrieving all spots, retrieving only available spots, and fetching spots by ID.
* Business logic is delegated to ParkingSpotService to maintain separation of concerns.
*/

@Tag(name = "Parking Spots", description = "API for managing parking spots")
@RestController
@RequestMapping("/api/v1/parking-spots")
@RequiredArgsConstructor
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;

    @Operation(summary = "Create a new parking spot")
    @PostMapping
    public ResponseEntity<ParkingSpot> create(@RequestBody ParkingSpot spot) {
        // Accepts a ParkingSpot entity from the request body and delegates creation to the service layer.
        // Returns the created ParkingSpot with HTTP 201 (Created) status.
        ParkingSpot created = parkingSpotService.createParkingSpot(spot);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieve all parking spots")
    @GetMapping
    public ResponseEntity<List<ParkingSpot>> getAll() {
        // Retrieves all parking spots from the service layer.
        return ResponseEntity.ok(parkingSpotService.getAllParkingSpots());
    }

    @Operation(summary = "Retrieve only available parking spots")
    @GetMapping("/available")
    public ResponseEntity<List<ParkingSpot>> getAvailable() {
        // Retrieves only the spots that are currently available for parking.
        return ResponseEntity.ok(parkingSpotService.getAvailableParkingSpots());
    }

    @Operation(summary = "Retrieve a parking spot by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpot> getById(@PathVariable Long id) {
        // Fetches a specific parking spot using its ID from the service layer.
        return ResponseEntity.ok(parkingSpotService.getParkingSpotById(id));
    }
}