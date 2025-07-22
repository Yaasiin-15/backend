package com.restaurant.management.service;

import com.restaurant.management.exception.ValidationException;
import com.restaurant.management.model.MenuItem;
import java.util.List;

/**
 * Service interface for menu item operations.
 * Provides methods for managing restaurant menu items.
 */
public interface MenuService {
    
    /**
     * Retrieves all menu items.
     * 
     * @return list of all menu items
     */
    List<MenuItem> getAllMenuItems();
    
    /**
     * Retrieves a menu item by its ID.
     * 
     * @param id the menu item ID
     * @return the menu item
//     * @throws MenuItemNotFoundException if menu item not found
     */
    MenuItem getMenuItemById(Long id);
    
    /**
     * Retrieves menu items by category.
     * 
     * @param category the category name
     * @return list of menu items in the category
     */
    List<MenuItem> getMenuItemsByCategory(String category);
    
    /**
     * Retrieves all available menu items.
     * 
     * @return list of available menu items
     */
    List<MenuItem> getAvailableMenuItems();
    
    /**
     * Creates a new menu item.
     * 
     * @param menuItem the menu item to create
     * @return the created menu item
     * @throws ValidationException if menu item data is invalid
     */
    MenuItem createMenuItem(MenuItem menuItem);
    
    /**
     * Updates an existing menu item.
     * 
     * @param id the menu item ID
     * @param menuItemDetails the updated menu item details
     * @return the updated menu item
     * @throws MenuItemNotFoundException if menu item not found
     * @throws ValidationException if menu item data is invalid
     */
    MenuItem updateMenuItem(Long id, MenuItem menuItemDetails);
    
    /**
     * Deletes a menu item.
     * 
     * @param id the menu item ID
     * @throws MenuItemNotFoundException if menu item not found
     */
    void deleteMenuItem(Long id);
}