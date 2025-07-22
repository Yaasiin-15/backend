package com.restaurant.management.service.impl;

import com.restaurant.management.exception.ResourceNotFoundException;
import com.restaurant.management.exception.ValidationException;
import com.restaurant.management.model.Order;
import com.restaurant.management.model.OrderItem;
import com.restaurant.management.model.OrderStatus;
import com.restaurant.management.repository.OrderRepository;
import com.restaurant.management.repository.RestaurantTableRepository;
import com.restaurant.management.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of OrderService for handling order operations.
 * Provides comprehensive order management functionality.
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private RestaurantTableRepository tableRepository;
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        logger.debug("Retrieving all orders");
        return orderRepository.findAll();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        logger.debug("Retrieving order with id: {}", id);
        
        if (id == null) {
            throw new ValidationException("Order ID cannot be null");
        }
        
        return orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByStatus(OrderStatus status) {
        logger.debug("Retrieving orders with status: {}", status);
        
        if (status == null) {
            throw new ValidationException("Order status cannot be null");
        }
        
        return orderRepository.findByStatus(status);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByTable(Long tableId) {
        logger.debug("Retrieving orders for table: {}", tableId);
        
        if (tableId == null) {
            throw new ValidationException("Table ID cannot be null");
        }
        
        // Verify table exists
        if (!tableRepository.existsById(tableId)) {
            throw new ResourceNotFoundException("Table", "id", tableId);
        }
        
        return orderRepository.findByTableId(tableId);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByDateRange(LocalDateTime start, LocalDateTime end) {
        logger.debug("Retrieving orders between {} and {}", start, end);
        
        if (start == null || end == null) {
            throw new ValidationException("Start and end dates cannot be null");
        }
        
        if (start.isAfter(end)) {
            throw new ValidationException("Start date cannot be after end date");
        }
        
        return orderRepository.findByCreatedAtBetween(start, end);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Order createOrder(Order order) {
        Long tableId = order.getTable() != null ? order.getTable().getId() : null;
        logger.info("Creating new order for table: {}", tableId);
        
        validateOrder(order);
        
        // Verify tacd s
        if (order.getTable() != null && !tableRepository.existsById(order.getTable().getId())) {
            throw new ResourceNotFoundException("Table", "id", order.getTable().getId());
        }
        
        try {
            // Set default values
            if (order.getStatus() == null) {
                order.setStatus(OrderStatus.PENDING);
            }
            
            if (order.getCreatedAt() == null) {
                order.setCreatedAt(LocalDateTime.now());
            }
            
            // Calculate total amount
            Double totalAmount = calculateOrderTotal(order);
            order.setTotalAmount(java.math.BigDecimal.valueOf(totalAmount));
            
            Order savedOrder = orderRepository.save(order);
            logger.info("Order created successfully with id: {}", savedOrder.getId());
            return savedOrder;
            
        } catch (Exception e) {
            logger.error("Error creating order for table: {}", tableId, e);
            throw new RuntimeException("Failed to create order", e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Order updateOrder(Long id, Order orderDetails) {
        logger.info("Updating order with id: {}", id);
        
        if (id == null) {
            throw new ValidationException("Order ID cannot be null");
        }
        
        validateOrder(orderDetails);
        
        Order existingOrder = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        
        try {
            // Update fields
            existingOrder.setStatus(orderDetails.getStatus());
            existingOrder.setNotes(orderDetails.getNotes());
            existingOrder.setCustomerName(orderDetails.getCustomerName());
            existingOrder.setCustomerPhone(orderDetails.getCustomerPhone());
            
            // Recalculate total if order items changed
            if (orderDetails.getOrderItems() != null) {
                existingOrder.setOrderItems(orderDetails.getOrderItems());
                Double totalAmount = calculateOrderTotal(existingOrder);
                existingOrder.setTotalAmount(java.math.BigDecimal.valueOf(totalAmount));
            }
            
            existingOrder.setUpdatedAt(LocalDateTime.now());
            
            Order updatedOrder = orderRepository.save(existingOrder);
            logger.info("Order updated successfully with id: {}", updatedOrder.getId());
            return updatedOrder;
            
        } catch (Exception e) {
            logger.error("Error updating order with id: {}", id, e);
            throw new RuntimeException("Failed to update order", e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Order updateOrderStatus(Long id, OrderStatus status) {
        logger.info("Updating order status for id: {} to {}", id, status);
        
        if (id == null) {
            throw new ValidationException("Order ID cannot be null");
        }
        
        if (status == null) {
            throw new ValidationException("Order status cannot be null");
        }
        
        Order existingOrder = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        
        // Validate status transition
        validateStatusTransition(existingOrder.getStatus(), status);
        
        try {
            existingOrder.setStatus(status);
            existingOrder.setUpdatedAt(LocalDateTime.now());
            
            Order updatedOrder = orderRepository.save(existingOrder);
            logger.info("Order status updated successfully for id: {}", updatedOrder.getId());
            return updatedOrder;
            
        } catch (Exception e) {
            logger.error("Error updating order status for id: {}", id, e);
            throw new RuntimeException("Failed to update order status", e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteOrder(Long id) {
        logger.info("Deleting order with id: {}", id);
        
        if (id == null) {
            throw new ValidationException("Order ID cannot be null");
        }
        
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        
        // Check if order can be deleted
        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.SERVED) {
            throw new ValidationException("Cannot delete completed or served orders");
        }
        
        try {
            orderRepository.deleteById(id);
            logger.info("Order deleted successfully with id: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting order with id: {}", id, e);
            throw new RuntimeException("Failed to delete order", e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Double calculateOrderTotal(Order order) {
        if (order == null || order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            return 0.0;
        }
        
        return order.getOrderItems().stream()
            .mapToDouble(item -> item.getUnitPrice().doubleValue() * item.getQuantity())
            .sum();
    }
    
    /**
     * Validates order data.
     * 
     * @param order the order to validate
     * @throws ValidationException if validation fails
     */
    private void validateOrder(Order order) {
        if (order == null) {
            throw new ValidationException("Order cannot be null");
        }
        
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new ValidationException("Order must have at least one item");
        }
        
        // Validate order items
        for (OrderItem item : order.getOrderItems()) {
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new ValidationException("Order item quantity must be greater than 0");
            }
            
            if (item.getUnitPrice() == null || item.getUnitPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new ValidationException("Order item price must be greater than 0");
            }
        }
        
        if (order.getCustomerName() != null && order.getCustomerName().length() > 100) {
            throw new ValidationException("Customer name cannot exceed 100 characters");
        }
        
        if (order.getCustomerPhone() != null && order.getCustomerPhone().length() > 20) {
            throw new ValidationException("Customer phone cannot exceed 20 characters");
        }
    }
    
    /**
     * Validates order status transitions.
     * 
     * @param currentStatus the current order status
     * @param newStatus the new order status
     * @throws ValidationException if transition is invalid
     */
    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {
        // Define valid transitions
        List<OrderStatus> validTransitions;
        
        switch (currentStatus) {
            case PENDING:
                validTransitions = Arrays.asList(OrderStatus.CONFIRMED, OrderStatus.CANCELLED);
                break;
            case CONFIRMED:
                validTransitions = Arrays.asList(OrderStatus.PREPARING, OrderStatus.CANCELLED);
                break;
            case PREPARING:
                validTransitions = Arrays.asList(OrderStatus.READY, OrderStatus.CANCELLED);
                break;
            case READY:
                validTransitions = Arrays.asList(OrderStatus.SERVED, OrderStatus.COMPLETED);
                break;
            case SERVED:
                validTransitions = Arrays.asList(OrderStatus.COMPLETED);
                break;
            case COMPLETED:
            case CANCELLED:
                validTransitions = Arrays.asList(); // No transitions allowed
                break;
            default:
                throw new ValidationException("Unknown order status: " + currentStatus);
        }
        
        if (!validTransitions.contains(newStatus)) {
            throw new ValidationException(
                String.format("Invalid status transition from %s to %s", currentStatus, newStatus)
            );
        }
    }
}