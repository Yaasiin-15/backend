package com.restaurant.management.exception;

/**
 * Exception thrown when a table is not available for reservation.
 */
public class TableNotAvailableException extends RestaurantManagementException {
    
    public TableNotAvailableException(Long tableId, String timeSlot) {
        super(String.format("Table %d is not available for %s", tableId, timeSlot), "TABLE_NOT_AVAILABLE");
    }
    
    public TableNotAvailableException(String message) {
        super(message, "TABLE_NOT_AVAILABLE");
    }
}