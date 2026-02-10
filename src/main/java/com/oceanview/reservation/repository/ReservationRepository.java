package com.oceanview.reservation.repository;

import com.oceanview.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

// Database access layer for reservations - Spring Data JPA handles the implementation
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Search by guest name (case-insensitive partial match)
    List<Reservation> findByGuestFullNameContainingIgnoreCase(String name);

    // Find by email
    List<Reservation> findByEmail(String email);

    // Find by status (PENDING, CONFIRMED, CANCELLED, etc.)
    List<Reservation> findByStatus(String status);

    // Find by room type
    List<Reservation> findByRoomType(String roomType);

    // Find reservations for a specific check-in date
    @Query("SELECT r FROM Reservation r WHERE r.checkIn = :checkIn")
    List<Reservation> findByCheckIn(@Param("checkIn") LocalDate checkIn);

    // Find reservations checking in within a date range
    @Query("SELECT r FROM Reservation r WHERE r.checkIn BETWEEN :startDate AND :endDate")
    List<Reservation> findByCheckInBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Find reservations checking out within a date range
    @Query("SELECT r FROM Reservation r WHERE r.checkOut BETWEEN :startDate AND :endDate")
    List<Reservation> findByCheckOutBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Find currently occupied rooms (check-in <= today < check-out)
    @Query("SELECT r FROM Reservation r WHERE r.checkIn <= :currentDate AND r.checkOut > :currentDate AND r.status = 'CONFIRMED'")
    List<Reservation> findActiveReservations(@Param("currentDate") LocalDate currentDate);

    // Find future reservations
    @Query("SELECT r FROM Reservation r WHERE r.checkIn > :currentDate AND r.status IN ('PENDING', 'CONFIRMED') ORDER BY r.checkIn ASC")
    List<Reservation> findUpcomingReservations(@Param("currentDate") LocalDate currentDate);

    // Find by phone number
    List<Reservation> findByContactNumber(String contactNumber);

    // Check for double-booking (overlapping dates for same room type)
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.roomType = :roomType " +
           "AND r.status IN ('PENDING', 'CONFIRMED') " +
           "AND NOT (r.checkOut <= :checkIn OR r.checkIn >= :checkOut)")
    long countConflictingReservations(@Param("roomType") String roomType,
                                       @Param("checkIn") LocalDate checkIn,
                                       @Param("checkOut") LocalDate checkOut);

    // Find reservations created within a date range (for reporting)
    @Query("SELECT r FROM Reservation r WHERE r.createdAt >= :startDate AND r.createdAt < :endDate ORDER BY r.createdAt DESC")
    List<Reservation> findReservationsCreatedBetween(@Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);

    // Get all reservations ordered by newest first
    @Query("SELECT r FROM Reservation r ORDER BY r.createdAt DESC")
    List<Reservation> findAllOrderByCreatedAtDesc();
}
