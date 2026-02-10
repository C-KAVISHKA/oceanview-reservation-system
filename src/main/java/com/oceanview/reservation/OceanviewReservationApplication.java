package com.oceanview.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the OceanView Reservation System.
 * 
 * This Spring Boot application manages hotel reservations,
 * providing REST API endpoints and serving a static frontend.
 * 
 * @author Enzo
 * @version 1.0.0
 * @since 2026-02-10
 */
@SpringBootApplication
public class OceanviewReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanviewReservationApplication.class, args);
    }
}
