package com.restaurant.management.exception;

/**
 * Exception thrown when an invalid status transition is attempted.
 */
public class InvalidStatusTransitionException extends RestaurantManagementException {
    
    public InvalidStatusTransitionException(String currentStatus, String newStatus) {
        super(String.format("Invalid status transition from %s to %s", currentStatus, newStatus), 
              "INVALID_STATUS_TRANSITION");
    }
    
    public InvalidStatusTransitionException(String message) {
        super(message, "INVALID_STATUS_TRANSITION");
    }
}