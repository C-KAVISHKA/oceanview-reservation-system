package com.oceanview.reservation.controller;

import com.oceanview.reservation.model.Reservation;
import com.oceanview.reservation.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

// Integration tests for the reservation REST endpoints
@WebMvcTest(ReservationController.class)
@DisplayName("ReservationController Integration Tests")
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    private Reservation sampleReservation;

    @BeforeEach
    void setUp() {
        sampleReservation = new Reservation();
        sampleReservation.setId(1L);
        sampleReservation.setGuestFullName("John Smith");
        sampleReservation.setAddress("123 Ocean Drive, Miami, FL 33139");
        sampleReservation.setContactNumber("+1234567890");
        sampleReservation.setEmail("john.smith@example.com");
        sampleReservation.setRoomType("DOUBLE");
        sampleReservation.setCheckIn(LocalDate.of(2026, 8, 15));
        sampleReservation.setCheckOut(LocalDate.of(2026, 8, 18));
        sampleReservation.setNumberOfGuests(2);
        sampleReservation.setSpecialRequests("Ocean view preferred");
        sampleReservation.setStatus("CONFIRMED");
        sampleReservation.setTotalAmount(new BigDecimal("508.50"));
        sampleReservation.setCreatedAt(LocalDateTime.now());
        sampleReservation.setUpdatedAt(LocalDateTime.now());
    }

    // IT-003: Test successful reservation creation
    @Test
    @WithMockUser
    @DisplayName("IT-003: POST /api/reservations - Success")
    void testCreateReservationSuccess() throws Exception {
        when(reservationService.create(any(Reservation.class))).thenReturn(sampleReservation);

        String requestJson = """
            {
              "guestFullName": "John Smith",
              "address": "123 Ocean Drive, Miami, FL 33139",
              "contactNumber": "+1234567890",
              "email": "john.smith@example.com",
              "roomType": "DOUBLE",
              "checkIn": "2026-08-15",
              "checkOut": "2026-08-18",
              "numberOfGuests": 2,
              "specialRequests": "Ocean view preferred"
            }
            """;

        mockMvc.perform(post("/api/reservations")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.guestFullName").value("John Smith"))
            .andExpect(jsonPath("$.roomType").value("DOUBLE"));
    }

    // IT-004: Test validation errors
    @Test
    @WithMockUser
    @DisplayName("IT-004: POST /api/reservations - Validation Error")
    void testCreateReservationValidationError() throws Exception {
        String invalidJson = """
            {
              "guestFullName": "",
              "email": "invalid-email",
              "contactNumber": "",
              "address": "",
              "roomType": "INVALID",
              "checkIn": "2020-01-01",
              "checkOut": "2020-01-01",
              "numberOfGuests": 0
            }
            """;

        mockMvc.perform(post("/api/reservations")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidJson))
            .andExpect(status().isBadRequest());
    }

    // IT-005: Test get all reservations
    @Test
    @WithMockUser
    @DisplayName("IT-005: GET /api/reservations - Success")
    void testGetAllReservations() throws Exception {
        when(reservationService.listAll()).thenReturn(Arrays.asList(sampleReservation));

        mockMvc.perform(get("/api/reservations"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].guestFullName").value("John Smith"));
    }

    // IT-006: Test get reservation by ID (found)
    @Test
    @WithMockUser
    @DisplayName("IT-006: GET /api/reservations/{id} - Success")
    void testGetReservationByIdSuccess() throws Exception {
        when(reservationService.getById(1L)).thenReturn(Optional.of(sampleReservation));

        mockMvc.perform(get("/api/reservations/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.guestFullName").value("John Smith"))
            .andExpect(jsonPath("$.email").value("john.smith@example.com"));
    }

    // IT-007: Test get reservation by ID (not found)
    @Test
    @WithMockUser
    @DisplayName("IT-007: GET /api/reservations/{id} - Not Found")
    void testGetReservationByIdNotFound() throws Exception {
        when(reservationService.getById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reservations/999"))
            .andExpect(status().isNotFound());
    }

    // IT-008: Test update reservation
    @Test
    @WithMockUser
    @DisplayName("IT-008: PUT /api/reservations/{id} - Success")
    void testUpdateReservationSuccess() throws Exception {
        sampleReservation.setGuestFullName("John Smith Updated");
        when(reservationService.update(eq(1L), any(Reservation.class))).thenReturn(sampleReservation);

        String updateJson = """
            {
              "guestFullName": "John Smith Updated",
              "address": "123 Ocean Drive, Miami, FL 33139",
              "contactNumber": "+1234567890",
              "email": "john.smith@example.com",
              "roomType": "DOUBLE",
              "checkIn": "2026-08-15",
              "checkOut": "2026-08-18",
              "numberOfGuests": 2
            }
            """;

        mockMvc.perform(put("/api/reservations/1")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.guestFullName").value("John Smith Updated"));
    }

    // IT-009: Test delete reservation
    @Test
    @WithMockUser
    @DisplayName("IT-009: DELETE /api/reservations/{id} - Success")
    void testDeleteReservationSuccess() throws Exception {
        doNothing().when(reservationService).delete(1L);

        mockMvc.perform(delete("/api/reservations/1")
            .with(csrf()))
            .andExpect(status().isOk());
    }

    // Test get reservations by status
    @Test
    @WithMockUser
    @DisplayName("GET /api/reservations/status/CONFIRMED - Filter by status")
    void testFilterReservationsByStatus() throws Exception {
        when(reservationService.getByStatus("CONFIRMED")).thenReturn(Arrays.asList(sampleReservation));

        mockMvc.perform(get("/api/reservations/status/CONFIRMED"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].status").value("CONFIRMED"));
    }

    // Test search by guest name
    @Test
    @WithMockUser
    @DisplayName("GET /api/reservations?guestName=John - Search by name")
    void testSearchByGuestName() throws Exception {
        when(reservationService.searchByGuest("John")).thenReturn(Arrays.asList(sampleReservation));

        mockMvc.perform(get("/api/reservations").param("guestName", "John"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].guestFullName").value("John Smith"));
    }
}
