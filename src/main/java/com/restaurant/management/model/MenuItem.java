package com.restaurant.management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a menu item in the restaurant management system.
 * Contains all information about food and beverage items available for ordering.
 * 
 * Features:
 * - Categorized menu items with precise pricing using BigDecimal
 * - Availability status management for inventory control
 * - Preparation time tracking for kitchen operations
 * - Image URL support for visual presentation
 * - Audit timestamps for tracking changes
 */
@Entity
@Table(name = "menu_items")
public class MenuItem {
    
    /** Primary key identifier for the menu item */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name of the menu item (required, max 100 characters) */
    @NotBlank(message = "Menu item name is required")
    @Column(length = 100)
    private String name;

    /** Detailed description of the menu item (optional, max 500 characters) */
    @Column(length = 500)
    private String description;

    /** Price of the menu item using BigDecimal for precise monetary calculations */
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    /** Category classification for menu organization (e.g., "Appetizers", "Main Course") */
    @NotBlank(message = "Category is required")
    @Column(length = 50)
    private String category;

    /** URL to the menu item's image for display purposes (optional) */
    @Column(name = "image_url")
    private String imageUrl;

    /** Availability status - true if item can be ordered, false if out of stock */
    @Column(name = "is_available")
    private Boolean isAvailable = true;

    /** Estimated preparation time in minutes for kitchen planning */
    @Column(name = "preparation_time")
    private Integer preparationTime;

    /** Timestamp when the menu item was created */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /** Timestamp when the menu item was last updated */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public MenuItem() {
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

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }

    public Integer getPreparationTime() { return preparationTime; }
    public void setPreparationTime(Integer preparationTime) { this.preparationTime = preparationTime; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}