package com.restaurant.management.service;

import com.restaurant.management.model.Order;
import com.restaurant.management.model.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for order operations.
 * Provides methods for managing restaurant orders.
 */
public interface OrderService {
    
    /**
     * Retrieves all orders.
     * 
     * @return list of all orders
     */
    List<Order> getAllOrders();
    
    /**
     * Retrieves an order by its ID.
     * 
     * @param id the order ID
     * @return the order
     * @throws OrderNotFoundException if order not found
     */
    Order getOrderById(Long id);
    
    /**
     * Retrieves orders by status.
     * 
     * @param status the order status
     * @return list of orders with the specified status
     */
    List<Order> getOrdersByStatus(OrderStatus status);
    
    /**
     * Retrieves orders by table ID.
     * 
     * @param tableId the table ID
     * @return list of orders for the specified table
     */
    List<Order> getOrdersByTable(Long tableId);
    
    /**
     * Retrieves orders within a date range.
     * 
     * @param start the start date
     * @param end the end date
     * @return list of orders within the date range
     */
    List<Order> getOrdersByDateRange(LocalDateTime start, LocalDateTime end);
    
    /**
     * Creates a new order.
     * 
     * @param order the order to create
     * @return the created order
     * @throws ValidationException if order data is invalid
     * @throws TableNotFoundException if table not found
     */
    Order createOrder(Order order);
    
    /**
     * Updates an existing order.
     * 
     * @param id the order ID
     * @param orderDetails the updated order details
     * @return the updated order
     * @throws OrderNotFoundException if order not found
     * @throws ValidationException if order data is invalid
     */
    Order updateOrder(Long id, Order orderDetails);
    
    /**
     * Updates order status.
     * 
     * @param id the order ID
     * @param status the new status
     * @return the updated order
     * @throws OrderNotFoundException if order not found
     * @throws InvalidStatusTransitionException if status transition is invalid
     */
    Order updateOrderStatus(Long id, OrderStatus status);
    
    /**
     * Deletes an order.
     * 
     * @param id the order ID
     * @throws OrderNotFoundException if order not found
     * @throws OrderDeletionException if order cannot be deleted
     */
    void deleteOrder(Long id);
    
    /**
     * Calculates the total amount for an order.
     * 
     * @param order the order
     * @return the calculated total amount
     */
    Double calculateOrderTotal(Order order);
}