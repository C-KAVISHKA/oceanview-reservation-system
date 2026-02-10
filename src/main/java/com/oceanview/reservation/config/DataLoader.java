package com.oceanview.reservation.config;

import com.oceanview.reservation.model.Reservation;
import com.oceanview.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

// Loads sample reservation data into the database on startup (only if empty)
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final ReservationRepository reservationRepository;

    @Override
    public void run(String... args) {
        log.info("DataLoader: Checking if sample data needs to be loaded...");

        long existingCount = reservationRepository.count();

        if (existingCount > 0) {
            log.info("DataLoader: Database already contains {} reservation(s). Skipping data load.", existingCount);
            return;
        }

        log.info("DataLoader: Database is empty. Loading sample reservations...");

        // First sample guest
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
        reservation1.setCreatedAt(LocalDateTime.now().minusDays(5));
        reservation1.setUpdatedAt(LocalDateTime.now().minusDays(5));

        reservationRepository.save(reservation1);
        log.info("DataLoader: Created reservation for {}", reservation1.getGuestFullName());

        // Second sample guest
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
        reservation2.setCreatedAt(LocalDateTime.now().minusDays(3));
        reservation2.setUpdatedAt(LocalDateTime.now().minusDays(3));

        reservationRepository.save(reservation2);
        log.info("DataLoader: Created reservation for {}", reservation2.getGuestFullName());

        log.info("DataLoader: Loaded {} sample reservations.", reservationRepository.count());
    }
}
