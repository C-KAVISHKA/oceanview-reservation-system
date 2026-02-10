package com.oceanview.reservation.repository;

import com.oceanview.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Reservation entity.
 * 
 * Provides CRUD operations and custom query methods for managing reservations.
 * Spring Data JPA automatically implements this interface at runtime.
 * 
 * @author Enzo
 * @version 1.0.0
 * @since 2026-02-10
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /**
     * Find reservations by guest name (case-insensitive, partial match).
     * 
     * @param name partial or full guest name
     * @return list of matching reservations
     */
    List<Reservation> findByGuestFullNameContainingIgnoreCase(String name);

    /**
     * Find reservations by email address.
     * 
     * @param email guest email address
     * @return list of reservations for this email
     */
    List<Reservation> findByEmail(String email);

    /**
     * Find reservations by status.
     * 
     * @param status reservation status (PENDING, CONFIRMED, CANCELLED, COMPLETED)
     * @return list of reservations with this status
     */
    List<Reservation> findByStatus(String status);

    /**
     * Find reservations by room type.
     * 
     * @param roomType room type (SINGLE, DOUBLE, SUITE, DELUXE)
     * @return list of reservations for this room type
     */
    List<Reservation> findByRoomType(String roomType);

    /**
     * Find reservations by check-in date.
     * 
     * @param checkIn check-in date
     * @return list of reservations with this check-in date
     */
    @Query("SELECT r FROM Reservation r WHERE r.checkIn = :checkIn")
    List<Reservation> findByCheckIn(@Param("checkIn") LocalDate checkIn);

    /**
     * Find reservations by check-in date range.
     * 
     * @param startDate start of date range
     * @param endDate end of date range
     * @return list of reservations checking in within this range
     */
    @Query("SELECT r FROM Reservation r WHERE r.checkIn BETWEEN :startDate AND :endDate")
    List<Reservation> findByCheckInBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Find reservations by check-out date range.
     * 
     * @param startDate start of date range
     * @param endDate end of date range
     * @return list of reservations checking out within this range
     */
    @Query("SELECT r FROM Reservation r WHERE r.checkOut BETWEEN :startDate AND :endDate")
    List<Reservation> findByCheckOutBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Find active reservations (currently occupied).
     * Custom query to find reservations where current date is between check-in and check-out.
     * 
     * @param currentDate current date
     * @return list of active reservations
     */
    @Query("SELECT r FROM Reservation r WHERE r.checkIn <= :currentDate AND r.checkOut > :currentDate AND r.status = 'CONFIRMED'")
    List<Reservation> findActiveReservations(@Param("currentDate") LocalDate currentDate);

    /**
     * Find upcoming reservations (check-in date in the future).
     * 
     * @param currentDate current date
     * @return list of upcoming reservations
     */
    @Query("SELECT r FROM Reservation r WHERE r.checkIn > :currentDate AND r.status IN ('PENDING', 'CONFIRMED') ORDER BY r.checkIn ASC")
    List<Reservation> findUpcomingReservations(@Param("currentDate") LocalDate currentDate);

    /**
     * Find reservations by guest contact number.
     * 
     * @param contactNumber guest contact number
     * @return list of reservations for this contact number
     */
    List<Reservation> findByContactNumber(String contactNumber);

    /**
     * Check if a room type is available for a given date range.
     * Custom query to check for overlapping reservations.
     * 
     * @param roomType room type to check
     * @param checkIn desired check-in date
     * @param checkOut desired check-out date
     * @return count of conflicting reservations (0 means available)
     */
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.roomType = :roomType " +
           "AND r.status IN ('PENDING', 'CONFIRMED') " +
           "AND NOT (r.checkOut <= :checkIn OR r.checkIn >= :checkOut)")
    long countConflictingReservations(@Param("roomType") String roomType, 
                                       @Param("checkIn") LocalDate checkIn, 
                                       @Param("checkOut") LocalDate checkOut);

    /**
     * Find reservations created within a date range (for reporting).
     * 
     * @param startDate start of date range
     * @param endDate end of date range
     * @return list of reservations created in this period
     */
    @Query("SELECT r FROM Reservation r WHERE r.createdAt >= :startDate AND r.createdAt < :endDate ORDER BY r.createdAt DESC")
    List<Reservation> findReservationsCreatedBetween(@Param("startDate") LocalDate startDate, 
                                                      @Param("endDate") LocalDate endDate);

    /**
     * Find the most recent reservations (for dashboard display).
     * 
     * @return list of recent reservations ordered by creation date
     */
    @Query("SELECT r FROM Reservation r ORDER BY r.createdAt DESC")
    List<Reservation> findAllOrderByCreatedAtDesc();

    // TODO: Add method to calculate total revenue for a date range
    // @Query("SELECT SUM(r.totalAmount) FROM Reservation r WHERE r.checkIn BETWEEN :start AND :end AND r.status = 'CONFIRMED'")
    // BigDecimal calculateRevenueForPeriod(@Param("start") LocalDate start, @Param("end") LocalDate end);

    // TODO: Add method to find reservations by multiple statuses
    // List<Reservation> findByStatusIn(List<String> statuses);

    // TODO: Add method to count reservations by room type for occupancy statistics
    // long countByRoomTypeAndStatus(String roomType, String status);
}
