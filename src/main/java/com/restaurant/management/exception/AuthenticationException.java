package com.restaurant.management.exception;

/**
 * Exception thrown when authentication fails.
 */
public class AuthenticationException extends RestaurantManagementException {
    
    public AuthenticationException(String message) {
        super(message, "AUTHENTICATION_ERROR");
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, "AUTHENTICATION_ERROR", cause);
    }
}