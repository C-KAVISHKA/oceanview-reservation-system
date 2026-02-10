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

// Calculates bills for reservations - room charges, service charge (5%), tax (8%)
@Service
@Slf4j
public class BillingService {

    // Room rates per night
    private static final BigDecimal RATE_SINGLE = new BigDecimal("100.00");
    private static final BigDecimal RATE_DOUBLE = new BigDecimal("150.00");
    private static final BigDecimal RATE_SUITE = new BigDecimal("250.00");
    private static final BigDecimal RATE_DELUXE = new BigDecimal("400.00");

    private static final BigDecimal TAX_RATE = new BigDecimal("0.08");
    private static final BigDecimal SERVICE_CHARGE_RATE = new BigDecimal("0.05");
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final int CURRENCY_SCALE = 2;

    // Generate a full bill breakdown for a reservation
    public BillDetails calculate(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("Reservation cannot be null");
        }
        log.info("Calculating bill for reservation {}", reservation.getId());
        
        if (reservation.getCheckIn() == null || reservation.getCheckOut() == null) {
            throw new IllegalArgumentException("Check-in and check-out dates are required");
        }
        if (reservation.getRoomType() == null || reservation.getRoomType().isEmpty()) {
            throw new IllegalArgumentException("Room type is required");
        }

        long numberOfNights = calculateNumberOfNights(reservation.getCheckIn(), reservation.getCheckOut());
        
        if (numberOfNights <= 0) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        BigDecimal roomRatePerNight = getRoomRate(reservation.getRoomType());

        BigDecimal roomSubtotal = roomRatePerNight
                .multiply(BigDecimal.valueOf(numberOfNights))
                .setScale(CURRENCY_SCALE, ROUNDING_MODE);

        BigDecimal serviceCharge = roomSubtotal
                .multiply(SERVICE_CHARGE_RATE)
                .setScale(CURRENCY_SCALE, ROUNDING_MODE);

        BigDecimal tax = roomSubtotal
                .multiply(TAX_RATE)
                .setScale(CURRENCY_SCALE, ROUNDING_MODE);

        BigDecimal grandTotal = roomSubtotal
                .add(serviceCharge)
                .add(tax)
                .setScale(CURRENCY_SCALE, ROUNDING_MODE);

        // Build the bill details response
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

        log.info("Bill total for reservation {}: ${}", reservation.getId(), grandTotal);

        return billDetails;
    }

    // Calculate nights between two dates
    public long calculateNumberOfNights(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("Check-in and check-out dates cannot be null");
        }
        
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return nights;
    }

    // Get the nightly rate for a room type
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
                throw new IllegalArgumentException("Unknown room type: " + roomType);
            }
        };
    }

    // Get all room rates as a map
    public Map<String, BigDecimal> getAllRoomRates() {
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("SINGLE", RATE_SINGLE);
        rates.put("DOUBLE", RATE_DOUBLE);
        rates.put("SUITE", RATE_SUITE);
        rates.put("DELUXE", RATE_DELUXE);
        return rates;
    }

    public int getTaxPercentage() {
        return TAX_RATE.multiply(new BigDecimal("100")).intValue();
    }

    public int getServiceChargePercentage() {
        return SERVICE_CHARGE_RATE.multiply(new BigDecimal("100")).intValue();
    }

    // Bill details DTO - holds the complete breakdown for a reservation
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
        private int serviceChargeRate;
        private BigDecimal tax;
        private int taxRate;
        private BigDecimal grandTotal;
    }
}
