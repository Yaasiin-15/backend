package com.restaurant.management.controller;

import com.restaurant.management.model.MenuItem;
import com.restaurant.management.service.MenuService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST controller for menu item operations.
 * Handles CRUD operations for restaurant menu items.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    /**
     * Retrieves all menu items.
     * 
     * @return list of all menu items
     */
    @GetMapping("/items")
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        logger.debug("Request to get all menu items");
        List<MenuItem> menuItems = menuService.getAllMenuItems();
        return ResponseEntity.ok(menuItems);
    }

    /**
     * Retrieves a menu item by its ID.
     * 
     * @param id the menu item ID
     * @return the menu item
     */
    @GetMapping("/items/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) {
        logger.debug("Request to get menu item with id: {}", id);
        MenuItem menuItem = menuService.getMenuItemById(id);
        return ResponseEntity.ok(menuItem);
    }

    /**
     * Retrieves menu items by category.
     * 
     * @param category the category name
     * @return list of menu items in the category
     */
    @GetMapping("/items/category/{category}")
    public ResponseEntity<List<MenuItem>> getMenuItemsByCategory(@PathVariable String category) {
        logger.debug("Request to get menu items for category: {}", category);
        List<MenuItem> menuItems = menuService.getMenuItemsByCategory(category);
        return ResponseEntity.ok(menuItems);
    }

    /**
     * Retrieves all available menu items.
     * 
     * @return list of available menu items
     */
    @GetMapping("/items/available")
    public ResponseEntity<List<MenuItem>> getAvailableMenuItems() {
        logger.debug("Request to get available menu items");
        List<MenuItem> menuItems = menuService.getAvailableMenuItems();
        return ResponseEntity.ok(menuItems);
    }

    /**
     * Creates a new menu item.
     * 
     * @param menuItem the menu item to create
     * @return the created menu item
     */
    @PostMapping("/items")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<MenuItem> createMenuItem(@Valid @RequestBody MenuItem menuItem) {
        logger.info("Request to create menu item: {}", menuItem.getName());
        MenuItem savedMenuItem = menuService.createMenuItem(menuItem);
        return ResponseEntity.ok(savedMenuItem);
    }

    /**
     * Updates an existing menu item.
     * 
     * @param id the menu item ID
     * @param menuItemDetails the updated menu item details
     * @return the updated menu item
     */
    @PutMapping("/items/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id, @Valid @RequestBody MenuItem menuItemDetails) {
        logger.info("Request to update menu item with id: {}", id);
        MenuItem updatedMenuItem = menuService.updateMenuItem(id, menuItemDetails);
        return ResponseEntity.ok(updatedMenuItem);
    }

    /**
     * Deletes a menu item.
     * 
     * @param id the menu item ID
     * @return success response
     */
    @DeleteMapping("/items/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        logger.info("Request to delete menu item with id: {}", id);
        menuService.deleteMenuItem(id);
        return ResponseEntity.ok().build();
    }
}