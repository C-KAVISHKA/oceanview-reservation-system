# API Documentation

## Base URL

```
http://localhost:8080/api
```

## Authentication

All endpoints except `/api/auth/login` require authentication.

---

## Endpoints

### Authentication Endpoints

#### POST /api/auth/login

Authenticate a user and return session token.

**Request Body:**
```json
{
  "email": "string (required, valid email format)",
  "password": "string (required, min 6 characters)"
}
```

**Success Response (200 OK):**
```json
{
  "token": "string",
  "userId": "long",
  "email": "string",
  "fullName": "string"
}
```

**Error Responses:**
- `400 Bad Request` - Invalid credentials format
- `401 Unauthorized` - Invalid email or password

---

### Reservation Endpoints

#### POST /api/reservations

Create a new reservation.

**Request Body:**
```json
{
  "guestFullName": "string (required, 2-100 characters)",
  "address": "string (required, 10-200 characters)",
  "contactNumber": "string (required, pattern: ^\+?[0-9]{10,15}$)",
  "email": "string (required, valid email format)",
  "roomType": "string (required, values: SINGLE, DOUBLE, SUITE, DELUXE)",
  "checkIn": "string (required, format: YYYY-MM-DD, must be today or future)",
  "checkOut": "string (required, format: YYYY-MM-DD, must be after checkIn)",
  "numberOfGuests": "integer (required, min: 1, max: 10)",
  "specialRequests": "string (optional, max 500 characters)"
}
```

**Validation Rules:**
- `guestFullName`: Required, 2-100 characters
- `address`: Required, 10-200 characters
- `contactNumber`: Required, must match pattern `^\+?[0-9]{10,15}$`
- `email`: Required, valid email format
- `roomType`: Required, must be one of: SINGLE, DOUBLE, SUITE, DELUXE
- `checkIn`: Required, format YYYY-MM-DD, cannot be in the past
- `checkOut`: Required, format YYYY-MM-DD, must be after checkIn date
- `numberOfGuests`: Required, integer between 1 and 10
- `specialRequests`: Optional, maximum 500 characters

**Success Response (201 Created):**
```json
{
  "id": "long",
  "guestFullName": "string",
  "address": "string",
  "contactNumber": "string",
  "email": "string",
  "roomType": "string",
  "checkIn": "string (YYYY-MM-DD)",
  "checkOut": "string (YYYY-MM-DD)",
  "numberOfGuests": "integer",
  "specialRequests": "string",
  "status": "string (CONFIRMED, PENDING, CANCELLED)",
  "createdAt": "string (ISO 8601 timestamp)",
  "totalAmount": "decimal"
}
```

**Error Responses:**
- `400 Bad Request` - Validation error (invalid fields)
  ```json
  {
    "error": "Validation failed",
    "details": [
      {
        "field": "checkIn",
        "message": "Check-in date cannot be in the past"
      }
    ]
  }
  ```
- `401 Unauthorized` - Missing or invalid authentication token
- `409 Conflict` - Room not available for selected dates

---

#### GET /api/reservations

Retrieve all reservations (with optional filters).

**Query Parameters:**
- `status` (optional): Filter by status (CONFIRMED, PENDING, CANCELLED)
- `guestEmail` (optional): Filter by guest email
- `fromDate` (optional): Filter reservations from date (YYYY-MM-DD)
- `toDate` (optional): Filter reservations to date (YYYY-MM-DD)

**Success Response (200 OK):**
```json
[
  {
    "id": "long",
    "guestFullName": "string",
    "email": "string",
    "roomType": "string",
    "checkIn": "string (YYYY-MM-DD)",
    "checkOut": "string (YYYY-MM-DD)",
    "numberOfGuests": "integer",
    "status": "string",
    "totalAmount": "decimal"
  }
]
```

**Error Responses:**
- `401 Unauthorized` - Missing or invalid authentication token

---

#### GET /api/reservations/{id}

Retrieve a specific reservation by ID.

**Path Parameters:**
- `id` (long, required): Reservation ID

**Success Response (200 OK):**
```json
{
  "id": "long",
  "guestFullName": "string",
  "address": "string",
  "contactNumber": "string",
  "email": "string",
  "roomType": "string",
  "checkIn": "string (YYYY-MM-DD)",
  "checkOut": "string (YYYY-MM-DD)",
  "numberOfGuests": "integer",
  "specialRequests": "string",
  "status": "string",
  "createdAt": "string (ISO 8601 timestamp)",
  "updatedAt": "string (ISO 8601 timestamp)",
  "totalAmount": "decimal"
}
```

**Error Responses:**
- `401 Unauthorized` - Missing or invalid authentication token
- `404 Not Found` - Reservation does not exist
  ```json
  {
    "error": "Not Found",
    "message": "Reservation with ID {id} not found"
  }
  ```

---

#### PUT /api/reservations/{id}

Update an existing reservation.

**Path Parameters:**
- `id` (long, required): Reservation ID

**Request Body:**
```json
{
  "guestFullName": "string (optional, 2-100 characters)",
  "address": "string (optional, 10-200 characters)",
  "contactNumber": "string (optional, pattern: ^\+?[0-9]{10,15}$)",
  "email": "string (optional, valid email format)",
  "roomType": "string (optional, values: SINGLE, DOUBLE, SUITE, DELUXE)",
  "checkIn": "string (optional, format: YYYY-MM-DD)",
  "checkOut": "string (optional, format: YYYY-MM-DD)",
  "numberOfGuests": "integer (optional, min: 1, max: 10)",
  "specialRequests": "string (optional, max 500 characters)",
  "status": "string (optional, values: CONFIRMED, PENDING, CANCELLED)"
}
```

**Validation Rules:**
- Same validation rules apply as POST /api/reservations
- All fields are optional (partial update supported)
- If updating dates, checkOut must still be after checkIn

**Success Response (200 OK):**
```json
{
  "id": "long",
  "guestFullName": "string",
  "address": "string",
  "contactNumber": "string",
  "email": "string",
  "roomType": "string",
  "checkIn": "string (YYYY-MM-DD)",
  "checkOut": "string (YYYY-MM-DD)",
  "numberOfGuests": "integer",
  "specialRequests": "string",
  "status": "string",
  "updatedAt": "string (ISO 8601 timestamp)",
  "totalAmount": "decimal"
}
```

**Error Responses:**
- `400 Bad Request` - Validation error
- `401 Unauthorized` - Missing or invalid authentication token
- `404 Not Found` - Reservation does not exist
- `409 Conflict` - Room not available for updated dates

---

#### DELETE /api/reservations/{id}

Cancel/delete a reservation.

**Path Parameters:**
- `id` (long, required): Reservation ID

**Success Response (204 No Content)**

No response body.

**Error Responses:**
- `401 Unauthorized` - Missing or invalid authentication token
- `404 Not Found` - Reservation does not exist
- `409 Conflict` - Cannot delete reservation (e.g., already checked in)

---

### Billing Endpoints

#### GET /api/billing/{id}

Generate and retrieve billing information for a reservation.

**Path Parameters:**
- `id` (long, required): Reservation ID

**Success Response (200 OK):**
```json
{
  "billId": "long",
  "reservationId": "long",
  "guestFullName": "string",
  "roomType": "string",
  "checkIn": "string (YYYY-MM-DD)",
  "checkOut": "string (YYYY-MM-DD)",
  "numberOfNights": "integer",
  "roomRate": "decimal",
  "subtotal": "decimal",
  "tax": "decimal",
  "serviceCharge": "decimal",
  "totalAmount": "decimal",
  "generatedAt": "string (ISO 8601 timestamp)",
  "itemizedCharges": [
    {
      "description": "string",
      "amount": "decimal"
    }
  ]
}
```

**Error Responses:**
- `401 Unauthorized` - Missing or invalid authentication token
- `404 Not Found` - Reservation does not exist

---

### Reports Endpoints

#### GET /api/reports/occupancy

Retrieve room occupancy report for a specific date.

**Query Parameters:**
- `date` (required): Date in format YYYY-MM-DD

**Success Response (200 OK):**
```json
{
  "date": "string (YYYY-MM-DD)",
  "totalRooms": "integer",
  "occupiedRooms": "integer",
  "availableRooms": "integer",
  "occupancyRate": "decimal (percentage)",
  "roomBreakdown": [
    {
      "roomType": "string",
      "total": "integer",
      "occupied": "integer",
      "available": "integer"
    }
  ]
}
```

**Error Responses:**
- `400 Bad Request` - Invalid date format
  ```json
  {
    "error": "Bad Request",
    "message": "Invalid date format. Expected YYYY-MM-DD"
  }
  ```
- `401 Unauthorized` - Missing or invalid authentication token

---

#### GET /api/reports/revenue

Retrieve revenue report for a date range.

**Query Parameters:**
- `from` (required): Start date in format YYYY-MM-DD
- `to` (required): End date in format YYYY-MM-DD

**Success Response (200 OK):**
```json
{
  "fromDate": "string (YYYY-MM-DD)",
  "toDate": "string (YYYY-MM-DD)",
  "totalRevenue": "decimal",
  "totalReservations": "integer",
  "averageReservationValue": "decimal",
  "revenueByRoomType": [
    {
      "roomType": "string",
      "revenue": "decimal",
      "reservationCount": "integer"
    }
  ],
  "dailyRevenue": [
    {
      "date": "string (YYYY-MM-DD)",
      "revenue": "decimal",
      "reservationCount": "integer"
    }
  ]
}
```

**Validation Rules:**
- Both `from` and `to` parameters are required
- Dates must be in YYYY-MM-DD format
- `to` date must be after or equal to `from` date
- Date range should not exceed 365 days

**Error Responses:**
- `400 Bad Request` - Invalid date format or range
  ```json
  {
    "error": "Bad Request",
    "message": "Invalid date range. 'to' date must be after 'from' date"
  }
  ```
- `401 Unauthorized` - Missing or invalid authentication token

---

## Common HTTP Status Codes

| Code | Status | Description |
|------|--------|-------------|
| 200 | OK | Request succeeded |
| 201 | Created | Resource successfully created |
| 204 | No Content | Request succeeded, no content to return |
| 400 | Bad Request | Validation error or malformed request |
| 401 | Unauthorized | Authentication required or failed |
| 404 | Not Found | Resource does not exist |
| 409 | Conflict | Request conflicts with current state |
| 500 | Internal Server Error | Unexpected server error |

---

## Error Response Format

All error responses follow this structure:

```json
{
  "error": "string (error type)",
  "message": "string (human-readable error message)",
  "timestamp": "string (ISO 8601 timestamp)",
  "path": "string (API endpoint path)",
  "details": [
    {
      "field": "string (field name, for validation errors)",
      "message": "string (field-specific error message)"
    }
  ]
}
```

---

## Data Types and Formats

- **Dates**: Always use ISO 8601 format `YYYY-MM-DD` for date-only fields
- **Timestamps**: Use ISO 8601 format with timezone `YYYY-MM-DDTHH:mm:ss.sssZ`
- **Decimals**: Use two decimal places for monetary values
- **Phone Numbers**: International format with optional `+` prefix, 10-15 digits
- **Email**: Must be valid email format per RFC 5322

---

## Room Types

| Value | Description | Base Rate |
|-------|-------------|-----------|
| SINGLE | Single occupancy room | $100/night |
| DOUBLE | Double occupancy room | $150/night |
| SUITE | Suite with living area | $250/night |
| DELUXE | Deluxe suite | $400/night |

---

## Reservation Status Values

| Status | Description |
|--------|-------------|
| PENDING | Reservation created, awaiting confirmation |
| CONFIRMED | Reservation confirmed |
| CHECKED_IN | Guest has checked in |
| CHECKED_OUT | Guest has checked out |
| CANCELLED | Reservation cancelled |
