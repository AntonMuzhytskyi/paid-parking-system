package com.parking.samurai.scheduler;


import com.parking.samurai.domain.entity.Rent;
import com.parking.samurai.repository.RentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RentExpirationScheduler {

    private final RentRepository rentRepository;

    // Запускается каждые 30 секунд (можно изменить)
    @Scheduled(fixedRate = 30000)
    @Transactional
    public void expireOldRents() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutoff = now.minusMinutes(30);  // Настраиваемый таймаут — 30 минут

        List<Rent> expiredRents = rentRepository.findByActiveTrueAndEndTimeIsNullAndStartTimeBefore(cutoff);

        if (expiredRents.isEmpty()) {
            return;
        }

        log.info("Found {} expired 'rent now' sessions. Expiring...", expiredRents.size());

        for (Rent rent : expiredRents) {
            rent.setActive(false);
            rent.getParkingSpot().setAvailable(true);
            log.info("Expired rent id={} for spot id={}", rent.getId(), rent.getParkingSpot().getId());
        }

        // saveAll не обязателен — изменения уже в транзакции и отслеживаются Hibernate
    }
}