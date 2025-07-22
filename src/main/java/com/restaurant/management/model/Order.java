package com.restaurant.management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a customer order in the restaurant management system.
 * Contains order details, status tracking, and relationships to tables and users.
 * 
 * Features:
 * - Order status workflow management (PENDING -> CONFIRMED -> PREPARING -> READY -> DELIVERED -> COMPLETED)
 * - Table and user associations for tracking
 * - Customer information for delivery/contact
 * - Order items collection with cascade operations
 * - Precise monetary calculations using BigDecimal
 * - Audit timestamps for order lifecycle tracking
 */
@Entity
@Table(name = "orders")
public class Order {
    
    /** Primary key identifier for the order */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Reference to the restaurant table where the order was placed (optional for takeout) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    private RestaurantTable table;

    /** Reference to the staff member who created/manages the order */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /** Current status of the order in the workflow */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private OrderStatus status = OrderStatus.PENDING;

    /** Total amount for the order calculated from all order items */
    @NotNull(message = "Total amount is required")
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /** Additional notes or special instructions for the order */
    @Column(length = 500)
    private String notes;

    /** Customer's name for the order (useful for takeout/delivery) */
    @Column(name = "customer_name", length = 100)
    private String customerName;

    /** Customer's phone number for contact purposes */
    @Column(name = "customer_phone", length = 15)
    private String customerPhone;

    /** Timestamp when the order was created */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** Timestamp when the order was last updated */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** Collection of items included in this order */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {
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

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
}