# Sample Data for OceanView Reservation System

## Purpose
This document describes the sample data to be loaded during application startup for development and testing purposes.

---

## Room Types and Rates

| Room Type | Base Rate (per night) | Max Guests |
|-----------|----------------------|------------|
| SINGLE    | $100.00              | 1          |
| DOUBLE    | $150.00              | 2          |
| SUITE     | $250.00              | 4          |
| DELUXE    | $400.00              | 6          |

### Additional Charges
- Tax Rate: 10%
- Service Charge: 5%

---

## Sample Reservations

### Reservation 1
```
Guest Full Name: John Smith
Address: 123 Ocean Drive, Miami, FL 33139
Contact Number: +1234567890
Email: john.smith@example.com
Room Type: DOUBLE
Check-In Date: 2026-03-15
Check-Out Date: 2026-03-18
Number of Guests: 2
Special Requests: Late check-in expected, need parking spot
Status: CONFIRMED
```

**Calculated Details:**
- Number of Nights: 3
- Room Rate: $150.00/night
- Subtotal: $450.00
- Tax (10%): $45.00
- Service Charge (5%): $22.50
- **Total Amount: $517.50**

---

### Reservation 2
```
Guest Full Name: Sarah Johnson
Address: 456 Palm Avenue, Los Angeles, CA 90001
Contact Number: +1987654321
Email: sarah.johnson@example.com
Room Type: SUITE
Check-In Date: 2026-04-01
Check-Out Date: 2026-04-05
Number of Guests: 4
Special Requests: Ocean view preferred, extra towels
Status: CONFIRMED
```

**Calculated Details:**
- Number of Nights: 4
- Room Rate: $250.00/night
- Subtotal: $1,000.00
- Tax (10%): $100.00
- Service Charge (5%): $50.00
- **Total Amount: $1,150.00**

---

## Sample Users (for testing authentication)

### Staff User
```
Email: staff@oceanview.com
Password: staff123
Role: STAFF
Full Name: Alice Manager
```

### Manager User
```
Email: manager@oceanview.com
Password: manager123
Role: MANAGER
Full Name: Bob Director
```

---

## Implementation Notes

1. **Data Loader**: Use `@Component` with `CommandLineRunner` or `ApplicationRunner` to execute seed data insertion on startup.

2. **Conditional Loading**: Only load sample data when profile is `dev` or `test`, not in production.

3. **Idempotency**: Check if data already exists before inserting to avoid duplicates on application restart.

4. **Password Encryption**: Ensure user passwords are properly encrypted using BCrypt or similar before storage.

5. **Timestamp**: Set `createdAt` to current timestamp during insertion.

6. **Validation**: Ensure all sample data passes the same validation rules as real data.

---

## Testing Scenarios

These sample reservations support testing:
- ✅ Viewing reservation list
- ✅ Fetching reservation by ID
- ✅ Generating bills
- ✅ Calculating occupancy rates
- ✅ Computing revenue reports
- ✅ User authentication flows
- ✅ Date range queries
- ✅ Guest contact information retrieval

---

## Future Enhancements

Consider adding:
- More diverse reservation dates for comprehensive testing
- Cancelled and pending status reservations
- Past check-out dates for historical data testing
- Edge cases (same-day bookings, long-term stays)
- Multiple reservations for the same guest
