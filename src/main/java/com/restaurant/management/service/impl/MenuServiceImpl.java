package com.restaurant.management.service.impl;

import com.restaurant.management.exception.ResourceNotFoundException;
import com.restaurant.management.exception.ValidationException;
import com.restaurant.management.model.MenuItem;
import com.restaurant.management.repository.MenuItemRepository;
import com.restaurant.management.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementation of MenuService for handling menu item operations.
 * Provides comprehensive menu management functionality.
 */
@Service
@Transactional
public class MenuServiceImpl implements MenuService {
    
    private static final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);
    
    @Autowired
    private MenuItemRepository menuItemRepository;
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<MenuItem> getAllMenuItems() {
        logger.debug("Retrieving all menu items");
        return menuItemRepository.findAll();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public MenuItem getMenuItemById(Long id) {
        logger.debug("Retrieving menu item with id: {}", id);
        
        if (id == null) {
            throw new ValidationException("Menu item ID cannot be null");
        }
        
        return menuItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "id", id));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<MenuItem> getMenuItemsByCategory(String category) {
        logger.debug("Retrieving menu items for category: {}", category);
        
        if (!StringUtils.hasText(category)) {
            throw new ValidationException("Category cannot be empty");
        }
        
        return menuItemRepository.findByCategory(category);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<MenuItem> getAvailableMenuItems() {
        logger.debug("Retrieving available menu items");
        return menuItemRepository.findByIsAvailable(true);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public MenuItem createMenuItem(MenuItem menuItem) {
        logger.info("Creating new menu item: {}", menuItem.getName());
        
        validateMenuItem(menuItem);
        
        try {
            MenuItem savedMenuItem = menuItemRepository.save(menuItem);
            logger.info("Menu item created successfully with id: {}", savedMenuItem.getId());
            return savedMenuItem;
        } catch (Exception e) {
            logger.error("Error creating menu item: {}", menuItem.getName(), e);
            throw new RuntimeException("Failed to create menu item", e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public MenuItem updateMenuItem(Long id, MenuItem menuItemDetails) {
        logger.info("Updating menu item with id: {}", id);
        
        if (id == null) {
            throw new ValidationException("Menu item ID cannot be null");
        }
        
        validateMenuItem(menuItemDetails);
        
        MenuItem existingMenuItem = menuItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "id", id));
        
        try {
            // Update fields
            existingMenuItem.setName(menuItemDetails.getName());
            existingMenuItem.setDescription(menuItemDetails.getDescription());
            existingMenuItem.setPrice(menuItemDetails.getPrice());
            existingMenuItem.setCategory(menuItemDetails.getCategory());
            existingMenuItem.setImageUrl(menuItemDetails.getImageUrl());
            existingMenuItem.setIsAvailable(menuItemDetails.getIsAvailable());
            existingMenuItem.setPreparationTime(menuItemDetails.getPreparationTime());
            
            MenuItem updatedMenuItem = menuItemRepository.save(existingMenuItem);
            logger.info("Menu item updated successfully: {}", updatedMenuItem.getName());
            return updatedMenuItem;
            
        } catch (Exception e) {
            logger.error("Error updating menu item with id: {}", id, e);
            throw new RuntimeException("Failed to update menu item", e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMenuItem(Long id) {
        logger.info("Deleting menu item with id: {}", id);
        
        if (id == null) {
            throw new ValidationException("Menu item ID cannot be null");
        }
        
        if (!menuItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("MenuItem", "id", id);
        }
        
        try {
            menuItemRepository.deleteById(id);
            logger.info("Menu item deleted successfully with id: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting menu item with id: {}", id, e);
            throw new RuntimeException("Failed to delete menu item", e);
        }
    }
    
    /**
     * Validates menu item data.
     * 
     * @param menuItem the menu item to validate
     * @throws ValidationException if validation fails
     */
    private void validateMenuItem(MenuItem menuItem) {
        if (menuItem == null) {
            throw new ValidationException("Menu item cannot be null");
        }
        
        if (!StringUtils.hasText(menuItem.getName())) {
            throw new ValidationException("name", "Menu item name is required");
        }
        
        if (menuItem.getName().length() > 100) {
            throw new ValidationException("name", "Menu item name cannot exceed 100 characters");
        }
        
        if (menuItem.getPrice() == null || menuItem.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new ValidationException("price", "Menu item price must be greater than 0");
        }
        
        if (!StringUtils.hasText(menuItem.getCategory())) {
            throw new ValidationException("category", "Menu item category is required");
        }
        
        if (menuItem.getPreparationTime() != null && menuItem.getPreparationTime() < 0) {
            throw new ValidationException("preparationTime", "Preparation time cannot be negative");
        }
        
        if (menuItem.getDescription() != null && menuItem.getDescription().length() > 500) {
            throw new ValidationException("description", "Description cannot exceed 500 characters");
        }
    }
}