package com.restaurant.management.exception;

/**
 * Base exception class for restaurant management system.
 * All custom exceptions should extend this class.
 */
public class RestaurantManagementException extends RuntimeException {
    
    private final String errorCode;
    
    public RestaurantManagementException(String message) {
        super(message);
        this.errorCode = "GENERAL_ERROR";
    }
    
    public RestaurantManagementException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public RestaurantManagementException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "GENERAL_ERROR";
    }
    
    public RestaurantManagementException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}