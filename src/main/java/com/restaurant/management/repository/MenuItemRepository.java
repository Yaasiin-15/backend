package com.restaurant.management.repository;

import com.restaurant.management.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for MenuItem entity operations.
 * Provides data access methods for menu management and customer ordering.
 * 
 * Features:
 * - Standard CRUD operations via JpaRepository
 * - Category-based menu filtering for organization
 * - Availability-based filtering for active menu items
 * - Combined category and availability queries for customer-facing menus
 */
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    
    /**
     * Finds all menu items in a specific category.
     * Used for organizing menu displays by category (e.g., Appetizers, Main Course, Desserts).
     * 
     * @param category the category name to filter by
     * @return list of menu items in the specified category
     */
    List<MenuItem> findByCategory(String category);
    
    /**
     * Finds all menu items based on availability status.
     * Used to show only available items to customers or manage inventory.
     * 
     * @param isAvailable the availability status (true for available, false for unavailable)
     * @return list of menu items with the specified availability status
     */
    List<MenuItem> findByIsAvailable(Boolean isAvailable);
    
    /**
     * Finds menu items by both category and availability status.
     * Used for customer-facing menus to show only available items in specific categories.
     * 
     * @param category the category name to filter by
     * @param isAvailable the availability status to filter by
     * @return list of menu items matching both category and availability criteria
     */
    List<MenuItem> findByCategoryAndIsAvailable(String category, Boolean isAvailable);
}