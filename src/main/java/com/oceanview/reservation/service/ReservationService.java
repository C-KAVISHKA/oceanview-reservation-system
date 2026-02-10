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

// Business logic for managing reservations
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;

    // Create a new reservation (validates dates and checks for double-booking)
    public Reservation create(Reservation reservation) {
        log.info("Creating reservation for: {}", reservation.getGuestFullName());
        
        // Make sure check-out is after check-in
        if (reservation.getCheckOut().isBefore(reservation.getCheckIn()) ||
            reservation.getCheckOut().isEqual(reservation.getCheckIn())) {
            log.error("Invalid date range: check-out must be after check-in");
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        // Make sure the room isn't already booked for those dates
        if (hasOverlap(reservation)) {
            log.error("Room type {} not available for dates {} to {}", 
                     reservation.getRoomType(), 
                     reservation.getCheckIn(), 
                     reservation.getCheckOut());
            throw new IllegalArgumentException("Room type " + reservation.getRoomType() + 
                                             " is not available for the selected dates");
        }

        if (reservation.getStatus() == null || reservation.getStatus().isEmpty()) {
            reservation.setStatus("PENDING");
        }
        
        Reservation savedReservation = reservationRepository.save(reservation);
        log.info("Reservation created with ID: {}", savedReservation.getId());

        return savedReservation;
    }

    // Get a reservation by its ID
    @Transactional(readOnly = true)
    public Optional<Reservation> getById(Long id) {
        return reservationRepository.findById(id);
    }

    // Get all reservations
    @Transactional(readOnly = true)
    public List<Reservation> listAll() {
        return reservationRepository.findAll();
    }

    // Search reservations by guest name
    @Transactional(readOnly = true)
    public List<Reservation> searchByGuest(String guestName) {
        if (guestName == null || guestName.trim().isEmpty()) {
            return listAll();
        }
        return reservationRepository.findByGuestFullNameContainingIgnoreCase(guestName.trim());
    }

    // Update an existing reservation
    public Reservation update(Long id, Reservation updatedReservation) {
        log.info("Updating reservation {}", id);
        
        Reservation existingReservation = reservationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID: " + id));

        // Validate dates if they're being changed
        if (updatedReservation.getCheckIn() != null && updatedReservation.getCheckOut() != null) {
            if (updatedReservation.getCheckOut().isBefore(updatedReservation.getCheckIn()) ||
                updatedReservation.getCheckOut().isEqual(updatedReservation.getCheckIn())) {
                throw new IllegalArgumentException("Check-out date must be after check-in date");
            }
        }

        // Update each field if provided
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
        
        // Check for conflicts if room type or dates changed
        boolean roomTypeChanged = updatedReservation.getRoomType() != null &&
                                  !updatedReservation.getRoomType().equals(existingReservation.getRoomType());
        boolean datesChanged = (updatedReservation.getCheckIn() != null || 
                               updatedReservation.getCheckOut() != null);
        
        if (roomTypeChanged || datesChanged) {
            Long tempId = existingReservation.getId();
            existingReservation.setId(null);

            if (hasOverlap(existingReservation)) {
                existingReservation.setId(tempId);
                throw new IllegalArgumentException("Updated dates would conflict with existing reservations");
            }

            existingReservation.setId(tempId);
        }

        Reservation savedReservation = reservationRepository.save(existingReservation);
        log.info("Reservation {} updated", savedReservation.getId());

        return savedReservation;
    }

    // Delete a reservation by ID
    public void delete(Long id) {
        log.info("Deleting reservation {}", id);
        
        if (!reservationRepository.existsById(id)) {
            throw new IllegalArgumentException("Reservation not found with ID: " + id);
        }

        reservationRepository.deleteById(id);
        log.info("Reservation {} deleted", id);
    }

    // Check if the room type is already booked for the given dates
    @Transactional(readOnly = true)
    public boolean hasOverlap(Reservation reservation) {
        if (reservation.getRoomType() == null || 
            reservation.getCheckIn() == null || 
            reservation.getCheckOut() == null) {
            return false;
        }

        long conflictCount = reservationRepository.countConflictingReservations(
            reservation.getRoomType(),
            reservation.getCheckIn(),
            reservation.getCheckOut()
        );

        // If updating, exclude the current reservation from the conflict count
        if (reservation.getId() != null) {
            return conflictCount > 1;
        }

        return conflictCount > 0;
    }

    // Get reservations filtered by status
    @Transactional(readOnly = true)
    public List<Reservation> getByStatus(String status) {
        return reservationRepository.findByStatus(status);
    }

    // Get currently active reservations (guests checked in today)
    @Transactional(readOnly = true)
    public List<Reservation> getActiveReservations() {
        return reservationRepository.findActiveReservations(LocalDate.now());
    }

    // Get reservations with future check-in dates
    @Transactional(readOnly = true)
    public List<Reservation> getUpcomingReservations() {
        return reservationRepository.findUpcomingReservations(LocalDate.now());
    }

    // Get reservations sorted by newest first
    @Transactional(readOnly = true)
    public List<Reservation> getRecentReservations() {
        return reservationRepository.findAllOrderByCreatedAtDesc();
    }
}
