package com.parking.samurai.service;

import com.parking.samurai.entity.Rent;

import java.time.LocalDateTime;

/**
* Handles parking spot rental lifecycle:
* - instant rent ("rent now")
* - fixed-period rent
* - rent cancellation
*/

 public interface RentService {

    Rent rentSpotNow(Long spotId);

    Rent rentSpotForPeriod(Long spotId, LocalDateTime endTime);

    void cancelRent(Long rentId);
    Rent bookSpot(Long spotId);
}