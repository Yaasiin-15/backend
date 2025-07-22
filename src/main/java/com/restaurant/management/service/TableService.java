package com.restaurant.management.service;

import com.restaurant.management.model.RestaurantTable;
import com.restaurant.management.model.TableStatus;
import java.util.List;

/**
 * Service interface for restaurant table operations.
 * Provides methods for managing restaurant tables.
 */
public interface TableService {
    
    /**
     * Retrieves all tables.
     * 
     * @return list of all tables
     */
    List<RestaurantTable> getAllTables();
    
    /**
     * Retrieves a table by its ID.
     * 
     * @param id the table ID
     * @return the table
     * @throws ResourceNotFoundException if table not found
     */
    RestaurantTable getTableById(Long id);
    
    /**
     * Retrieves tables by status.
     * 
     * @param status the table status
     * @return list of tables with the specified status
     */
    List<RestaurantTable> getTablesByStatus(TableStatus status);
    
    /**
     * Retrieves tables by capacity.
     * 
     * @param capacity the minimum capacity
     * @return list of tables with at least the specified capacity
     */
    List<RestaurantTable> getTablesByCapacity(Integer capacity);
    
    /**
     * Retrieves available tables.
     * 
     * @return list of available tables
     */
    List<RestaurantTable> getAvailableTables();
    
    /**
     * Creates a new table.
     * 
     * @param table the table to create
     * @return the created table
     * @throws ValidationException if table data is invalid
     */
    RestaurantTable createTable(RestaurantTable table);
    
    /**
     * Updates an existing table.
     * 
     * @param id the table ID
     * @param tableDetails the updated table details
     * @return the updated table
     * @throws ResourceNotFoundException if table not found
     * @throws ValidationException if table data is invalid
     */
    RestaurantTable updateTable(Long id, RestaurantTable tableDetails);
    
    /**
     * Updates table status.
     * 
     * @param id the table ID
     * @param status the new status
     * @return the updated table
     * @throws ResourceNotFoundException if table not found
     */
    RestaurantTable updateTableStatus(Long id, TableStatus status);
    
    /**
     * Deletes a table.
     * 
     * @param id the table ID
     * @throws ResourceNotFoundException if table not found
     * @throws TableDeletionException if table cannot be deleted
     */
    void deleteTable(Long id);
}