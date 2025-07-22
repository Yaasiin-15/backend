package com.restaurant.management.payload;

import java.util.List;

/**
 * Response payload for JWT authentication.
 * Contains the JWT token and user information returned after successful authentication.
 * 
 * This response is sent to the client after successful login and includes:
 * - JWT access token for subsequent API requests
 * - Token type (Bearer)
 * - User identification and profile information
 * - User roles for frontend authorization decisions
 */
public class JwtResponse {
    
    /** The JWT access token for API authentication */
    private String token;
    
    /** Token type, always "Bearer" for JWT tokens */
    private String type = "Bearer";
    
    /** User's unique identifier */
    private Long id;
    
    /** User's username for display purposes */
    private String username;
    
    /** User's email address */
    private String email;
    
    /** List of roles assigned to the user for authorization */
    private List<String> roles;

    /**
     * Constructor for creating a JWT response.
     * 
     * @param accessToken the JWT access token
     * @param id the user's unique identifier
     * @param username the user's username
     * @param email the user's email address
     * @param roles list of roles assigned to the user
     */
    public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    // Getters and Setters with documentation
    
    /** @return the JWT access token */
    public String getToken() { return token; }
    
    /** @param token the JWT access token to set */
    public void setToken(String token) { this.token = token; }
    
    /** @return the JWT access token (alias for getToken) */
    public String getAccessToken() { return token; }
    
    /** @param accessToken the JWT access token to set (alias for setToken) */
    public void setAccessToken(String accessToken) { this.token = accessToken; }

    /** @return the token type (always "Bearer") */
    public String getTokenType() { return type; }
    
    /** @param tokenType the token type to set */
    public void setTokenType(String tokenType) { this.type = tokenType; }

    /** @return the user's unique identifier */
    public Long getId() { return id; }
    
    /** @param id the user's unique identifier to set */
    public void setId(Long id) { this.id = id; }

    /** @return the user's email address */
    public String getEmail() { return email; }
    
    /** @param email the user's email address to set */
    public void setEmail(String email) { this.email = email; }

    /** @return the user's username */
    public String getUsername() { return username; }
    
    /** @param username the user's username to set */
    public void setUsername(String username) { this.username = username; }

    /** @return the list of roles assigned to the user */
    public List<String> getRoles() { return roles; }
}