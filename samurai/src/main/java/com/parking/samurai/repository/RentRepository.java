package com.parking.samurai.repository;


import com.parking.samurai.domain.entity.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {

    List<Rent> findByParkingSpotIdAndActive(Long spotId, boolean active);

    List<Rent> findByActiveTrueAndEndTimeIsNullAndStartTimeBefore(LocalDateTime cutoff);
}