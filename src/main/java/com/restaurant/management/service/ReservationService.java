package com.restaurant.management.service;

import com.restaurant.management.model.Reservation;
import com.restaurant.management.model.ReservationStatus;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for reservation operations.
 * Provides methods for managing restaurant reservations.
 */
public interface ReservationService {
    
    /**
     * Retrieves all reservations.
     * 
     * @return list of all reservations
     */
    List<Reservation> getAllReservations();
    
    /**
     * Retrieves a reservation by its ID.
     * 
     * @param id the reservation ID
     * @return the reservation
     * @throws ResourceNotFoundException if reservation not found
     */
    Reservation getReservationById(Long id);
    
    /**
     * Retrieves reservations by status.
     * 
     * @param status the reservation status
     * @return list of reservations with the specified status
     */
    List<Reservation> getReservationsByStatus(ReservationStatus status);
    
    /**
     * Retrieves reservations by table ID.
     * 
     * @param tableId the table ID
     * @return list of reservations for the specified table
     */
    List<Reservation> getReservationsByTable(Long tableId);
    
    /**
     * Retrieves reservations within a date range.
     * 
     * @param start the start date
     * @param end the end date
     * @return list of reservations within the date range
     */
    List<Reservation> getReservationsByDateRange(LocalDateTime start, LocalDateTime end);
    
    /**
     * Creates a new reservation.
     * 
     * @param reservation the reservation to create
     * @return the created reservation
     * @throws ValidationException if reservation data is invalid
     * @throws TableNotAvailableException if table is not available
     */
    Reservation createReservation(Reservation reservation);
    
    /**
     * Updates an existing reservation.
     * 
     * @param id the reservation ID
     * @param reservationDetails the updated reservation details
     * @return the updated reservation
     * @throws ResourceNotFoundException if reservation not found
     * @throws ValidationException if reservation data is invalid
     */
    Reservation updateReservation(Long id, Reservation reservationDetails);
    
    /**
     * Updates reservation status.
     * 
     * @param id the reservation ID
     * @param status the new status
     * @return the updated reservation
     * @throws ResourceNotFoundException if reservation not found
     * @throws InvalidStatusTransitionException if status transition is invalid
     */
    Reservation updateReservationStatus(Long id, ReservationStatus status);
    
    /**
     * Deletes a reservation.
     * 
     * @param id the reservation ID
     * @throws ResourceNotFoundException if reservation not found
     * @throws ReservationDeletionException if reservation cannot be deleted
     */
    void deleteReservation(Long id);
    
    /**
     * Checks if a table is available for a given time slot.
     * 
     * @param tableId the table ID
     * @param reservationTime the reservation time
     * @param duration the duration in minutes
     * @return true if table is available, false otherwise
     */
    boolean isTableAvailable(Long tableId, LocalDateTime reservationTime, Integer duration);
}