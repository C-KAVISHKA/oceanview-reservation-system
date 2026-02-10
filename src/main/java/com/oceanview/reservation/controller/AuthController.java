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

// Handles user login and logout (demo implementation with hardcoded credentials)
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Slf4j
public class AuthController {

    // Demo credentials - in production these would come from a database
    private static final String DEMO_EMAIL = "staff@oceanview.com";
    private static final String DEMO_PASSWORD = "Passw0rd!";
    private static final String DEMO_ROLE = "STAFF";

    // POST /api/auth/login - validates credentials and returns a token
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login attempt for: {}", request.getEmail());

        if (DEMO_EMAIL.equals(request.getEmail()) && DEMO_PASSWORD.equals(request.getPassword())) {
            log.info("Login successful for: {}", request.getEmail());
            
            UserInfo userInfo = new UserInfo(request.getEmail(), DEMO_ROLE, "Staff User");
            String demoToken = "demo-token-" + System.currentTimeMillis();
            LoginResponse response = new LoginResponse(demoToken, userInfo);

            return ResponseEntity.ok(response);
        } else {
            log.warn("Login failed for: {} - Invalid credentials", request.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Invalid email or password"));
        }
    }

    // POST /api/auth/logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        log.info("Logout request");
        return ResponseEntity.ok(new SuccessResponse("Logout successful"));
    }

    // Login request body
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

    // Login response with token
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponse {
        private String token;
        private UserInfo user;
    }

    // User info returned after login
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private String email;
        private String role;
        private String name;
    }

    // Error response wrapper
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

    // Success response wrapper
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
