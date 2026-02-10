package com.oceanview.reservation.controller;

import com.oceanview.reservation.model.Reservation;
import com.oceanview.reservation.service.BillingService;
import com.oceanview.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * REST Controller for Billing operations.
 * 
 * Provides API endpoints for generating bills and calculating charges
 * for reservations.
 * 
 * @author Enzo
 * @version 1.0.0
 * @since 2026-02-10
 */
@RestController
@RequestMapping("/api/billing")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class BillingController {

    private final ReservationService reservationService;
    private final BillingService billingService;

    /**
     * Get billing details for a reservation.
     * 
     * GET /api/billing/{id}
     * 
     * Retrieves the reservation by ID and calculates complete billing details
     * including room charges, service charges, taxes, and grand total.
     * 
     * @param id reservation ID
     * @return billing details with 200 status, or 404 if reservation not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBillingDetails(@PathVariable Long id) {
        log.info("GET /api/billing/{} - Fetching billing details", id);
        
        try {
            // Load reservation by ID
            Optional<Reservation> reservationOpt = reservationService.getById(id);
            
            if (reservationOpt.isEmpty()) {
                log.warn("Reservation not found for billing: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Reservation not found with ID: " + id));
            }
            
            Reservation reservation = reservationOpt.get();
            log.debug("Reservation found: {} - {}", id, reservation.getGuestFullName());
            
            // Calculate billing details
            BillingService.BillDetails billDetails = billingService.calculate(reservation);
            
            log.info("Billing calculated successfully for reservation {}: Total ${}", 
                    id, billDetails.getGrandTotal());
            
            return ResponseEntity.ok(billDetails);
            
        } catch (IllegalArgumentException e) {
            log.error("Validation error calculating billing for reservation {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error calculating billing for reservation {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to calculate billing: " + e.getMessage()));
        }
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
}
