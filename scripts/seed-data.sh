#!/bin/bash

# ==============================================================================
# OceanView Reservation System - Sample Data Seed Script
# ==============================================================================
#
# PURPOSE:
# This script provides instructions for implementing automatic sample data
# seeding when the application starts in development or testing mode.
#
# ==============================================================================

# IMPLEMENTATION INSTRUCTIONS:
# ----------------------------
# When implementing this functionality, create a DataLoader component in:
# src/main/java/com/oceanview/reservation/config/DataLoader.java
#
# The DataLoader should:
# 1. Implement CommandLineRunner or ApplicationRunner interface
# 2. Be annotated with @Component
# 3. Use @Profile("dev") to only run in development mode
# 4. Have @Autowired dependencies for repositories
#
# PSEUDOCODE:
# -----------
# @Component
# @Profile("dev")
# public class DataLoader implements CommandLineRunner {
#     @Autowired
#     private ReservationRepository reservationRepository;
#     
#     @Autowired
#     private UserRepository userRepository;
#     
#     @Override
#     public void run(String... args) throws Exception {
#         // Check if data already exists to avoid duplicates
#         if (reservationRepository.count() > 0) {
#             return; // Data already seeded
#         }
#         
#         // Create and save sample reservations
#         // - Reservation 1: John Smith, DOUBLE room, 2026-03-15 to 2026-03-18
#         // - Reservation 2: Sarah Johnson, SUITE room, 2026-04-01 to 2026-04-05
#         
#         // Create and save sample users
#         // - Staff user: staff@oceanview.com
#         // - Manager user: manager@oceanview.com
#         
#         // Set timestamps (createdAt, updatedAt)
#         // Calculate and set totalAmount using BillingCalculator
#         // Encrypt passwords before storing
#         
#         // Call repository.save() for each entity
#         // Log success message
#     }
# }

# DATA SOURCE:
# ------------
# Sample data specifications are documented in:
# src/main/resources/db/sample-data.md
#
# This includes:
# - Two example reservations with complete guest details
# - Room type rate mappings (SINGLE: $100, DOUBLE: $150, SUITE: $250, DELUXE: $400)
# - Sample user accounts for authentication testing
# - Tax rate (10%) and service charge (5%) configuration

# EXECUTION TIMING:
# -----------------
# The DataLoader will automatically execute when:
# - Spring Boot application starts
# - Active profile is set to "dev" or "test"
# - Database tables have been created by Hibernate

# VALIDATION:
# -----------
# After implementation, verify by:
# 1. Starting the application with --spring.profiles.active=dev
# 2. Checking application logs for "Sample data loaded successfully"
# 3. Querying H2 console: SELECT * FROM reservation
# 4. Testing GET /api/reservations endpoint
# 5. Verifying user login with staff@oceanview.com / staff123

# CLEANUP:
# --------
# To reset sample data:
# - Restart the application (H2 in-memory database will be cleared)
# - Or use: spring.jpa.hibernate.ddl-auto=create-drop

# ==============================================================================
# NOTE: This is a placeholder script with instructions only.
# Actual implementation will be in Java code (DataLoader.java).
# ==============================================================================

echo "This is a placeholder script. See comments above for implementation instructions."
echo "Actual data seeding will be implemented in DataLoader.java component."
