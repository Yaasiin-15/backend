package com.restaurant.management.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * Request payload for user registration.
 * Contains all necessary information to create a new user account in the system.
 * 
 * Validation rules:
 * - Username: 3-20 characters, required
 * - Email: Valid email format, max 50 characters, required
 * - Password: 6-40 characters, required
 * - First/Last Name: Max 50 characters each, required
 * - Phone: Max 15 characters, optional
 * - Roles: Optional set of role names (defaults to STAFF if not provided)
 */
public class SignupRequest {
    
    /** Username for the new account (3-20 characters, alphanumeric + underscore) */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    /** Email address for the new account (must be unique and valid format) */
    @NotBlank(message = "Email is required")
    @Size(max = 50, message = "Email cannot exceed 50 characters")
    @Email(message = "Email must be valid")
    private String email;

    /** Set of role names to assign to the user (optional, defaults to STAFF) */
    private Set<String> role;

    /** Password for the new account (6-40 characters, will be encrypted) */
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 40, message = "Password must be between 6 and 40 characters")
    private String password;

    /** User's first name */
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    /** User's last name */
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    /** User's phone number (optional) */
    @Size(max = 15, message = "Phone number cannot exceed 15 characters")
    private String phone;

    // Getters and Setters with documentation
    
    /** @return the username for the new account */
    public String getUsername() { return username; }
    
    /** @param username the username to set (3-20 characters) */
    public void setUsername(String username) { this.username = username; }

    /** @return the email address for the new account */
    public String getEmail() { return email; }
    
    /** @param email the email address to set (must be valid format) */
    public void setEmail(String email) { this.email = email; }

    /** @return the password for the new account */
    public String getPassword() { return password; }
    
    /** @param password the password to set (6-40 characters, will be encrypted) */
    public void setPassword(String password) { this.password = password; }

    /** @return the set of role names to assign to the user */
    public Set<String> getRole() { return this.role; }
    
    /** @param role the set of role names to assign (optional, defaults to STAFF) */
    public void setRole(Set<String> role) { this.role = role; }

    /** @return the user's first name */
    public String getFirstName() { return firstName; }
    
    /** @param firstName the user's first name to set */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /** @return the user's last name */
    public String getLastName() { return lastName; }
    
    /** @param lastName the user's last name to set */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /** @return the user's phone number */
    public String getPhone() { return phone; }
    
    /** @param phone the user's phone number to set (optional) */
    public void setPhone(String phone) { this.phone = phone; }
}