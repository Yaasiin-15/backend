package com.restaurant.management.repository;

import com.restaurant.management.model.Reservation;
import com.restaurant.management.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByTableId(Long tableId);
    List<Reservation> findByCustomerPhone(String customerPhone);
    
    @Query("SELECT r FROM Reservation r WHERE r.reservationDate BETWEEN ?1 AND ?2")
    List<Reservation> findByReservationDateBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT r FROM Reservation r WHERE r.reservationDate >= ?1 AND r.status = ?2 ORDER BY r.reservationDate ASC")
    List<Reservation> findUpcomingReservations(LocalDateTime now, ReservationStatus status);
}