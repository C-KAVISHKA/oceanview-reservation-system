package com.oceanview.reservation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// Reservation entity - stores all booking details for a guest
@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Guest full name is required")
    @Size(min = 2, max = 100, message = "Guest name must be between 2 and 100 characters")
    @Column(name = "guest_full_name", nullable = false, length = 100)
    private String guestFullName;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    @Column(name = "address", nullable = false)
    private String address;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^\\+?[0-9\\-\\s()]{7,20}$", message = "Invalid contact number format")
    @Column(name = "contact_number", nullable = false, length = 20)
    private String contactNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @NotBlank(message = "Room type is required")
    @Pattern(regexp = "^(SINGLE|DOUBLE|SUITE|DELUXE)$", message = "Room type must be SINGLE, DOUBLE, SUITE, or DELUXE")
    @Column(name = "room_type", nullable = false, length = 20)
    private String roomType;

    @NotNull(message = "Check-in date is required")
    @Future(message = "Check-in date must be in the future")
    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @NotNull(message = "Check-out date is required")
    @Future(message = "Check-out date must be in the future")
    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    @NotNull(message = "Number of guests is required")
    @Min(value = 1, message = "Number of guests must be at least 1")
    @Max(value = 10, message = "Number of guests cannot exceed 10")
    @Column(name = "number_of_guests", nullable = false)
    private Integer numberOfGuests;

    @Size(max = 500, message = "Special requests must not exceed 500 characters")
    @Column(name = "special_requests", length = 500)
    private String specialRequests;

    @Column(name = "status", length = 20)
    private String status = "PENDING";

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Make sure check-out is after check-in
    @AssertTrue(message = "Check-out date must be after check-in date")
    public boolean isValidDateRange() {
        if (checkIn == null || checkOut == null) {
            return true; // Let @NotNull handle null validation
        }
        return checkOut.isAfter(checkIn);
    }

    // Calculate how many nights the guest is staying
    @Transient
    public long getNumberOfNights() {
        if (checkIn == null || checkOut == null) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
    }

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = "PENDING";
        }
    }
}
