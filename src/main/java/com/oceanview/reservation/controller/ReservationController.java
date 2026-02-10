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

/**
 * REST Controller for Reservation operations.
 * 
 * Provides RESTful API endpoints for managing hotel reservations.
 * Base path: /api/reservations
 * 
 * @author Enzo
 * @version 1.0.0
 * @since 2026-02-10
 */
@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * Create a new reservation.
     * 
     * POST /api/reservations
     * 
     * @param reservation reservation data to create
     * @return created reservation with 201 status, or 400 if validation fails
     */
    @PostMapping
    public ResponseEntity<?> createReservation(@Valid @RequestBody Reservation reservation) {
        log.info("POST /api/reservations - Creating reservation for guest: {}", reservation.getGuestFullName());
        
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

    /**
     * Get reservation by ID.
     * 
     * GET /api/reservations/{id}
     * 
     * @param id reservation ID
     * @return reservation with 200 status, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable Long id) {
        log.info("GET /api/reservations/{} - Fetching reservation", id);
        
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

    /**
     * Get all reservations or search by guest name.
     * 
     * GET /api/reservations
     * GET /api/reservations?guestName=John
     * 
     * @param guestName optional guest name filter
     * @return list of reservations with 200 status
     */
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations(
            @RequestParam(required = false) String guestName) {
        
        if (guestName != null && !guestName.trim().isEmpty()) {
            log.info("GET /api/reservations?guestName={} - Searching by guest name", guestName);
            List<Reservation> reservations = reservationService.searchByGuest(guestName);
            log.info("Found {} reservations matching guest name", reservations.size());
            return ResponseEntity.ok(reservations);
        } else {
            log.info("GET /api/reservations - Fetching all reservations");
            List<Reservation> reservations = reservationService.listAll();
            log.info("Retrieved {} total reservations", reservations.size());
            return ResponseEntity.ok(reservations);
        }
    }

    /**
     * Update an existing reservation.
     * 
     * PUT /api/reservations/{id}
     * 
     * @param id reservation ID to update
     * @param reservation updated reservation data
     * @return updated reservation with 200 status, or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReservation(
            @PathVariable Long id,
            @Valid @RequestBody Reservation reservation) {
        
        log.info("PUT /api/reservations/{} - Updating reservation", id);
        
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

    /**
     * Delete a reservation by ID.
     * 
     * DELETE /api/reservations/{id}
     * 
     * @param id reservation ID to delete
     * @return 200 status with success message, or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        log.info("DELETE /api/reservations/{} - Deleting reservation", id);
        
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

    /**
     * Get reservations by status.
     * 
     * GET /api/reservations/status/{status}
     * 
     * @param status reservation status (PENDING, CONFIRMED, CANCELLED, COMPLETED)
     * @return list of reservations with given status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Reservation>> getReservationsByStatus(@PathVariable String status) {
        log.info("GET /api/reservations/status/{} - Fetching by status", status);
        List<Reservation> reservations = reservationService.getByStatus(status.toUpperCase());
        log.info("Found {} reservations with status {}", reservations.size(), status);
        return ResponseEntity.ok(reservations);
    }

    /**
     * Get active reservations (currently occupied).
     * 
     * GET /api/reservations/active
     * 
     * @return list of active reservations
     */
    @GetMapping("/active")
    public ResponseEntity<List<Reservation>> getActiveReservations() {
        log.info("GET /api/reservations/active - Fetching active reservations");
        List<Reservation> reservations = reservationService.getActiveReservations();
        log.info("Found {} active reservations", reservations.size());
        return ResponseEntity.ok(reservations);
    }

    /**
     * Get upcoming reservations.
     * 
     * GET /api/reservations/upcoming
     * 
     * @return list of upcoming reservations
     */
    @GetMapping("/upcoming")
    public ResponseEntity<List<Reservation>> getUpcomingReservations() {
        log.info("GET /api/reservations/upcoming - Fetching upcoming reservations");
        List<Reservation> reservations = reservationService.getUpcomingReservations();
        log.info("Found {} upcoming reservations", reservations.size());
        return ResponseEntity.ok(reservations);
    }

    /**
     * Get recent reservations ordered by creation date.
     * 
     * GET /api/reservations/recent
     * 
     * @return list of recent reservations
     */
    @GetMapping("/recent")
    public ResponseEntity<List<Reservation>> getRecentReservations() {
        log.info("GET /api/reservations/recent - Fetching recent reservations");
        List<Reservation> reservations = reservationService.getRecentReservations();
        log.info("Retrieved {} recent reservations", reservations.size());
        return ResponseEntity.ok(reservations);
    }

    /**
     * Error response wrapper class.
     */
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

    /**
     * Success response wrapper class.
     */
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
