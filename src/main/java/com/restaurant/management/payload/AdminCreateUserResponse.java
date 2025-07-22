package com.restaurant.management.payload;

/**
 * Response payload for admin user creation.
 * Contains the created user details and credentials.
 */
public class AdminCreateUserResponse {
    
    private String message;
    private String username;
    private String password;
    private String fullName;
    private String email;

    public AdminCreateUserResponse() {}

    public AdminCreateUserResponse(String message, String username, String password, String fullName, String email) {
        this.message = message;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
    }

    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}