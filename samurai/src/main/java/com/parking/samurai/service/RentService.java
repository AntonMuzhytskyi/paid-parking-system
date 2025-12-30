package com.parking.samurai.service;


import com.parking.samurai.domain.entity.Rent;

import java.time.LocalDateTime;

public interface RentService {

    Rent rentSpotNow(Long spotId);  // "rent now" с таймером

    Rent rentSpotForPeriod(Long spotId, LocalDateTime endTime);  // фиксированный период

    void cancelRent(Long rentId);  // отмена аренды
}