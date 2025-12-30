package com.parking.samurai.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parking_spots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parking_spot_seq")
    @SequenceGenerator(name = "parking_spot_seq", sequenceName = "parking_spot_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message = "Location is required")
    private String location; // Например, "A-15", "B-3"

    @Positive(message = "Price must be positive")
    private BigDecimal pricePerHour;

    private boolean available = true; // По умолчанию доступно

    // История аренд этого места (для будущего функционала)
    @OneToMany(mappedBy = "parkingSpot", cascade = CascadeType.ALL, orphanRemoval = true)
    //@JsonManagedReference
    @JsonIgnore
    private List<Rent> rents = new ArrayList<>();
}