package com.restaurant.management.util;

import com.restaurant.management.exception.ValidationException;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * Utility class for common validation operations.
 * Provides reusable validation methods across the application.
 */
public class ValidationUtils {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[0-9]{10,15}$"
    );
    
    /**
     * Validates that a string is not null or empty.
     * 
     * @param value the string to validate
     * @param fieldName the field name for error messages
     * @throws ValidationException if validation fails
     */
    public static void validateRequired(String value, String fieldName) {
        if (!StringUtils.hasText(value)) {
            throw new ValidationException(fieldName, "is required");
        }
    }
    
    /**
     * Validates that a value is not null.
     * 
     * @param value the value to validate
     * @param fieldName the field name for error messages
     * @throws ValidationException if validation fails
     */
    public static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new ValidationException(fieldName, "cannot be null");
        }
    }
    
    /**
     * Validates string length.
     * 
     * @param value the string to validate
     * @param fieldName the field name for error messages
     * @param maxLength the maximum allowed length
     * @throws ValidationException if validation fails
     */
    public static void validateLength(String value, String fieldName, int maxLength) {
        if (value != null && value.length() > maxLength) {
            throw new ValidationException(fieldName, 
                String.format("cannot exceed %d characters", maxLength));
        }
    }
    
    /**
     * Validates string length with minimum requirement.
     * 
     * @param value the string to validate
     * @param fieldName the field name for error messages
     * @param minLength the minimum required length
     * @param maxLength the maximum allowed length
     * @throws ValidationException if validation fails
     */
    public static void validateLength(String value, String fieldName, int minLength, int maxLength) {
        if (value != null) {
            if (value.length() < minLength) {
                throw new ValidationException(fieldName, 
                    String.format("must be at least %d characters", minLength));
            }
            if (value.length() > maxLength) {
                throw new ValidationException(fieldName, 
                    String.format("cannot exceed %d characters", maxLength));
            }
        }
    }
    
    /**
     * Validates email format.
     * 
     * @param email the email to validate
     * @param fieldName the field name for error messages
     * @throws ValidationException if validation fails
     */
    public static void validateEmail(String email, String fieldName) {
        if (StringUtils.hasText(email) && !EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException(fieldName, "must be a valid email address");
        }
    }
    
    /**
     * Validates phone number format.
     * 
     * @param phone the phone number to validate
     * @param fieldName the field name for error messages
     * @throws ValidationException if validation fails
     */
    public static void validatePhone(String phone, String fieldName) {
        if (StringUtils.hasText(phone) && !PHONE_PATTERN.matcher(phone).matches()) {
            throw new ValidationException(fieldName, "must be a valid phone number");
        }
    }
    
    /**
     * Validates that a number is positive.
     * 
     * @param value the number to validate
     * @param fieldName the field name for error messages
     * @throws ValidationException if validation fails
     */
    public static void validatePositive(Number value, String fieldName) {
        if (value != null && value.doubleValue() <= 0) {
            throw new ValidationException(fieldName, "must be greater than 0");
        }
    }
    
    /**
     * Validates that a number is non-negative.
     * 
     * @param value the number to validate
     * @param fieldName the field name for error messages
     * @throws ValidationException if validation fails
     */
    public static void validateNonNegative(Number value, String fieldName) {
        if (value != null && value.doubleValue() < 0) {
            throw new ValidationException(fieldName, "cannot be negative");
        }
    }
    
    /**
     * Validates that a date is in the future.
     * 
     * @param dateTime the date to validate
     * @param fieldName the field name for error messages
     * @throws ValidationException if validation fails
     */
    public static void validateFutureDate(LocalDateTime dateTime, String fieldName) {
        if (dateTime != null && dateTime.isBefore(LocalDateTime.now())) {
            throw new ValidationException(fieldName, "must be in the future");
        }
    }
    
    /**
     * Validates that start date is before end date.
     * 
     * @param start the start date
     * @param end the end date
     * @throws ValidationException if validation fails
     */
    public static void validateDateRange(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null && start.isAfter(end)) {
            throw new ValidationException("Start date cannot be after end date");
        }
    }
    
    /**
     * Validates that a number is within a specified range.
     * 
     * @param value the number to validate
     * @param fieldName the field name for error messages
     * @param min the minimum value (inclusive)
     * @param max the maximum value (inclusive)
     * @throws ValidationException if validation fails
     */
    public static void validateRange(Number value, String fieldName, double min, double max) {
        if (value != null) {
            double val = value.doubleValue();
            if (val < min || val > max) {
                throw new ValidationException(fieldName, 
                    String.format("must be between %s and %s", min, max));
            }
        }
    }
}