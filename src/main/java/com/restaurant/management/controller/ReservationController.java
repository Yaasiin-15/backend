package com.restaurant.management.controller;

import com.restaurant.management.model.Reservation;
import com.restaurant.management.model.ReservationStatus;
import com.restaurant.management.repository.ReservationRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        return reservation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Reservation>> getReservationsByStatus(@PathVariable ReservationStatus status) {
        List<Reservation> reservations = reservationRepository.findByStatus(status);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/table/{tableId}")
    public ResponseEntity<List<Reservation>> getReservationsByTable(@PathVariable Long tableId) {
        List<Reservation> reservations = reservationRepository.findByTableId(tableId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/customer/{phone}")
    public ResponseEntity<List<Reservation>> getReservationsByCustomerPhone(@PathVariable String phone) {
        List<Reservation> reservations = reservationRepository.findByCustomerPhone(phone);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@Valid @RequestBody Reservation reservation) {
        Reservation savedReservation = reservationRepository.save(reservation);
        return ResponseEntity.ok(savedReservation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @Valid @RequestBody Reservation reservationDetails) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            reservation.setCustomerName(reservationDetails.getCustomerName());
            reservation.setCustomerPhone(reservationDetails.getCustomerPhone());
            reservation.setCustomerEmail(reservationDetails.getCustomerEmail());
            reservation.setReservationDate(reservationDetails.getReservationDate());
            reservation.setPartySize(reservationDetails.getPartySize());
            reservation.setTable(reservationDetails.getTable());
            reservation.setStatus(reservationDetails.getStatus());
            reservation.setNotes(reservationDetails.getNotes());
            
            Reservation updatedReservation = reservationRepository.save(reservation);
            return ResponseEntity.ok(updatedReservation);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Reservation> updateReservationStatus(@PathVariable Long id, @RequestBody ReservationStatus status) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            reservation.setStatus(status);
            
            Reservation updatedReservation = reservationRepository.save(reservation);
            return ResponseEntity.ok(updatedReservation);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}