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

// Handles billing API requests - generates bills for reservations
@RestController
@RequestMapping("/api/billing")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class BillingController {

    private final ReservationService reservationService;
    private final BillingService billingService;

    // GET /api/billing/{id} - get billing details for a reservation
    @GetMapping("/{id}")
    public ResponseEntity<?> getBillingDetails(@PathVariable Long id) {
        log.info("Fetching billing for reservation {}", id);

        try {
            Optional<Reservation> reservationOpt = reservationService.getById(id);

            if (reservationOpt.isEmpty()) {
                log.warn("Reservation not found for billing: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Reservation not found with ID: " + id));
            }

            Reservation reservation = reservationOpt.get();
            BillingService.BillDetails billDetails = billingService.calculate(reservation);

            log.info("Bill generated for reservation {}: ${}", id, billDetails.getGrandTotal());
            return ResponseEntity.ok(billDetails);
            
        } catch (IllegalArgumentException e) {
            log.error("Validation error for reservation {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Error calculating billing for reservation {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to calculate billing: " + e.getMessage()));
        }
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
}
