package com.oceanview.reservation.service;

import com.oceanview.reservation.model.Reservation;
import com.oceanview.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for Reservation business logic.
 * 
 * Handles reservation operations including creation, retrieval, updates,
 * deletion, and validation logic such as date overlap checking.
 * 
 * @author Enzo
 * @version 1.0.0
 * @since 2026-02-10
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;

    /**
     * Create a new reservation.
     * 
     * @param reservation reservation to create
     * @return saved reservation with generated ID
     * @throws IllegalArgumentException if reservation has date overlap
     */
    public Reservation create(Reservation reservation) {
        log.info("Creating new reservation for guest: {}", reservation.getGuestFullName());
        
        // Validate date range
        if (reservation.getCheckOut().isBefore(reservation.getCheckIn()) ||
            reservation.getCheckOut().isEqual(reservation.getCheckIn())) {
            log.error("Invalid date range: check-out must be after check-in");
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
        
        // Check for overlapping reservations
        if (hasOverlap(reservation)) {
            log.error("Room type {} not available for dates {} to {}", 
                     reservation.getRoomType(), 
                     reservation.getCheckIn(), 
                     reservation.getCheckOut());
            throw new IllegalArgumentException("Room type " + reservation.getRoomType() + 
                                             " is not available for the selected dates");
        }
        
        // Set default status if not provided
        if (reservation.getStatus() == null || reservation.getStatus().isEmpty()) {
            reservation.setStatus("PENDING");
        }
        
        Reservation savedReservation = reservationRepository.save(reservation);
        log.info("Reservation created successfully with ID: {}", savedReservation.getId());
        
        return savedReservation;
    }

    /**
     * Get reservation by ID.
     * 
     * @param id reservation ID
     * @return optional containing reservation if found
     */
    @Transactional(readOnly = true)
    public Optional<Reservation> getById(Long id) {
        log.debug("Fetching reservation with ID: {}", id);
        return reservationRepository.findById(id);
    }

    /**
     * Get all reservations.
     * 
     * @return list of all reservations
     */
    @Transactional(readOnly = true)
    public List<Reservation> listAll() {
        log.debug("Fetching all reservations");
        return reservationRepository.findAll();
    }

    /**
     * Search reservations by guest name (case-insensitive, partial match).
     * 
     * @param guestName guest name or partial name
     * @return list of matching reservations
     */
    @Transactional(readOnly = true)
    public List<Reservation> searchByGuest(String guestName) {
        log.debug("Searching reservations for guest: {}", guestName);
        if (guestName == null || guestName.trim().isEmpty()) {
            return listAll();
        }
        return reservationRepository.findByGuestFullNameContainingIgnoreCase(guestName.trim());
    }

    /**
     * Update an existing reservation.
     * 
     * @param id reservation ID to update
     * @param updatedReservation reservation data to update
     * @return updated reservation
     * @throws IllegalArgumentException if reservation not found or has date overlap
     */
    public Reservation update(Long id, Reservation updatedReservation) {
        log.info("Updating reservation with ID: {}", id);
        
        Reservation existingReservation = reservationRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Reservation not found with ID: {}", id);
                return new IllegalArgumentException("Reservation not found with ID: " + id);
            });
        
        // Validate date range if dates are being updated
        if (updatedReservation.getCheckIn() != null && updatedReservation.getCheckOut() != null) {
            if (updatedReservation.getCheckOut().isBefore(updatedReservation.getCheckIn()) ||
                updatedReservation.getCheckOut().isEqual(updatedReservation.getCheckIn())) {
                log.error("Invalid date range for update");
                throw new IllegalArgumentException("Check-out date must be after check-in date");
            }
        }
        
        // Update fields
        if (updatedReservation.getGuestFullName() != null) {
            existingReservation.setGuestFullName(updatedReservation.getGuestFullName());
        }
        if (updatedReservation.getAddress() != null) {
            existingReservation.setAddress(updatedReservation.getAddress());
        }
        if (updatedReservation.getContactNumber() != null) {
            existingReservation.setContactNumber(updatedReservation.getContactNumber());
        }
        if (updatedReservation.getEmail() != null) {
            existingReservation.setEmail(updatedReservation.getEmail());
        }
        if (updatedReservation.getRoomType() != null) {
            existingReservation.setRoomType(updatedReservation.getRoomType());
        }
        if (updatedReservation.getCheckIn() != null) {
            existingReservation.setCheckIn(updatedReservation.getCheckIn());
        }
        if (updatedReservation.getCheckOut() != null) {
            existingReservation.setCheckOut(updatedReservation.getCheckOut());
        }
        if (updatedReservation.getNumberOfGuests() != null) {
            existingReservation.setNumberOfGuests(updatedReservation.getNumberOfGuests());
        }
        if (updatedReservation.getSpecialRequests() != null) {
            existingReservation.setSpecialRequests(updatedReservation.getSpecialRequests());
        }
        if (updatedReservation.getStatus() != null) {
            existingReservation.setStatus(updatedReservation.getStatus());
        }
        if (updatedReservation.getTotalAmount() != null) {
            existingReservation.setTotalAmount(updatedReservation.getTotalAmount());
        }
        
        // Check for overlap if room type or dates changed
        boolean roomTypeChanged = updatedReservation.getRoomType() != null && 
                                  !updatedReservation.getRoomType().equals(existingReservation.getRoomType());
        boolean datesChanged = (updatedReservation.getCheckIn() != null || 
                               updatedReservation.getCheckOut() != null);
        
        if (roomTypeChanged || datesChanged) {
            // Temporarily set ID to null to check overlap excluding this reservation
            Long tempId = existingReservation.getId();
            existingReservation.setId(null);
            
            if (hasOverlap(existingReservation)) {
                existingReservation.setId(tempId);
                log.error("Update would cause room overlap");
                throw new IllegalArgumentException("Updated dates would conflict with existing reservations");
            }
            
            existingReservation.setId(tempId);
        }
        
        Reservation savedReservation = reservationRepository.save(existingReservation);
        log.info("Reservation updated successfully: {}", savedReservation.getId());
        
        return savedReservation;
    }

    /**
     * Delete a reservation by ID.
     * 
     * @param id reservation ID to delete
     * @throws IllegalArgumentException if reservation not found
     */
    public void delete(Long id) {
        log.info("Deleting reservation with ID: {}", id);
        
        if (!reservationRepository.existsById(id)) {
            log.error("Cannot delete - reservation not found with ID: {}", id);
            throw new IllegalArgumentException("Reservation not found with ID: " + id);
        }
        
        reservationRepository.deleteById(id);
        log.info("Reservation deleted successfully: {}", id);
    }

    /**
     * Check if a reservation has date overlap with existing reservations for the same room type.
     * 
     * This method prevents double-booking by checking if any confirmed or pending reservations
     * overlap with the requested dates for the same room type.
     * 
     * @param reservation reservation to check
     * @return true if overlap exists, false otherwise
     */
    @Transactional(readOnly = true)
    public boolean hasOverlap(Reservation reservation) {
        if (reservation.getRoomType() == null || 
            reservation.getCheckIn() == null || 
            reservation.getCheckOut() == null) {
            return false;
        }
        
        log.debug("Checking overlap for room type {} from {} to {}", 
                 reservation.getRoomType(), 
                 reservation.getCheckIn(), 
                 reservation.getCheckOut());
        
        long conflictCount = reservationRepository.countConflictingReservations(
            reservation.getRoomType(),
            reservation.getCheckIn(),
            reservation.getCheckOut()
        );
        
        // If updating existing reservation, exclude it from conflict check
        if (reservation.getId() != null) {
            // Count would include the reservation being updated, so check if count > 1
            boolean hasConflict = conflictCount > 1;
            log.debug("Overlap check result (excluding self): {}", hasConflict);
            return hasConflict;
        }
        
        boolean hasConflict = conflictCount > 0;
        log.debug("Overlap check result: {}", hasConflict);
        return hasConflict;
    }

    /**
     * Get reservations by status.
     * 
     * @param status reservation status
     * @return list of reservations with given status
     */
    @Transactional(readOnly = true)
    public List<Reservation> getByStatus(String status) {
        log.debug("Fetching reservations with status: {}", status);
        return reservationRepository.findByStatus(status);
    }

    /**
     * Get active reservations (currently occupied).
     * 
     * @return list of active reservations
     */
    @Transactional(readOnly = true)
    public List<Reservation> getActiveReservations() {
        log.debug("Fetching active reservations");
        return reservationRepository.findActiveReservations(LocalDate.now());
    }

    /**
     * Get upcoming reservations.
     * 
     * @return list of upcoming reservations
     */
    @Transactional(readOnly = true)
    public List<Reservation> getUpcomingReservations() {
        log.debug("Fetching upcoming reservations");
        return reservationRepository.findUpcomingReservations(LocalDate.now());
    }

    /**
     * Get recent reservations ordered by creation date.
     * 
     * @return list of reservations ordered by created date descending
     */
    @Transactional(readOnly = true)
    public List<Reservation> getRecentReservations() {
        log.debug("Fetching recent reservations");
        return reservationRepository.findAllOrderByCreatedAtDesc();
    }
}
