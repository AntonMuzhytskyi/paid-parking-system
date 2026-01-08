package com.parking.samurai.scheduler;

import com.parking.samurai.entity.Rent;
import com.parking.samurai.repository.RentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
* Scheduled component responsible for expiring outdated "rent now" sessions.
* Periodically checks for active rents without an end time that exceeded the allowed duration.
* Automatically frees parking spots and updates rent status within a transactional context.
*/

@Component
@RequiredArgsConstructor
@Slf4j
public class RentExpirationScheduler {

    private final RentRepository rentRepository;

    // Runs every 30 seconds (configurable).
    // Can be adjusted or moved to application properties if needed.
    @Scheduled(fixedRate = 30000)
    @Transactional
    public void expireOldRents() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutoff = now.minusMinutes(30); // Configurable timeout (30 minutes)

        // Finds active rents that should be expired.
        List<Rent> expiredRents = rentRepository.findByActiveTrueAndEndTimeIsNullAndStartTimeBefore(cutoff);

        if (expiredRents.isEmpty()) {
            return;
        }

        log.info("Found {} expired 'rent now' sessions. Expiring...", expiredRents.size());

        /*for (Rent rent : expiredRents) {
            // Marks rent as inactive and frees the associated parking spot.
            rent.setActive(false);
            rent.getParkingSpot().setAvailable(true);
            log.info("Expired rent id={} for spot id={}", rent.getId(), rent.getParkingSpot().getId());
        }*/
        for (Rent rent : expiredRents) {
            rent.setActive(false);
            if (rent.getParkingSpot() != null) {
                rent.getParkingSpot().setAvailable(true);
            }
        }

        rentRepository.saveAll(expiredRents);
        log.info("Expired {} sessions", expiredRents.size());
        // Explicit saveAll() is not required.
        // Entities are managed by Hibernate and changes are flushed automatically at transaction commit.
    }
}