package com.parking.samurai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Samurai Backend MVP
 *
 * <p>Created by Anton Muzhytskyi, 2026</p>
 *
 * <p>This project demonstrates a full backend workflow for a parking management system,
 * including authentication (JWT), parking spot management, real-time notifications via WebSocket,
 * and rent lifecycle management.</p>
 */

@SpringBootApplication
@EnableScheduling
public class SamuraiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SamuraiApplication.class, args);
	}

}
