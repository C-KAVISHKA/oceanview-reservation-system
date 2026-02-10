package com.oceanview.reservation.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Authentication operations.
 * 
 * ⚠️ DEMO ONLY — This is a simplified authentication implementation for demonstration purposes.
 * 
 * REPLACE WITH REAL AUTHENTICATION FOR PRODUCTION:
 * - Use Spring Security with proper authentication manager
 * - Implement JWT token generation with proper secret key
 * - Hash passwords with BCrypt (never store plain text)
 * - Use database to validate credentials
 * - Implement token expiration and refresh mechanism
 * - Add rate limiting to prevent brute force attacks
 * - Use HTTPS for secure transmission
 * - Store tokens in HTTP-only cookies (not localStorage)
 * 
 * @author Enzo
 * @version 1.0.0
 * @since 2026-02-10
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Slf4j
public class AuthController {

    // DEMO CREDENTIALS - Replace with database lookup in production
    private static final String DEMO_EMAIL = "staff@oceanview.com";
    private static final String DEMO_PASSWORD = "Passw0rd!";
    private static final String DEMO_ROLE = "STAFF";

    /**
     * Login endpoint - DEMO IMPLEMENTATION ONLY.
     * 
     * POST /api/auth/login
     * 
     * Accepts email and password credentials and returns a token on success.
     * 
     * ⚠️ WARNING: This is a simplified demo implementation.
     * In production, use Spring Security with:
     * - Database-backed user authentication
     * - BCrypt password hashing
     * - JWT token generation with proper secret
     * - Token expiration and refresh logic
     * 
     * @param request login credentials (email and password)
     * @return authentication token and user details on success, 401 on failure
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        log.info("POST /api/auth/login - Login attempt for email: {}", request.getEmail());
        
        // DEMO ONLY: Simple credential check
        // TODO: Replace with proper authentication:
        //   1. Look up user in database by email
        //   2. Verify password using BCrypt.matches()
        //   3. Generate real JWT token with proper secret
        //   4. Set token expiration
        //   5. Log authentication event
        
        if (DEMO_EMAIL.equals(request.getEmail()) && DEMO_PASSWORD.equals(request.getPassword())) {
            log.info("Login successful for user: {}", request.getEmail());
            
            // Create user response
            UserInfo userInfo = new UserInfo(request.getEmail(), DEMO_ROLE, "Staff User");
            
            // DEMO TOKEN - Replace with real JWT generation
            String demoToken = "demo-token-" + System.currentTimeMillis();
            
            // Create authentication response
            LoginResponse response = new LoginResponse(demoToken, userInfo);
            
            return ResponseEntity.ok(response);
        } else {
            log.warn("Login failed for email: {} - Invalid credentials", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid email or password"));
        }
    }

    /**
     * Logout endpoint - DEMO IMPLEMENTATION ONLY.
     * 
     * POST /api/auth/logout
     * 
     * In a real implementation, this would:
     * - Invalidate the JWT token (add to blacklist)
     * - Clear server-side session
     * - Log the logout event
     * 
     * @return success message
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        log.info("POST /api/auth/logout - Logout request");
        
        // DEMO ONLY: Just return success
        // TODO: In production, invalidate token and clear session
        
        return ResponseEntity.ok(new SuccessResponse("Logout successful"));
    }

    /**
     * Login request DTO.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;
    }

    /**
     * Login response DTO.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponse {
        private String token;
        private UserInfo user;
    }

    /**
     * User information DTO.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private String email;
        private String role;
        private String name;
    }

    /**
     * Error response DTO.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponse {
        private String error;
        private long timestamp;

        public ErrorResponse(String error) {
            this.error = error;
            this.timestamp = System.currentTimeMillis();
        }
    }

    /**
     * Success response DTO.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SuccessResponse {
        private String message;
        private long timestamp;

        public SuccessResponse(String message) {
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }
    }
}
