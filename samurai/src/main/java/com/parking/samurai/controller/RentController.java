package com.parking.samurai.controller;

import com.parking.samurai.entity.Rent;
import com.parking.samurai.entity.ParkingSpot;
import com.parking.samurai.repository.ParkingSpotRepository;
import com.parking.samurai.repository.RentRepository;
import com.parking.samurai.entity.User;
import com.parking.samurai.repository.UserRepository;
import com.parking.samurai.service.WebSocketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

/**
* This REST controller handles parking spot rental operations.
* Supports booking, canceling, and fetching active rents.
* Integrates with WebSocketService to notify clients of real-time parking spot availability changes.
* SecurityContext is used to identify the current authenticated user.
*/

@Tag(name = "Rents", description = "API for parking spot rental")
@RestController
@RequestMapping("/api/v1/rents")
@RequiredArgsConstructor
public class RentController {

    private final WebSocketService webSocketService;
    private final ParkingSpotRepository parkingSpotRepository;
    private final RentRepository rentRepository;

    private final UserRepository userRepository;

    /*private User getCurrentUser() {
        // Retrieves the currently authenticated user from Spring Security context.
        // Throws an exception if no user is authenticated.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }
        return (User) authentication.getPrincipal();
    }*/
    // Не забудь добавить в поля контроллера: private final UserRepository userRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();

        // Если это уже объект User (как мы ожидаем)
        if (principal instanceof User) {
            return (User) principal;
        }

        // Если это UserDetails (стандарт Spring Security)
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found in database"));
        }

        // Если это просто строка (Username/Email)
        if (principal instanceof String) {
            return userRepository.findByUsername((String) principal)
                    .orElseThrow(() -> new RuntimeException("User not found in database"));
        }

        throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
    }

    /*@Operation(summary = "Book a parking spot with immediate payment (main flow)")
    @PostMapping("/book/{spotId}")
    public ResponseEntity<Rent> bookSpot(@PathVariable Long spotId) {
        // Retrieves the parking spot by ID.
        ParkingSpot spot = parkingSpotRepository.findById(spotId)
                .orElseThrow(() -> new RuntimeException("Parking spot not found"));

        // Ensure the spot is available.
        if (!spot.isAvailable()) {
            throw new IllegalStateException("Parking spot is already rented");
        }

        User user = getCurrentUser();

        BigDecimal totalPrice = spot.getPricePerHour();

        // Creates a new Rent entity and marks the spot as unavailable.
        Rent rent = Rent.builder()
                .parkingSpot(spot)
                .user(user)
                .startTime(LocalDateTime.now())
                .endTime(null)
                .active(true)
                .priceAtRentTime(spot.getPricePerHour())
                .totalPrice(totalPrice)
                .paymentStatus(Rent.PaymentStatus.PAID)
                .build();

        spot.setAvailable(false);

        // Saves the rent and updates the spot.
        rentRepository.save(rent);
        parkingSpotRepository.save(spot);

        // Notify clients via WebSocket about the change in availability.
        webSocketService.notifyParkingSpotsChanged();

        return ResponseEntity.status(HttpStatus.CREATED).body(rent);
    }*/
    @Operation(summary = "Book a parking spot with immediate payment (main flow)")
    @PostMapping("/book/{spotId}")
    public ResponseEntity<Rent> bookSpot(@PathVariable Long spotId) {
        // Вызываем сервис, где вся магия с БД под защитой @Transactional
        Rent rent = rentService.bookSpot(spotId);

        // Только ПОСЛЕ успешного сохранения в БД уведомляем WebSocket
        try {
            webSocketService.notifyParkingSpotsChanged();
        } catch (Exception e) {
            // Ошибка в вебсокете не должна ломать успешную аренду
            log.error("WebSocket notification failed", e);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(rent);
    }

    @Operation(summary = "Cancel a rent (free up spot, no refund)")
    @PostMapping("/cancel/{rentId}")
    public ResponseEntity<Void> cancelRent(@PathVariable Long rentId) {
        // Retrieves the rent by ID.
        Rent rent = rentRepository.findById(rentId)
                .orElseThrow(() -> new RuntimeException("Rent not found"));

        User currentUser = getCurrentUser();

        // Only the owner of the rent can cancel it.
        if (!rent.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("You can only cancel your own rent");
        }

        if (!rent.isActive()) {
            throw new IllegalStateException("Rent is already inactive");
        }

        // Marks rent as inactive and frees up the parking spot.
        rent.setActive(false);
        rent.getParkingSpot().setAvailable(true);

        rentRepository.save(rent);
        parkingSpotRepository.save(rent.getParkingSpot());
        webSocketService.notifyParkingSpotsChanged();

        return ResponseEntity.noContent().build(); // 204 — no content
    }


    @Operation(summary = "Get my current active rent")
    @GetMapping("/my-active")
    public ResponseEntity<Rent> getMyActiveRent() {
        // Retrieves the currently authenticated user.
        User user = getCurrentUser();

        // Finds the active rent for the user.
        Optional<Rent> activeRent = rentRepository.findTopByUserIdAndActiveTrue(user.getId());

        // Returns the active rent or 204 if none exists.
        return activeRent
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build()); // 204 если нет активной
    }

}
