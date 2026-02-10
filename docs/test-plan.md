# Test Plan

## Test Strategy

The OceanView Reservation System will be tested using a multi-layered approach:

1. **Unit Tests** - Test individual components in isolation (services, utilities)
2. **Integration Tests** - Test API endpoints and database interactions
3. **Manual UI Tests** - Test frontend user interactions and workflows
4. **End-to-End Tests** - Test complete user journeys

**Target Coverage:** Minimum 80% code coverage for business logic

---

## Test Environment

- **Development Testing**: H2 in-memory database
- **Integration Testing**: H2 with test data
- **Tools**: JUnit 5, Mockito, Spring Boot Test, RestAssured

---

## Unit Tests

### UT-001: Billing Calculation - Standard Reservation
**Component:** BillingService / BillingCalculator  
**Type:** Unit Test  
**Priority:** High

**Test Data:**
```
Room Type: DOUBLE
Number of Nights: 3
Room Rate: $150.00/night
Tax Rate: 10%
Service Charge: 5%
```

**Test Steps:**
1. Create reservation with above parameters
2. Call `calculateTotal(reservation)`
3. Verify subtotal calculation
4. Verify tax calculation
5. Verify service charge calculation
6. Verify final total

**Expected Result:**
- Subtotal: $450.00 (3 × $150)
- Tax: $45.00 (10% of $450)
- Service Charge: $22.50 (5% of $450)
- **Total: $517.50**

**Acceptance Criteria:**
- ✅ All calculations accurate to 2 decimal places
- ✅ Method returns BigDecimal with proper precision
- ✅ Handles edge cases (0 nights, negative values throw exception)

---

### UT-002: Billing Calculation - Single Night
**Component:** BillingCalculator  
**Type:** Unit Test  
**Priority:** High

**Test Data:**
```
Room Type: SINGLE
Number of Nights: 1
Room Rate: $100.00/night
```

**Expected Result:**
- Subtotal: $100.00
- Tax: $10.00
- Service Charge: $5.00
- **Total: $115.00**

**Acceptance Criteria:**
- ✅ Single night calculation is accurate
- ✅ Minimum stay period validation works

---

### UT-003: Billing Calculation - Extended Stay
**Component:** BillingCalculator  
**Type:** Unit Test  
**Priority:** Medium

**Test Data:**
```
Room Type: DELUXE
Number of Nights: 10
Room Rate: $400.00/night
```

**Expected Result:**
- Subtotal: $4,000.00
- Tax: $400.00
- Service Charge: $200.00
- **Total: $4,600.00**

**Acceptance Criteria:**
- ✅ Large amounts calculated correctly
- ✅ No rounding errors in multi-night stays

---

### UT-004: Number of Nights Calculation
**Component:** BillingCalculator  
**Type:** Unit Test  
**Priority:** High

**Test Data:**
```
Check-In: 2026-03-15
Check-Out: 2026-03-18
```

**Expected Result:**
- Number of Nights: 3

**Test Cases:**
- Same day check-in/out (should return 0 or throw error)
- Check-out before check-in (should throw exception)
- Month boundary crossing (2026-03-30 to 2026-04-02 = 3 nights)
- Year boundary crossing

**Acceptance Criteria:**
- ✅ Correctly calculates days between dates
- ✅ Validates date order
- ✅ Throws exception for invalid date ranges

---

### UT-005: Reservation Validation - Valid Data
**Component:** ReservationService  
**Type:** Unit Test  
**Priority:** High

**Test Data:**
```
guestFullName: "John Smith"
email: "john.smith@example.com"
contactNumber: "+1234567890"
checkIn: 2026-06-01
checkOut: 2026-06-05
roomType: DOUBLE
numberOfGuests: 2
```

**Expected Result:**
- Validation passes without exceptions

**Acceptance Criteria:**
- ✅ All required fields present
- ✅ Email format valid
- ✅ Phone number format valid
- ✅ Date range valid
- ✅ Guest count within room capacity

---

### UT-006: Reservation Validation - Invalid Email
**Component:** ReservationService  
**Type:** Unit Test  
**Priority:** High

**Test Data:**
```
email: "invalid-email"
```

**Expected Result:**
- ValidationException thrown with message "Invalid email format"

**Acceptance Criteria:**
- ✅ Rejects malformed email addresses
- ✅ Clear error message returned

---

### UT-007: Reservation Validation - Past Check-in Date
**Component:** ReservationService  
**Type:** Unit Test  
**Priority:** High

**Test Data:**
```
checkIn: 2026-01-01 (past date)
checkOut: 2026-01-05
```

**Expected Result:**
- ValidationException thrown with message "Check-in date cannot be in the past"

**Acceptance Criteria:**
- ✅ Prevents reservations with past dates
- ✅ Uses current system date for comparison

---

### UT-008: Reservation Validation - Check-out Before Check-in
**Component:** ReservationService  
**Type:** Unit Test  
**Priority:** High

**Test Data:**
```
checkIn: 2026-06-10
checkOut: 2026-06-08
```

**Expected Result:**
- ValidationException thrown with message "Check-out date must be after check-in date"

**Acceptance Criteria:**
- ✅ Validates date order
- ✅ Prevents invalid date ranges

---

### UT-009: Password Encryption
**Component:** AuthManager  
**Type:** Unit Test  
**Priority:** High

**Test Data:**
```
plainPassword: "testPassword123"
```

**Test Steps:**
1. Encrypt password
2. Verify encrypted != plain text
3. Verify password against hash
4. Try wrong password against hash

**Expected Result:**
- Encrypted password is hashed
- Correct password verification returns true
- Wrong password verification returns false

**Acceptance Criteria:**
- ✅ Passwords never stored in plain text
- ✅ BCrypt or similar algorithm used
- ✅ Same password hashes to different values (salted)

---

### UT-010: JWT Token Generation
**Component:** AuthManager  
**Type:** Unit Test  
**Priority:** Medium

**Test Data:**
```
User with email: "test@example.com"
Role: STAFF
```

**Expected Result:**
- Valid JWT token generated
- Token contains user email
- Token contains role
- Token has expiration time

**Acceptance Criteria:**
- ✅ Token can be parsed and validated
- ✅ Claims are correctly embedded
- ✅ Token expires after configured time

---

## Integration Tests

### IT-001: Login - Valid Credentials
**Component:** AuthController + AuthService  
**Type:** Integration Test  
**Priority:** High

**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
  "email": "staff@oceanview.com",
  "password": "staff123"
}
```

**Expected Response:** `200 OK`
```json
{
  "token": "eyJhbGc...",
  "userId": 1,
  "email": "staff@oceanview.com",
  "fullName": "Alice Manager"
}
```

**Acceptance Criteria:**
- ✅ Returns valid authentication token
- ✅ Token can be used for subsequent requests
- ✅ User details included in response

---

### IT-002: Login - Invalid Credentials
**Component:** AuthController  
**Type:** Integration Test  
**Priority:** High

**Endpoint:** `POST /api/auth/login`

**Request Body:**
```json
{
  "email": "staff@oceanview.com",
  "password": "wrongpassword"
}
```

**Expected Response:** `401 Unauthorized`
```json
{
  "error": "Unauthorized",
  "message": "Invalid email or password"
}
```

**Acceptance Criteria:**
- ✅ Rejects invalid credentials
- ✅ Does not reveal whether email or password is wrong
- ✅ Returns appropriate error code

---

### IT-003: Create Reservation - Success
**Component:** ReservationController + ReservationService  
**Type:** Integration Test  
**Priority:** High

**Endpoint:** `POST /api/reservations`

**Request Body:**
```json
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
```

**Expected Response:** `201 Created`
```json
{
  "id": 1,
  "guestFullName": "John Smith",
  "roomType": "DOUBLE",
  "checkIn": "2026-08-15",
  "checkOut": "2026-08-18",
  "numberOfGuests": 2,
  "status": "CONFIRMED",
  "totalAmount": 517.50,
  "createdAt": "2026-02-10T10:30:00Z"
}
```

**Acceptance Criteria:**
- ✅ Reservation saved to database
- ✅ ID generated
- ✅ Total amount calculated correctly
- ✅ Status set to CONFIRMED
- ✅ Timestamps populated

---

### IT-004: Create Reservation - Validation Error
**Component:** ReservationController  
**Type:** Integration Test  
**Priority:** High

**Endpoint:** `POST /api/reservations`

**Request Body:**
```json
{
  "guestFullName": "J",
  "email": "invalid-email",
  "checkIn": "2026-01-01",
  "checkOut": "2025-12-31"
}
```

**Expected Response:** `400 Bad Request`
```json
{
  "error": "Validation failed",
  "details": [
    {"field": "guestFullName", "message": "Must be between 2-100 characters"},
    {"field": "email", "message": "Invalid email format"},
    {"field": "checkOut", "message": "Check-out date must be after check-in date"}
  ]
}
```

**Acceptance Criteria:**
- ✅ All validation errors returned at once
- ✅ Field names clearly identified
- ✅ Helpful error messages

---

### IT-005: Get All Reservations
**Component:** ReservationController  
**Type:** Integration Test  
**Priority:** High

**Endpoint:** `GET /api/reservations`

**Prerequisites:** 2 sample reservations in database

**Expected Response:** `200 OK`
```json
[
  {
    "id": 1,
    "guestFullName": "John Smith",
    "roomType": "DOUBLE",
    "checkIn": "2026-03-15",
    "checkOut": "2026-03-18",
    "status": "CONFIRMED",
    "totalAmount": 517.50
  },
  {
    "id": 2,
    "guestFullName": "Sarah Johnson",
    "roomType": "SUITE",
    "checkIn": "2026-04-01",
    "checkOut": "2026-04-05",
    "status": "CONFIRMED",
    "totalAmount": 1150.00
  }
]
```

**Acceptance Criteria:**
- ✅ Returns all reservations
- ✅ Proper JSON array format
- ✅ Essential fields included

---

### IT-006: Get Reservation by ID - Success
**Component:** ReservationController  
**Type:** Integration Test  
**Priority:** High

**Endpoint:** `GET /api/reservations/1`

**Expected Response:** `200 OK`
```json
{
  "id": 1,
  "guestFullName": "John Smith",
  "address": "123 Ocean Drive, Miami, FL 33139",
  "contactNumber": "+1234567890",
  "email": "john.smith@example.com",
  "roomType": "DOUBLE",
  "checkIn": "2026-03-15",
  "checkOut": "2026-03-18",
  "numberOfGuests": 2,
  "specialRequests": "Late check-in expected",
  "status": "CONFIRMED",
  "totalAmount": 517.50
}
```

**Acceptance Criteria:**
- ✅ Returns complete reservation details
- ✅ All fields populated correctly

---

### IT-007: Get Reservation by ID - Not Found
**Component:** ReservationController  
**Type:** Integration Test  
**Priority:** High

**Endpoint:** `GET /api/reservations/999`

**Expected Response:** `404 Not Found`
```json
{
  "error": "Not Found",
  "message": "Reservation with ID 999 not found"
}
```

**Acceptance Criteria:**
- ✅ Handles non-existent IDs gracefully
- ✅ Clear error message

---

### IT-008: Update Reservation - Success
**Component:** ReservationController  
**Type:** Integration Test  
**Priority:** High

**Endpoint:** `PUT /api/reservations/1`

**Request Body:**
```json
{
  "numberOfGuests": 3,
  "specialRequests": "Updated: Need extra bed"
}
```

**Expected Response:** `200 OK`
- Updated fields reflect new values
- Other fields unchanged
- `updatedAt` timestamp updated

**Acceptance Criteria:**
- ✅ Partial update supported
- ✅ Total amount recalculated if dates/room changed
- ✅ Validation still enforced

---

### IT-009: Delete Reservation - Success
**Component:** ReservationController  
**Type:** Integration Test  
**Priority:** High

**Endpoint:** `DELETE /api/reservations/1`

**Expected Response:** `204 No Content`

**Post-Condition:**
- GET /api/reservations/1 returns 404

**Acceptance Criteria:**
- ✅ Reservation removed from database
- ✅ Subsequent queries fail appropriately

---

### IT-010: Generate Bill for Reservation
**Component:** BillingController  
**Type:** Integration Test  
**Priority:** High

**Endpoint:** `GET /api/billing/1`

**Expected Response:** `200 OK`
```json
{
  "billId": 1,
  "reservationId": 1,
  "guestFullName": "John Smith",
  "roomType": "DOUBLE",
  "checkIn": "2026-03-15",
  "checkOut": "2026-03-18",
  "numberOfNights": 3,
  "roomRate": 150.00,
  "subtotal": 450.00,
  "tax": 45.00,
  "serviceCharge": 22.50,
  "totalAmount": 517.50,
  "generatedAt": "2026-02-10T10:30:00Z"
}
```

**Acceptance Criteria:**
- ✅ Bill generated dynamically
- ✅ All calculations accurate
- ✅ Itemized charges included

---

### IT-011: Occupancy Report
**Component:** ReportController  
**Type:** Integration Test  
**Priority:** Medium

**Endpoint:** `GET /api/reports/occupancy?date=2026-03-15`

**Prerequisites:** Sample reservations in database

**Expected Response:** `200 OK`
```json
{
  "date": "2026-03-15",
  "totalRooms": 50,
  "occupiedRooms": 1,
  "availableRooms": 49,
  "occupancyRate": 2.0
}
```

**Acceptance Criteria:**
- ✅ Accurate count for specified date
- ✅ Occupancy percentage calculated correctly

---

### IT-012: Revenue Report
**Component:** ReportController  
**Type:** Integration Test  
**Priority:** Medium

**Endpoint:** `GET /api/reports/revenue?from=2026-03-01&to=2026-03-31`

**Expected Response:** `200 OK`
```json
{
  "fromDate": "2026-03-01",
  "toDate": "2026-03-31",
  "totalRevenue": 517.50,
  "totalReservations": 1,
  "averageReservationValue": 517.50
}
```

**Acceptance Criteria:**
- ✅ Revenue summed correctly for date range
- ✅ Breakdown by room type provided

---

## Manual UI Tests

### UI-001: Login Flow
**Type:** Manual UI Test  
**Priority:** High

**Test Steps:**
1. Navigate to login.html
2. Enter valid credentials (staff@oceanview.com / staff123)
3. Click "Login" button
4. Verify redirect to dashboard.html
5. Verify username displayed

**Expected Result:**
- Successful authentication
- Dashboard loads with user data
- Navigation menu accessible

**Acceptance Criteria:**
- ✅ Login form validates input
- ✅ Error messages display for invalid credentials
- ✅ Session persists across page refreshes

---

### UI-002: Create Reservation Form
**Type:** Manual UI Test  
**Priority:** High

**Test Steps:**
1. Navigate to new-reservation.html
2. Fill all required fields
3. Select room type from dropdown
4. Choose dates from date picker
5. Submit form

**Expected Result:**
- Form validates all fields
- Server creates reservation
- Success message displayed
- Redirect to view-reservation page with new reservation

**Acceptance Criteria:**
- ✅ Client-side validation prevents submission of invalid data
- ✅ Date picker defaults to today
- ✅ Phone number formatted automatically
- ✅ Email validation in real-time

---

### UI-003: View Reservation Details
**Type:** Manual UI Test  
**Priority:** High

**Test Steps:**
1. Navigate to dashboard.html
2. Click on a reservation from the list
3. View full reservation details

**Expected Result:**
- Complete reservation information displayed
- Guest details visible
- Dates formatted correctly
- Total amount shown

**Acceptance Criteria:**
- ✅ All fields readable
- ✅ Edit and Cancel buttons available
- ✅ Print Bill button functional

---

### UI-004: Print Bill
**Type:** Manual UI Test  
**Priority:** Medium

**Test Steps:**
1. Navigate to view-reservation page
2. Click "Print Bill" button
3. Verify bill displays in billing.html

**Expected Result:**
- Bill generated with all charges
- Professional format suitable for printing
- Browser print dialog accessible

**Acceptance Criteria:**
- ✅ Itemized charges listed
- ✅ Tax and service charge broken down
- ✅ Print-friendly CSS applied

---

### UI-005: Reports Dashboard
**Type:** Manual UI Test  
**Priority:** Medium

**Test Steps:**
1. Login as manager
2. Navigate to reports.html
3. Select date range
4. Generate occupancy report
5. Generate revenue report

**Expected Result:**
- Reports display with charts/tables
- Data matches backend calculations
- Export options available

**Acceptance Criteria:**
- ✅ Date range validation
- ✅ Visual representation (charts)
- ✅ Export to PDF/CSV

---

## Test Execution

### Running Unit Tests
```bash
mvn test -Dtest="*Test"
```

### Running Integration Tests
```bash
mvn test -Dtest="*IT"
```

### Running All Tests
```bash
mvn clean test
```

### Test Coverage Report
```bash
mvn test jacoco:report
# Report available at: target/site/jacoco/index.html
```

---

## Test Data Management

**Test Database:** H2 in-memory (automatically reset between tests)

**Fixtures:** Use `@BeforeEach` to set up test data

**Cleanup:** Use `@AfterEach` to clean up (if needed)

---

## Defect Tracking

**Priority Levels:**
- **P1 Critical:** Blocks core functionality
- **P2 High:** Significant feature impact
- **P3 Medium:** Minor feature impact
- **P4 Low:** Cosmetic issues

**Status:** Open, In Progress, Fixed, Verified, Closed

---

## Test Schedule

| Phase | Timeline | Deliverable |
|-------|----------|-------------|
| Unit Test Implementation | Week 1 | All UT-* tests passing |
| Integration Test Implementation | Week 2 | All IT-* tests passing |
| Manual UI Testing | Week 3 | Test execution report |
| Bug Fixes & Regression | Week 4 | All defects resolved |

---

## Success Criteria

- ✅ 80%+ code coverage
- ✅ All unit tests pass
- ✅ All integration tests pass
- ✅ Zero P1/P2 defects in production
- ✅ Manual testing sign-off by QA
