package com.oceanview.reservation.service;

import com.oceanview.reservation.model.Reservation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for billing and payment calculations.
 * 
 * Handles all billing-related operations including room rate calculations,
 * tax computations, service charges, and total bill generation.
 * 
 * @author Enzo
 * @version 1.0.0
 * @since 2026-02-10
 */
@Service
@Slf4j
public class BillingService {

    // Room rates per night (in dollars)
    private static final BigDecimal RATE_SINGLE = new BigDecimal("100.00");
    private static final BigDecimal RATE_DOUBLE = new BigDecimal("150.00");
    private static final BigDecimal RATE_SUITE = new BigDecimal("250.00");
    private static final BigDecimal RATE_DELUXE = new BigDecimal("400.00");

    // Tax rate (8%)
    private static final BigDecimal TAX_RATE = new BigDecimal("0.08");

    // Service charge rate (5%)
    private static final BigDecimal SERVICE_CHARGE_RATE = new BigDecimal("0.05");

    // Rounding mode for currency calculations
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    // Decimal scale for currency (2 decimal places)
    private static final int CURRENCY_SCALE = 2;

    /**
     * Calculate complete billing details for a reservation.
     * 
     * This method performs the following calculations:
     * 1. Determine number of nights (check-out date - check-in date)
     * 2. Look up room rate based on room type
     * 3. Calculate room subtotal (nights × rate)
     * 4. Calculate service charge (5% of room subtotal)
     * 5. Calculate tax (8% of room subtotal)
     * 6. Calculate grand total (room subtotal + service charge + tax)
     * 
     * All monetary values are rounded to 2 decimal places using HALF_UP rounding.
     * 
     * @param reservation reservation to calculate billing for
     * @return BillDetails containing complete billing breakdown
     * @throws IllegalArgumentException if reservation data is invalid
     */
    public BillDetails calculate(Reservation reservation) {
        log.info("Calculating billing for reservation ID: {}", reservation.getId());
        
        // Validate input
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }
        if (reservation.getCheckIn() == null || reservation.getCheckOut() == null) {
            throw new IllegalArgumentException("Check-in and check-out dates are required");
        }
        if (reservation.getRoomType() == null || reservation.getRoomType().isEmpty()) {
            throw new IllegalArgumentException("Room type is required");
        }

        // Step 1: Calculate number of nights
        long numberOfNights = calculateNumberOfNights(reservation.getCheckIn(), reservation.getCheckOut());
        log.debug("Number of nights: {}", numberOfNights);
        
        if (numberOfNights <= 0) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        // Step 2: Get room rate per night based on room type
        BigDecimal roomRatePerNight = getRoomRate(reservation.getRoomType());
        log.debug("Room rate per night for {}: ${}", reservation.getRoomType(), roomRatePerNight);

        // Step 3: Calculate room subtotal (nights × rate per night)
        BigDecimal roomSubtotal = roomRatePerNight
                .multiply(BigDecimal.valueOf(numberOfNights))
                .setScale(CURRENCY_SCALE, ROUNDING_MODE);
        log.debug("Room subtotal: ${}", roomSubtotal);

        // Step 4: Calculate service charge (5% of room subtotal)
        BigDecimal serviceCharge = roomSubtotal
                .multiply(SERVICE_CHARGE_RATE)
                .setScale(CURRENCY_SCALE, ROUNDING_MODE);
        log.debug("Service charge (5%): ${}", serviceCharge);

        // Step 5: Calculate tax (8% of room subtotal)
        BigDecimal tax = roomSubtotal
                .multiply(TAX_RATE)
                .setScale(CURRENCY_SCALE, ROUNDING_MODE);
        log.debug("Tax (8%): ${}", tax);

        // Step 6: Calculate grand total
        BigDecimal grandTotal = roomSubtotal
                .add(serviceCharge)
                .add(tax)
                .setScale(CURRENCY_SCALE, ROUNDING_MODE);
        log.debug("Grand total: ${}", grandTotal);

        // Create billing details response
        BillDetails billDetails = new BillDetails();
        billDetails.setReservationId(reservation.getId());
        billDetails.setGuestName(reservation.getGuestFullName());
        billDetails.setRoomType(reservation.getRoomType());
        billDetails.setCheckInDate(reservation.getCheckIn());
        billDetails.setCheckOutDate(reservation.getCheckOut());
        billDetails.setNumberOfNights(numberOfNights);
        billDetails.setRoomRatePerNight(roomRatePerNight);
        billDetails.setRoomSubtotal(roomSubtotal);
        billDetails.setServiceCharge(serviceCharge);
        billDetails.setServiceChargeRate(SERVICE_CHARGE_RATE.multiply(new BigDecimal("100")).intValue());
        billDetails.setTax(tax);
        billDetails.setTaxRate(TAX_RATE.multiply(new BigDecimal("100")).intValue());
        billDetails.setGrandTotal(grandTotal);

        log.info("Billing calculated successfully for reservation {}: Total ${}", 
                reservation.getId(), grandTotal);

        return billDetails;
    }

    /**
     * Calculate number of nights between two dates.
     * 
     * Uses ChronoUnit.DAYS to get the precise number of days between dates.
     * This is a deterministic calculation that handles DST changes correctly.
     * 
     * @param checkIn check-in date
     * @param checkOut check-out date
     * @return number of nights (days between dates)
     */
    public long calculateNumberOfNights(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Check-in and check-out dates cannot be null");
        }
        
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        log.trace("Calculated {} nights between {} and {}", nights, checkIn, checkOut);
        
        return nights;
    }

    /**
     * Get room rate per night based on room type.
     * 
     * Room rates:
     * - SINGLE: $100.00 per night
     * - DOUBLE: $150.00 per night
     * - SUITE:  $250.00 per night
     * - DELUXE: $400.00 per night
     * 
     * @param roomType room type code
     * @return rate per night for the room type
     * @throws IllegalArgumentException if room type is unknown
     */
    public BigDecimal getRoomRate(String roomType) {
        if (roomType == null || roomType.isEmpty()) {
            throw new IllegalArgumentException("Room type cannot be null or empty");
        }

        return switch (roomType.toUpperCase()) {
            case "SINGLE" -> RATE_SINGLE;
            case "DOUBLE" -> RATE_DOUBLE;
            case "SUITE" -> RATE_SUITE;
            case "DELUXE" -> RATE_DELUXE;
            default -> {
                log.error("Unknown room type: {}", roomType);
                throw new IllegalArgumentException("Unknown room type: " + roomType + 
                        ". Valid types are: SINGLE, DOUBLE, SUITE, DELUXE");
            }
        };
    }

    /**
     * Get all available room rates as a map.
     * Useful for displaying pricing information to users.
     * 
     * @return map of room type to rate per night
     */
    public Map<String, BigDecimal> getAllRoomRates() {
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("SINGLE", RATE_SINGLE);
        rates.put("DOUBLE", RATE_DOUBLE);
        rates.put("SUITE", RATE_SUITE);
        rates.put("DELUXE", RATE_DELUXE);
        return rates;
    }

    /**
     * Get tax rate as a percentage.
     * 
     * @return tax rate (e.g., 8 for 8%)
     */
    public int getTaxPercentage() {
        return TAX_RATE.multiply(new BigDecimal("100")).intValue();
    }

    /**
     * Get service charge rate as a percentage.
     * 
     * @return service charge rate (e.g., 5 for 5%)
     */
    public int getServiceChargePercentage() {
        return SERVICE_CHARGE_RATE.multiply(new BigDecimal("100")).intValue();
    }

    /**
     * DTO representing complete billing details for a reservation.
     * 
     * This class contains all information needed for a bill including:
     * - Guest and reservation information
     * - Room details and rates
     * - Itemized charges (room, service, tax)
     * - Grand total
     */
    @Data
    public static class BillDetails {
        private Long reservationId;
        private String guestName;
        private String roomType;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private long numberOfNights;
        private BigDecimal roomRatePerNight;
        private BigDecimal roomSubtotal;
        private BigDecimal serviceCharge;
        private int serviceChargeRate; // as percentage (e.g., 5 for 5%)
        private BigDecimal tax;
        private int taxRate; // as percentage (e.g., 8 for 8%)
        private BigDecimal grandTotal;
    }
}
