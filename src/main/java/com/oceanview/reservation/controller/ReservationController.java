package com.oceanview.reservation.controller;

import com.oceanview.reservation.model.Reservation;
import com.oceanview.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// Handles all reservation CRUD operations via REST API
@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    // POST /api/reservations - create a new reservation
    @PostMapping
    public ResponseEntity<?> createReservation(@Valid @RequestBody Reservation reservation) {
        log.info("Creating reservation for: {}", reservation.getGuestFullName());
        
        try {
            Reservation savedReservation = reservationService.create(reservation);
            log.info("Reservation created successfully with ID: {}", savedReservation.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);
        } catch (IllegalArgumentException e) {
            log.error("Validation error creating reservation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error creating reservation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to create reservation: " + e.getMessage()));
        }
    }

    // GET /api/reservations/{id} - get a single reservation
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable Long id) {
        log.info("Fetching reservation {}", id);
        
        Optional<Reservation> reservation = reservationService.getById(id);
        
        if (reservation.isPresent()) {
            log.info("Reservation found: {}", id);
            return ResponseEntity.ok(reservation.get());
        } else {
            log.warn("Reservation not found: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Reservation not found with ID: " + id));
        }
    }

    // GET /api/reservations - list all or search by guest name
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations(
            @RequestParam(required = false) String guestName) {

        if (guestName != null && !guestName.trim().isEmpty()) {
            log.info("Searching reservations for: {}", guestName);
            List<Reservation> reservations = reservationService.searchByGuest(guestName);
            return ResponseEntity.ok(reservations);
        } else {
            log.info("Fetching all reservations");
            List<Reservation> reservations = reservationService.listAll();
            return ResponseEntity.ok(reservations);
        }
    }

    // PUT /api/reservations/{id} - update a reservation
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(
            @PathVariable Long id,
            @Valid @RequestBody Reservation reservation) {

        log.info("Updating reservation {}", id);
        
        try {
            Reservation updatedReservation = reservationService.update(id, reservation);
            log.info("Reservation updated successfully: {}", id);
            return ResponseEntity.ok(updatedReservation);
        } catch (IllegalArgumentException e) {
            log.error("Error updating reservation {}: {}", id, e.getMessage());
            
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(e.getMessage()));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(e.getMessage()));
            }
        } catch (Exception e) {
            log.error("Error updating reservation {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to update reservation: " + e.getMessage()));
        }
    }

    // DELETE /api/reservations/{id} - delete a reservation
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        log.info("Deleting reservation {}", id);
        
        try {
            reservationService.delete(id);
            log.info("Reservation deleted successfully: {}", id);
            return ResponseEntity.ok(new SuccessResponse("Reservation deleted successfully"));
        } catch (IllegalArgumentException e) {
            log.error("Error deleting reservation {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error deleting reservation {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to delete reservation: " + e.getMessage()));
        }
    }

    // GET /api/reservations/status/{status} - filter by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Reservation>> getReservationsByStatus(@PathVariable String status) {
        log.info("Fetching reservations with status: {}", status);
        List<Reservation> reservations = reservationService.getByStatus(status.toUpperCase());
        log.info("Found {} reservations with status {}", reservations.size(), status);
        return ResponseEntity.ok(reservations);
    }

    // GET /api/reservations/active - currently occupied rooms
    @GetMapping("/active")
    public ResponseEntity<List<Reservation>> getActiveReservations() {
        log.info("Fetching active reservations");
        List<Reservation> reservations = reservationService.getActiveReservations();
        log.info("Found {} active reservations", reservations.size());
        return ResponseEntity.ok(reservations);
    }

    // GET /api/reservations/upcoming - future check-ins
    @GetMapping("/upcoming")
    public ResponseEntity<List<Reservation>> getUpcomingReservations() {
        log.info("Fetching upcoming reservations");
        List<Reservation> reservations = reservationService.getUpcomingReservations();
        log.info("Found {} upcoming reservations", reservations.size());
        return ResponseEntity.ok(reservations);
    }

    // GET /api/reservations/recent - latest reservations first
    @GetMapping("/recent")
    public ResponseEntity<List<Reservation>> getRecentReservations() {
        log.info("Fetching recent reservations");
        List<Reservation> reservations = reservationService.getRecentReservations();
        log.info("Retrieved {} recent reservations", reservations.size());
        return ResponseEntity.ok(reservations);
    }

    // Error response wrapper
    private static class ErrorResponse {
        private final String error;
        private final long timestamp;

        public ErrorResponse(String error) {
            this.error = error;
            this.timestamp = System.currentTimeMillis();
        }

        public String getError() {
            return error;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    // Success response wrapper
    private static class SuccessResponse {
        private final String message;
        private final long timestamp;

        public SuccessResponse(String message) {
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        public String getMessage() {
            return message;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
