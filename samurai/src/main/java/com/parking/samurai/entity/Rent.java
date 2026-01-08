package com.parking.samurai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
* JPA entity representing a parking spot rent.
* Stores information about rental period, pricing, payment status, and ownership.
* Acts as a transactional domain entity connecting User and ParkingSpot.
* Designed to support both short-term and extended rental flows.
*/

@Entity
@Table(name = "rents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rent_seq")
    @SequenceGenerator(name = "rent_seq", sequenceName = "rent_seq", allocationSize = 1)
    private Long id;

    // Reference to the rented parking spot.
    // LAZY fetch is used to avoid unnecessary data loading.
    // Included conditionally in JSON to prevent circular references if needed.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id", nullable = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ParkingSpot parkingSpot;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    // Optional end time.
    // Can be null for "rent now" scenarios where the duration is not fixed.
    private LocalDateTime endTime;

    // Indicates whether the rent is currently active.
    // Active rents block the parking spot from being booked by others.
    @Builder.Default
    private boolean active = true;

    // Price per hour at the moment of rent creation.
    // Stored to preserve historical pricing even if spot price changes later.
    private java.math.BigDecimal priceAtRentTime;

    // Reference to the user who owns this rent.
    // Marked with @JsonIgnore to avoid exposing user details in API responses.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    //Current payment status of the rent.
    //Stored as STRING for better readability and database safety.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    //Total price
    // TODO: Add automatic price calculation based on rent duration
    private BigDecimal totalPrice;

    //Payment states for a rent lifecycle.
    public enum PaymentStatus {
        PENDING, PAID, FAILED, REFUNDED
    }
}