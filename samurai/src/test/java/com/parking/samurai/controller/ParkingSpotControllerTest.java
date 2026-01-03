package com.parking.samurai.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.parking.samurai.entity.ParkingSpot;
import com.parking.samurai.repository.ParkingSpotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ParkingSpotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ParkingSpotRepository repository;

    @Test
    void shouldCreateAndGetParkingSpot() throws Exception {
        ParkingSpot spot = ParkingSpot.builder()
                .location("Test-01")
                .pricePerHour(BigDecimal.valueOf(10.00))
                .available(true)
                .build();

        String spotJson = objectMapper.writeValueAsString(spot);

        mockMvc.perform(post("/api/v1/parking-spots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(spotJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/parking-spots/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].location").value("Test-01"));
    }
}