package com.oceanview.reservation.service;

import com.oceanview.reservation.model.Reservation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for billing calculations
@DisplayName("BillingService Unit Tests")
public class BillingServiceTest {

    private BillingService billingService;

    @BeforeEach
    void setUp() {
        billingService = new BillingService();
    }

    // UT-001: Test standard reservation billing calculation
    // Expected: DOUBLE room, 3 nights = LKR 152,550.00 total
    @Test
    @DisplayName("UT-001: Calculate bill for standard reservation")
    void testCalculateBillForStandardReservation() {
        // Arrange: Create reservation with DOUBLE room, 3 nights
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setGuestFullName("John Smith");
        reservation.setRoomType("DOUBLE");
        reservation.setCheckIn(LocalDate.of(2026, 6, 1));
        reservation.setCheckOut(LocalDate.of(2026, 6, 4));
        reservation.setNumberOfGuests(2);
        
        // Act: Calculate billing details
        BillingService.BillDetails billDetails = billingService.calculate(reservation);
        
        // Assert: Verify calculations
        assertEquals(1L, billDetails.getReservationId());
        assertEquals("John Smith", billDetails.getGuestName());
        assertEquals("DOUBLE", billDetails.getRoomType());
        assertEquals(3, billDetails.getNumberOfNights());
        assertEquals(new BigDecimal("45000.00"), billDetails.getRoomRatePerNight());
        assertEquals(new BigDecimal("135000.00"), billDetails.getRoomSubtotal());
        assertEquals(new BigDecimal("6750.00"), billDetails.getServiceCharge());
        assertEquals(new BigDecimal("10800.00"), billDetails.getTax());
        assertEquals(new BigDecimal("152550.00"), billDetails.getGrandTotal());
    }

    // UT-002: Test single night billing calculation
    // Expected: SINGLE room, 1 night = LKR 33,900.00 total
    @Test
    @DisplayName("UT-002: Calculate bill for single night reservation")
    void testCalculateBillForSingleNight() {
        // Arrange: Create reservation with SINGLE room, 1 night
        Reservation reservation = new Reservation();
        reservation.setId(2L);
        reservation.setGuestFullName("Jane Doe");
        reservation.setRoomType("SINGLE");
        reservation.setCheckIn(LocalDate.of(2026, 7, 15));
        reservation.setCheckOut(LocalDate.of(2026, 7, 16));
        reservation.setNumberOfGuests(1);
        
        // Act: Calculate billing details
        BillingService.BillDetails billDetails = billingService.calculate(reservation);
        
        // Assert: Verify calculations
        assertEquals(1, billDetails.getNumberOfNights());
        assertEquals(new BigDecimal("30000.00"), billDetails.getRoomRatePerNight());
        assertEquals(new BigDecimal("30000.00"), billDetails.getRoomSubtotal());
        assertEquals(new BigDecimal("1500.00"), billDetails.getServiceCharge());
        assertEquals(new BigDecimal("2400.00"), billDetails.getTax());
        assertEquals(new BigDecimal("33900.00"), billDetails.getGrandTotal());
    }

    // UT-003: Test extended stay billing calculation
    // Expected: DELUXE room, 10 nights = LKR 1,356,000.00 total
    @Test
    @DisplayName("UT-003: Calculate bill for extended stay")
    void testCalculateBillForExtendedStay() {
        // Arrange: Create reservation with DELUXE room, 10 nights
        Reservation reservation = new Reservation();
        reservation.setId(3L);
        reservation.setGuestFullName("Bob Johnson");
        reservation.setRoomType("DELUXE");
        reservation.setCheckIn(LocalDate.of(2026, 8, 1));
        reservation.setCheckOut(LocalDate.of(2026, 8, 11));
        reservation.setNumberOfGuests(4);
        
        // Act: Calculate billing details
        BillingService.BillDetails billDetails = billingService.calculate(reservation);
        
        // Assert: Verify calculations
        assertEquals(10, billDetails.getNumberOfNights());
        assertEquals(new BigDecimal("120000.00"), billDetails.getRoomRatePerNight());
        assertEquals(new BigDecimal("1200000.00"), billDetails.getRoomSubtotal());
        assertEquals(new BigDecimal("60000.00"), billDetails.getServiceCharge());
        assertEquals(new BigDecimal("96000.00"), billDetails.getTax());
        assertEquals(new BigDecimal("1356000.00"), billDetails.getGrandTotal());
    }

    // UT-004: Test number of nights calculation
    // Expected: 2026-03-15 to 2026-03-18 = 3 nights
    @Test
    @DisplayName("UT-004: Calculate number of nights between dates")
    void testCalculateNumberOfNights() {
        // Arrange: Create reservation
        Reservation reservation = new Reservation();
        reservation.setId(4L);
        reservation.setGuestFullName("Alice Brown");
        reservation.setRoomType("SUITE");
        reservation.setCheckIn(LocalDate.of(2026, 3, 15));
        reservation.setCheckOut(LocalDate.of(2026, 3, 18));
        reservation.setNumberOfGuests(2);
        
        // Act: Calculate nights (using calculateNumberOfNights method)
        long nights = billingService.calculateNumberOfNights(
            reservation.getCheckIn(), 
            reservation.getCheckOut()
        );
        
        // Assert: Verify 3 nights
        assertEquals(3, nights);
        
        // Also verify via BillDetails
        BillingService.BillDetails billDetails = billingService.calculate(reservation);
        assertEquals(3, billDetails.getNumberOfNights());
    }

    // UT-005: Test SUITE room type calculation
    @Test
    @DisplayName("UT-005: Calculate bill for SUITE room")
    void testCalculateBillForSuite() {
        // Arrange: Create reservation with SUITE room, 5 nights
        Reservation reservation = new Reservation();
        reservation.setId(5L);
        reservation.setGuestFullName("Sarah Johnson");
        reservation.setRoomType("SUITE");
        reservation.setCheckIn(LocalDate.of(2026, 7, 15));
        reservation.setCheckOut(LocalDate.of(2026, 7, 20));
        reservation.setNumberOfGuests(2);
        
        // Act: Calculate billing details
        BillingService.BillDetails billDetails = billingService.calculate(reservation);
        
        // Assert: Verify calculations
        assertEquals(5, billDetails.getNumberOfNights());
        assertEquals(new BigDecimal("75000.00"), billDetails.getRoomRatePerNight());
        assertEquals(new BigDecimal("375000.00"), billDetails.getRoomSubtotal());
        assertEquals(new BigDecimal("18750.00"), billDetails.getServiceCharge());
        assertEquals(new BigDecimal("30000.00"), billDetails.getTax());
        assertEquals(new BigDecimal("423750.00"), billDetails.getGrandTotal());
    }

    // UT-006: Test decimal precision for billing calculations
    @Test
    @DisplayName("UT-006: Should maintain proper decimal precision")
    void testDecimalPrecision() {
        // Arrange: Create reservation that will produce decimal values
        Reservation reservation = new Reservation();
        reservation.setId(6L);
        reservation.setGuestFullName("Test User");
        reservation.setRoomType("DOUBLE");
        reservation.setCheckIn(LocalDate.of(2026, 9, 1));
        reservation.setCheckOut(LocalDate.of(2026, 9, 4));
        reservation.setNumberOfGuests(2);
        
        // Act: Calculate billing details
        BillingService.BillDetails billDetails = billingService.calculate(reservation);
        
        // Assert: All amounts should have exactly 2 decimal places
        assertEquals(2, billDetails.getRoomRatePerNight().scale());
        assertEquals(2, billDetails.getRoomSubtotal().scale());
        assertEquals(2, billDetails.getServiceCharge().scale());
        assertEquals(2, billDetails.getTax().scale());
        assertEquals(2, billDetails.getGrandTotal().scale());
        
        // Verify grand total is sum of parts
        BigDecimal expectedTotal = billDetails.getRoomSubtotal()
                .add(billDetails.getServiceCharge())
                .add(billDetails.getTax());
        assertEquals(expectedTotal, billDetails.getGrandTotal());
    }

    // UT-007: Test tax and service charge percentages
    @Test
    @DisplayName("UT-007: Verify tax and service charge rates")
    void testTaxAndServiceChargeRates() {
        // Act: Get rates
        int taxRate = billingService.getTaxPercentage();
        int serviceChargeRate = billingService.getServiceChargePercentage();
        
        // Assert: Verify expected rates
        assertEquals(8, taxRate);
        assertEquals(5, serviceChargeRate);
    }

    // UT-008: Test all room rates
    @Test
    @DisplayName("UT-008: Verify all room rate mappings")
    void testGetAllRoomRates() {
        // Act: Get all room rates
        var roomRates = billingService.getAllRoomRates();
        
        // Assert: Verify all room types are present with correct rates
        assertEquals(4, roomRates.size());
        assertEquals(new BigDecimal("30000.00"), roomRates.get("SINGLE"));
        assertEquals(new BigDecimal("45000.00"), roomRates.get("DOUBLE"));
        assertEquals(new BigDecimal("75000.00"), roomRates.get("SUITE"));
        assertEquals(new BigDecimal("120000.00"), roomRates.get("DELUXE"));
    }

    // UT-009: Test invalid room type
    @Test
    @DisplayName("UT-009: Should throw exception for invalid room type")
    void testInvalidRoomType() {
        // Arrange: Create reservation with invalid room type
        Reservation reservation = new Reservation();
        reservation.setId(7L);
        reservation.setGuestFullName("Invalid Guest");
        reservation.setRoomType("INVALID_TYPE");
        reservation.setCheckIn(LocalDate.of(2026, 10, 1));
        reservation.setCheckOut(LocalDate.of(2026, 10, 2));
        reservation.setNumberOfGuests(1);
        
        // Act & Assert: Should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            billingService.calculate(reservation);
        });
    }

    // UT-010: Test same-day checkout (0 nights)
    @Test
    @DisplayName("UT-010: Should throw exception for same-day checkout")
    void testSameDayCheckout() {
        // Arrange: Create reservation with same check-in and check-out
        Reservation reservation = new Reservation();
        reservation.setId(8L);
        reservation.setGuestFullName("Same Day Guest");
        reservation.setRoomType("SINGLE");
        reservation.setCheckIn(LocalDate.of(2026, 11, 1));
        reservation.setCheckOut(LocalDate.of(2026, 11, 1));
        reservation.setNumberOfGuests(1);
        
        // Act & Assert: Should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            billingService.calculate(reservation);
        });
    }
}

