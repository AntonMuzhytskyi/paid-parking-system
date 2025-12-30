package com.parking.samurai.controller;

import com.parking.samurai.domain.entity.Rent;
import com.parking.samurai.domain.entity.ParkingSpot;
import com.parking.samurai.repository.ParkingSpotRepository;
import com.parking.samurai.repository.RentRepository;
import com.parking.samurai.domain.entity.User;
import com.parking.samurai.service.WebSocketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Tag(name = "Rents", description = "API для аренды парковочных мест")
@RestController
@RequestMapping("/api/v1/rents")
@RequiredArgsConstructor
public class RentController {

    private final WebSocketService webSocketService;
    private final ParkingSpotRepository parkingSpotRepository;
    private final RentRepository rentRepository;

    // Метод для получения текущего пользователя из JWT
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }
        return (User) authentication.getPrincipal();
    }

    @Operation(summary = "Арендовать место с немедленной оплатой (основной флоу)")
    @PostMapping("/book/{spotId}")
    public ResponseEntity<Rent> bookSpot(@PathVariable Long spotId) {
        ParkingSpot spot = parkingSpotRepository.findById(spotId)
                .orElseThrow(() -> new RuntimeException("Parking spot not found"));

        if (!spot.isAvailable()) {
            throw new IllegalStateException("Parking spot is already rented");
        }

        User user = getCurrentUser();

        BigDecimal totalPrice = spot.getPricePerHour(); // минимум за 1 час

        Rent rent = Rent.builder()
                .parkingSpot(spot)
                .user(user)
                .startTime(LocalDateTime.now())
                .endTime(null) // для "rent now"
                .active(true)
                .priceAtRentTime(spot.getPricePerHour())
                .totalPrice(totalPrice)
                .paymentStatus(Rent.PaymentStatus.PAID) // сразу оплачено
                .build();

        spot.setAvailable(false);

        rentRepository.save(rent);
        parkingSpotRepository.save(spot);
        webSocketService.notifyParkingSpotsChanged();

        return ResponseEntity.status(HttpStatus.CREATED).body(rent);
    }

    @Operation(summary = "Отменить аренду (освободить место, без возврата денег)")
    @PostMapping("/cancel/{rentId}")
    public ResponseEntity<Void> cancelRent(@PathVariable Long rentId) {
        Rent rent = rentRepository.findById(rentId)
                .orElseThrow(() -> new RuntimeException("Rent not found"));

        User currentUser = getCurrentUser();
        if (!rent.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("You can only cancel your own rent");
        }

        if (!rent.isActive()) {
            throw new IllegalStateException("Rent is already inactive");
        }

        rent.setActive(false);
        rent.getParkingSpot().setAvailable(true);

        rentRepository.save(rent);
        parkingSpotRepository.save(rent.getParkingSpot());
        webSocketService.notifyParkingSpotsChanged();

        return ResponseEntity.noContent().build(); // 204 — идеально
    }



}

/*
package com.parking.samurai.controller;

import com.parking.samurai.domain.entity.ParkingSpot;
import com.parking.samurai.domain.entity.Rent;
import com.parking.samurai.domain.entity.User;
import com.parking.samurai.repository.ParkingSpotRepository;
import com.parking.samurai.repository.RentRepository;
import com.parking.samurai.service.RentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "Rents", description = "API для аренды парковочных мест")
@RestController
@RequestMapping("/api/v1/rents")
@RequiredArgsConstructor
public class RentController {

    private final RentService rentService;
    private final ParkingSpotRepository spotRepository;
    private final RentRepository rentRepository;

    @Operation(summary = "Арендовать место прямо сейчас (с таймером)")
    @PostMapping("/now/{spotId}")
    public ResponseEntity<Rent> rentNow(@PathVariable Long spotId) {
        Rent rent = rentService.rentSpotNow(spotId);
        return new ResponseEntity<>(rent, HttpStatus.CREATED);
    }

    @Operation(summary = "Арендовать место на фиксированный период")
    @PostMapping("/period/{spotId}")
    public ResponseEntity<Rent> rentForPeriod(
            @PathVariable Long spotId,
            @RequestParam String endTime) {  // формат: 2025-12-30T15:30:00
        LocalDateTime end = LocalDateTime.parse(endTime);
        Rent rent = rentService.rentSpotForPeriod(spotId, end);
        return new ResponseEntity<>(rent, HttpStatus.CREATED);
    }

    @Operation(summary = "Отменить аренду")
    @PostMapping("/cancel/{rentId}")
    public ResponseEntity<Void> cancel(@PathVariable Long rentId) {
        rentService.cancelRent(rentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Арендовать и сразу оплатить место (рекомендуемый флоу)")
    @PostMapping("/book-and-pay/{spotId}")
    public ResponseEntity<Rent> bookAndPay(@PathVariable Long spotId) {
        // Сначала создаём аренду (как в rentSpotNow)
        ParkingSpot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new RuntimeException("Spot not found"));

        if (!spot.isAvailable()) {
            throw new IllegalStateException("Spot is already rented");
        }

        User user = getCurrentUser();  // метод из SecurityContext

        Rent rent = Rent.builder()
                .parkingSpot(spot)
                .user(user)
                .startTime(LocalDateTime.now())
                .active(true)
                .priceAtRentTime(spot.getPricePerHour())
                .totalPrice(spot.getPricePerHour())  // минимум за 1 час, можно улучшить
                .paymentStatus(Rent.PaymentStatus.PAID)  // сразу оплачено!
                .build();

        spot.setAvailable(false);

        rentRepository.save(rent);
        spotRepository.save(spot);

        return ResponseEntity.status(HttpStatus.CREATED).body(rent);
    }
}*/
