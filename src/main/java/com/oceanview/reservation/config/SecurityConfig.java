package com.oceanview.reservation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the OceanView Reservation System.
 * 
 * This configuration disables CSRF protection and permits all requests
 * for development purposes. In a production environment, this should be
 * replaced with proper authentication and authorization.
 * 
 * IMPORTANT: This is a DEVELOPMENT-ONLY configuration.
 * For production:
 * - Enable CSRF protection for non-API endpoints
 * - Implement proper JWT-based authentication
 * - Configure role-based access control (RBAC)
 * - Use HTTPS only
 * - Implement rate limiting
 * - Add security headers
 * 
 * @author Enzo
 * @version 1.0.0
 * @since 2026-02-10
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configure security filter chain for HTTP security.
     * 
     * Current configuration (DEVELOPMENT ONLY):
     * - CSRF disabled for REST API
     * - All endpoints publicly accessible
     * - H2 console enabled
     * - Frame options disabled for H2 console
     * 
     * @param http the HttpSecurity to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for REST API (would need to enable for production with proper tokens)
            .csrf(csrf -> csrf.disable())
            
            // Disable frame options to allow H2 console to work
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable())
            )
            
            // Permit all requests (DEVELOPMENT ONLY - replace with proper auth in production)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll()           // All API endpoints
                .requestMatchers("/h2-console/**").permitAll()    // H2 console
                .requestMatchers("/error").permitAll()            // Error page
                .anyRequest().permitAll()                         // All other requests
            );
        
        return http.build();
    }
}
