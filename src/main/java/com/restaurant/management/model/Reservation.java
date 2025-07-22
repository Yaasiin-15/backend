package com.restaurant.management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Entity representing a table reservation in the restaurant management system.
 * Manages customer reservations with table assignments and status tracking.
 * 
 * Features:
 * - Customer contact information management
 * - Table assignment and party size tracking
 * - Reservation status workflow (PENDING -> CONFIRMED -> SEATED -> COMPLETED -> CANCELLED)
 * - Date and time scheduling
 * - Special notes and requirements
 * - Audit timestamps for reservation lifecycle
 */
@Entity
@Table(name = "reservations")
public class Reservation {
    
    /** Primary key identifier for the reservation */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Reference to the assigned restaurant table */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    private RestaurantTable table;

    /** Customer's full name for the reservation (required) */
    @NotBlank(message = "Customer name is required")
    @Column(name = "customer_name", length = 100)
    private String customerName;

    /** Customer's phone number for contact and confirmation (required) */
    @NotBlank(message = "Customer phone is required")
    @Column(name = "customer_phone", length = 15)
    private String customerPhone;

    /** Customer's email address for confirmation and updates (optional) */
    @Column(name = "customer_email", length = 100)
    private String customerEmail;

    /** Date and time of the reservation */
    @NotNull(message = "Reservation date is required")
    @Column(name = "reservation_date")
    private LocalDateTime reservationDate;

    /** Number of people in the party */
    @NotNull(message = "Party size is required")
    @Column(name = "party_size")
    private Integer partySize;

    /** Current status of the reservation */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ReservationStatus status = ReservationStatus.PENDING;

    /** Special notes, dietary requirements, or other customer requests */
    @Column(length = 500)
    private String notes;

    /** Timestamp when the reservation was created */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** Timestamp when the reservation was last updated */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Reservation() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RestaurantTable getTable() { return table; }
    public void setTable(RestaurantTable table) { this.table = table; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public LocalDateTime getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDateTime reservationDate) { this.reservationDate = reservationDate; }

    public Integer getPartySize() { return partySize; }
    public void setPartySize(Integer partySize) { this.partySize = partySize; }

    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}