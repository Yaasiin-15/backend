package com.restaurant.management.repository;

import com.restaurant.management.model.Order;
import com.restaurant.management.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Order entity operations.
 * Provides data access methods for order management and reporting.
 * 
 * Features:
 * - Standard CRUD operations via JpaRepository
 * - Status-based order filtering for kitchen workflow
 * - Table and user association queries
 * - Date range queries for reporting
 * - Custom JPQL queries for complex filtering
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * Finds all orders with a specific status.
     * Used for kitchen workflow management and order tracking.
     * 
     * @param status the order status to filter by
     * @return list of orders with the specified status
     */
    List<Order> findByStatus(OrderStatus status);
    
    /**
     * Finds all orders associated with a specific table.
     * Used for table management and order history.
     * 
     * @param tableId the table ID to filter by
     * @return list of orders for the specified table
     */
    List<Order> findByTableId(Long tableId);
    
    /**
     * Finds all orders created by a specific user.
     * Used for staff performance tracking and order history.
     * 
     * @param userId the user ID to filter by
     * @return list of orders created by the specified user
     */
    List<Order> findByUserId(Long userId);
    
    /**
     * Finds orders created within a specific date range.
     * Used for reporting and analytics purposes.
     * 
     * @param start the start date and time
     * @param end the end date and time
     * @return list of orders created within the date range
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN ?1 AND ?2")
    List<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    /**
     * Finds orders with specific statuses, ordered by creation date (newest first).
     * Used for dashboard displays and priority-based order management.
     * 
     * @param statuses list of order statuses to include
     * @return list of orders with specified statuses, ordered by creation date descending
     */
    @Query("SELECT o FROM Order o WHERE o.status IN ?1 ORDER BY o.createdAt DESC")
    List<Order> findByStatusInOrderByCreatedAtDesc(List<OrderStatus> statuses);
}