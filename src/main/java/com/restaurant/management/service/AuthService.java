package com.restaurant.management.service;

import com.restaurant.management.payload.AdminCreateUserRequest;
import com.restaurant.management.payload.AdminCreateUserResponse;
import com.restaurant.management.payload.JwtResponse;
import com.restaurant.management.payload.LoginRequest;
import com.restaurant.management.payload.SignupRequest;

/**
 * Service interface for authentication operations.
 */
public interface AuthService {
    
    /**
     * Authenticates a user and returns JWT response.
     * 
     * @param loginRequest the login credentials
     * @return JWT response with user details
     */
    JwtResponse authenticateUser(LoginRequest loginRequest);
    
    /**
     * Registers a new user in the system.
     * 
     * @param signupRequest the user registration details
     * @return success message
     */
    String registerUser(SignupRequest signupRequest);
    
    /**
     * Creates a new user account by admin with specified roles.
     * 
     * @param adminCreateUserRequest the user creation details
     * @return response with user details and credentials
     */
    AdminCreateUserResponse createUserByAdmin(AdminCreateUserRequest adminCreateUserRequest);
}