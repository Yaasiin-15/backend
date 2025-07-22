package com.restaurant.management.controller;

import com.restaurant.management.payload.AdminCreateUserRequest;
import com.restaurant.management.payload.AdminCreateUserResponse;
import com.restaurant.management.payload.JwtResponse;
import com.restaurant.management.payload.LoginRequest;
import com.restaurant.management.payload.MessageResponse;
import com.restaurant.management.payload.SignupRequest;
import com.restaurant.management.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication operations.
 * Handles user login and registration requests.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private AuthService authService;

    /**
     * Authenticates a user and returns JWT token.
     * 
     * @param loginRequest the login credentials
     * @return JWT response with user details
     */
    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Authentication request received for user: {}", loginRequest.getUsername());
        
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    /**
     * Registers a new user in the system.
     * 
     * @param signupRequest the user registration details
     * @return success message
     */
    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        logger.info("Registration request received for user: {}", signupRequest.getUsername());
        
        String message = authService.registerUser(signupRequest);
        return ResponseEntity.ok(new MessageResponse(message));
    }

    /**
     * Creates a new user account by admin with specified roles.
     * Only accessible by users with ADMIN role.
     * 
     * @param adminCreateUserRequest the user creation details
     * @return response with user details and credentials
     */
    @PostMapping("/admin/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminCreateUserResponse> createUserByAdmin(@Valid @RequestBody AdminCreateUserRequest adminCreateUserRequest) {
        logger.info("Admin user creation request received for user: {}", adminCreateUserRequest.getUsername());
        
        AdminCreateUserResponse response = authService.createUserByAdmin(adminCreateUserRequest);
        return ResponseEntity.ok(response);
    }
}