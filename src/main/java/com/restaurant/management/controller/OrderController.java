package com.restaurant.management.controller;

import com.restaurant.management.model.Order;
import com.restaurant.management.model.OrderStatus;
import com.restaurant.management.service.OrderService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for order operations.
 * Handles CRUD operations for restaurant orders.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    /**
     * Retrieves all orders.
     * 
     * @return list of all orders
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        logger.debug("Request to get all orders");
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves an order by its ID.
     * 
     * @param id the order ID
     * @return the order
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        logger.debug("Request to get order with id: {}", id);
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * Retrieves orders by status.
     * 
     * @param status the order status
     * @return list of orders with the specified status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable OrderStatus status) {
        logger.debug("Request to get orders with status: {}", status);
        List<Order> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves orders by table ID.
     * 
     * @param tableId the table ID
     * @return list of orders for the specified table
     */
    @GetMapping("/table/{tableId}")
    public ResponseEntity<List<Order>> getOrdersByTable(@PathVariable Long tableId) {
        logger.debug("Request to get orders for table: {}", tableId);
        List<Order> orders = orderService.getOrdersByTable(tableId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves orders within a date range.
     * 
     * @param start the start date
     * @param end the end date
     * @return list of orders within the date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<Order>> getOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        logger.debug("Request to get orders between {} and {}", start, end);
        List<Order> orders = orderService.getOrdersByDateRange(start, end);
        return ResponseEntity.ok(orders);
    }

    /**
     * Creates a new order.
     * 
     * @param order the order to create
     * @return the created order
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) {
        Long tableId = order.getTable() != null ? order.getTable().getId() : null;
        logger.info("Request to create order for table: {}", tableId);
        Order savedOrder = orderService.createOrder(order);
        return ResponseEntity.ok(savedOrder);
    }

    /**
     * Updates an existing order.
     * 
     * @param id the order ID
     * @param orderDetails the updated order details
     * @return the updated order
     */
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @Valid @RequestBody Order orderDetails) {
        logger.info("Request to update order with id: {}", id);
        Order updatedOrder = orderService.updateOrder(id, orderDetails);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * Updates order status.
     * 
     * @param id the order ID
     * @param status the new status
     * @return the updated order
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatus status) {
        logger.info("Request to update order status for id: {} to {}", id, status);
        Order updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * Deletes an order.
     * 
     * @param id the order ID
     * @return success response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        logger.info("Request to delete order with id: {}", id);
        orderService.deleteOrder(id);
        return ResponseEntity.ok().build();
    }
}