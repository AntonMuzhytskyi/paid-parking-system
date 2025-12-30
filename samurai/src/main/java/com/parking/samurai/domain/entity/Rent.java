package com.parking.samurai.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id", nullable = false)
    @JsonBackReference
    @NotNull(message = "Parking spot is required")
    private ParkingSpot parkingSpot;*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id", nullable = false)
    @JsonInclude(JsonInclude.Include.NON_NULL)  // или просто оставь без аннотации
    private ParkingSpot parkingSpot;

    // Поле для пользователя добавим позже, когда будет JWT
    // private User user;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    // Для фиксированного периода (например, 3 дня)
    private LocalDateTime endTime;

    // Для "rent now" с таймером — если true, аренда активна до ручного отмены или таймера
    private boolean active = true;

    // Опционально: цена, которая была на момент аренды
    private java.math.BigDecimal priceAtRentTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    //@JsonBackReference  // или @JsonIgnore, если не нужно возвращать
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    private BigDecimal totalPrice;  // Итоговая сумма

    public enum PaymentStatus {
        PENDING, PAID, FAILED, REFUNDED
    }
}