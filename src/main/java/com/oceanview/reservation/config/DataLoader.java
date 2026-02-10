package com.oceanview.reservation.config;

import com.oceanview.reservation.model.Reservation;
import com.oceanview.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DataLoader - Seeds the database with sample reservation data.
 * 
 * This component runs at application startup and populates the database
 * with initial sample reservations if the database is empty. This is useful
 * for development and testing purposes.
 * 
 * The loader is idempotent - it only inserts data if the reservations table
 * is empty (count == 0), preventing duplicate entries on application restarts.
 * 
 * @author Enzo
 * @version 1.0.0
 * @since 2026-02-10
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final ReservationRepository reservationRepository;

    /**
     * Runs at application startup to seed sample data.
     * 
     * Only inserts data if the database is empty to maintain idempotency.
     * Creates two sample reservations with realistic future dates and contact information.
     * 
     * @param args command line arguments (not used)
     */
    @Override
    public void run(String... args) {
        log.info("DataLoader: Checking if sample data needs to be loaded...");
        
        // Check if database is empty (idempotent check)
        long existingCount = reservationRepository.count();
        
        if (existingCount > 0) {
            log.info("DataLoader: Database already contains {} reservation(s). Skipping data load.", existingCount);
            return;
        }
        
        log.info("DataLoader: Database is empty. Loading sample reservations...");
        
        // Sample Reservation 1: John Smith - DOUBLE Room
        Reservation reservation1 = new Reservation();
        reservation1.setGuestFullName("John Smith");
        reservation1.setEmail("john.smith@example.com");
        reservation1.setContactNumber("+1-555-0101");
        reservation1.setAddress("123 Ocean Drive, Miami Beach, FL 33139");
        reservation1.setCheckIn(LocalDate.of(2026, 6, 1));
        reservation1.setCheckOut(LocalDate.of(2026, 6, 4));
        reservation1.setNumberOfGuests(2);
        reservation1.setRoomType("DOUBLE");
        reservation1.setStatus("CONFIRMED");
        reservation1.setCreatedAt(LocalDateTime.now().minusDays(5)); // Created 5 days ago
        reservation1.setUpdatedAt(LocalDateTime.now().minusDays(5));
        
        reservationRepository.save(reservation1);
        log.info("DataLoader: Created reservation for {} - {} room ({} to {})", 
                reservation1.getGuestFullName(),
                reservation1.getRoomType(),
                reservation1.getCheckIn(),
                reservation1.getCheckOut());
        
        // Sample Reservation 2: Sarah Johnson - SUITE Room
        Reservation reservation2 = new Reservation();
        reservation2.setGuestFullName("Sarah Johnson");
        reservation2.setEmail("sarah.johnson@example.com");
        reservation2.setContactNumber("+1-555-0202");
        reservation2.setAddress("456 Sunset Boulevard, Los Angeles, CA 90028");
        reservation2.setCheckIn(LocalDate.of(2026, 7, 15));
        reservation2.setCheckOut(LocalDate.of(2026, 7, 20));
        reservation2.setNumberOfGuests(2);
        reservation2.setRoomType("SUITE");
        reservation2.setStatus("CONFIRMED");
        reservation2.setCreatedAt(LocalDateTime.now().minusDays(3)); // Created 3 days ago
        reservation2.setUpdatedAt(LocalDateTime.now().minusDays(3));
        
        reservationRepository.save(reservation2);
        log.info("DataLoader: Created reservation for {} - {} room ({} to {})", 
                reservation2.getGuestFullName(),
                reservation2.getRoomType(),
                reservation2.getCheckIn(),
                reservation2.getCheckOut());
        
        log.info("DataLoader: Successfully loaded {} sample reservations.", reservationRepository.count());
    }
}
