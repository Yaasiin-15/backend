package com.restaurant.management.exception;

/**
 * Exception thrown when validation fails.
 */
public class ValidationException extends RestaurantManagementException {
    
    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }
    
    public ValidationException(String field, String message) {
        super(String.format("Validation failed for field '%s': %s", field, message), "VALIDATION_ERROR");
    }
}