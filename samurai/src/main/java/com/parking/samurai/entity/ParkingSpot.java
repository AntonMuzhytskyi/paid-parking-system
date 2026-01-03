package com.parking.samurai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
* JPA entity representing a parking spot in the system.
* Contains pricing and availability information.
* Linked to Rent entities to keep rental history (for future extensions).
* Designed to be used as a core domain model in the parking management flow.
*/

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
    private String location;  // Example: "A-15", "B-3"

    @Positive(message = "Price must be positive")
    private BigDecimal pricePerHour;

    private boolean available = true;

    // Rental history for this parking spot.
    // Marked with @JsonIgnore to avoid circular references in JSON serialization.
    // Can be used later for analytics or reporting features.
    @OneToMany(mappedBy = "parkingSpot", cascade = CascadeType.ALL, orphanRemoval = true)
    //@JsonManagedReference
    @JsonIgnore
    private List<Rent> rents = new ArrayList<>();
}