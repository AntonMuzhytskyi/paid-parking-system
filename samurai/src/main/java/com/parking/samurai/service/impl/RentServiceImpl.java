package com.parking.samurai.service.impl;

import com.parking.samurai.entity.ParkingSpot;
import com.parking.samurai.entity.Rent;
import com.parking.samurai.entity.User;
import com.parking.samurai.repository.ParkingSpotRepository;
import com.parking.samurai.repository.RentRepository;
import com.parking.samurai.repository.UserRepository;
import com.parking.samurai.service.RentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
* Service implementation for managing parking spot rentals.
* Handles "rent now", fixed-period rentals, and cancellations.
* Encapsulates all business rules such as availability checks, price calculation,
* active status management, and user association.
* Marked as @Transactional to ensure atomic operations.
*/

@Service
@RequiredArgsConstructor
@Transactional
public class RentServiceImpl implements RentService {

    private final ParkingSpotRepository spotRepository;
    private final RentRepository rentRepository;
    private final UserRepository userRepository;

    @Override
    public Rent rentSpotNow(Long spotId) {
        ParkingSpot spot = getAvailableSpot(spotId);
        User user = getCurrentUser();

        spot.setAvailable(false);

        Rent rent = Rent.builder()
                .parkingSpot(spot)
                .user(user)
                .startTime(LocalDateTime.now())
                .active(true)
                .priceAtRentTime(spot.getPricePerHour())
                .build();

        BigDecimal totalPrice = spot.getPricePerHour(); // minimum 1 hour
        rent.setTotalPrice(totalPrice);
        rent.setPriceAtRentTime(spot.getPricePerHour());
        rent.setPaymentStatus(Rent.PaymentStatus.PENDING);

        rentRepository.save(rent);
        spotRepository.save(spot);

        return rent;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof User) {
            return (User) principal;
        }

        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found in DB: " + username));
    }

    @Override
    public Rent rentSpotForPeriod(Long spotId, LocalDateTime endTime) {
        if (endTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("End time cannot be in the past");
        }

        ParkingSpot spot = getAvailableSpot(spotId);

        User user = getCurrentUser();

        spot.setAvailable(false);

        Rent rent = Rent.builder()
                .parkingSpot(spot)
                .user(user)
                .startTime(LocalDateTime.now())
                .endTime(endTime)
                .active(true)
                .priceAtRentTime(spot.getPricePerHour())
                .build();

        BigDecimal totalPrice = calculateTotalPrice(spot, LocalDateTime.now(), endTime);
        rent.setTotalPrice(totalPrice);
        rent.setEndTime(endTime);
        rent.setPaymentStatus(Rent.PaymentStatus.PENDING);

        rentRepository.save(rent);
        spotRepository.save(spot);

        return rent;
    }

    @Override
    public void cancelRent(Long rentId) {
        Rent rent = rentRepository.findById(rentId)
                .orElseThrow(() -> new RuntimeException("Rent not found"));

        if (!rent.isActive()) {
            throw new IllegalStateException("Rent is already inactive");
        }

        rent.setActive(false);
        ParkingSpot spot = rent.getParkingSpot();
        spot.setAvailable(true);

        rentRepository.save(rent);
        spotRepository.save(spot);
    }

    private ParkingSpot getAvailableSpot(Long spotId) {
        ParkingSpot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new RuntimeException("Spot not found"));

        if (!spot.isAvailable()) {
            throw new IllegalStateException("Spot is already rented");
        }

        return spot;
    }

    private BigDecimal calculateTotalPrice(ParkingSpot spot, LocalDateTime start, LocalDateTime end) {
        if (end == null) {
            // For "rent now": minimum 1 hour charge
            return spot.getPricePerHour().multiply(BigDecimal.valueOf(1)); // 1 час минимум
        }
        long hours = java.time.Duration.between(start, end).toHours();
        if (hours < 1) hours = 1;
        return spot.getPricePerHour().multiply(BigDecimal.valueOf(hours));
    }

    @Override
    @Transactional
    public Rent bookSpot(Long spotId) {
        ParkingSpot spot = spotRepository.findById(spotId)
                .orElseThrow(() -> new RuntimeException("Parking spot not found"));

        if (!spot.isAvailable()) {
            throw new IllegalStateException("Parking spot is already rented");
        }

        User user = getCurrentUser();
        Rent rent = Rent.builder()
                .parkingSpot(spot)
                .user(user)
                .startTime(LocalDateTime.now())
                .active(true)
                .priceAtRentTime(spot.getPricePerHour())
                .totalPrice(spot.getPricePerHour())
                .paymentStatus(Rent.PaymentStatus.PAID)
                .build();

        spot.setAvailable(false);

        spotRepository.save(spot);
        return rentRepository.save(rent);
    }
}