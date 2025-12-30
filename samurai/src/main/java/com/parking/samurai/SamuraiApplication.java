package com.parking.samurai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SamuraiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SamuraiApplication.class, args);
	}

}
