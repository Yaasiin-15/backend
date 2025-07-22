package com.restaurant.management.controller;

import com.restaurant.management.model.RestaurantTable;
import com.restaurant.management.model.TableStatus;
import com.restaurant.management.repository.RestaurantTableRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tables")
public class TableController {

    @Autowired
    private RestaurantTableRepository tableRepository;

    @GetMapping
    public ResponseEntity<List<RestaurantTable>> getAllTables() {
        List<RestaurantTable> tables = tableRepository.findAll();
        return ResponseEntity.ok(tables);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantTable> getTableById(@PathVariable Long id) {
        Optional<RestaurantTable> table = tableRepository.findById(id);
        return table.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RestaurantTable>> getTablesByStatus(@PathVariable TableStatus status) {
        List<RestaurantTable> tables = tableRepository.findByStatus(status);
        return ResponseEntity.ok(tables);
    }

    @GetMapping("/capacity/{capacity}")
    public ResponseEntity<List<RestaurantTable>> getTablesByCapacity(@PathVariable Integer capacity) {
        List<RestaurantTable> tables = tableRepository.findByCapacityGreaterThanEqual(capacity);
        return ResponseEntity.ok(tables);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> createTable(@Valid @RequestBody RestaurantTable table) {
        if (tableRepository.existsByTableNumber(table.getTableNumber())) {
            return ResponseEntity.badRequest()
                    .body("Error: Table number " + table.getTableNumber() + " already exists!");
        }
        
        RestaurantTable savedTable = tableRepository.save(table);
        return ResponseEntity.ok(savedTable);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<RestaurantTable> updateTable(@PathVariable Long id, @Valid @RequestBody RestaurantTable tableDetails) {
        Optional<RestaurantTable> optionalTable = tableRepository.findById(id);
        
        if (optionalTable.isPresent()) {
            RestaurantTable table = optionalTable.get();
            
            // Check if table number is being changed and if it already exists
            if (!table.getTableNumber().equals(tableDetails.getTableNumber()) &&
                tableRepository.existsByTableNumber(tableDetails.getTableNumber())) {
                return ResponseEntity.badRequest().build();
            }
            
            table.setTableNumber(tableDetails.getTableNumber());
            table.setCapacity(tableDetails.getCapacity());
            table.setLocation(tableDetails.getLocation());
            
            RestaurantTable updatedTable = tableRepository.save(table);
            return ResponseEntity.ok(updatedTable);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<RestaurantTable> updateTableStatus(@PathVariable Long id, @RequestBody TableStatus status) {
        Optional<RestaurantTable> optionalTable = tableRepository.findById(id);
        
        if (optionalTable.isPresent()) {
            RestaurantTable table = optionalTable.get();
            table.setStatus(status);
            
            RestaurantTable updatedTable = tableRepository.save(table);
            return ResponseEntity.ok(updatedTable);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> deleteTable(@PathVariable Long id) {
        if (tableRepository.existsById(id)) {
            tableRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}