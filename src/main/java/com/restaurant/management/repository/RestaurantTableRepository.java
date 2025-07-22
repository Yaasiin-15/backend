package com.restaurant.management.repository;

import com.restaurant.management.model.RestaurantTable;
import com.restaurant.management.model.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
    Optional<RestaurantTable> findByTableNumber(Integer tableNumber);
    List<RestaurantTable> findByStatus(TableStatus status);
    List<RestaurantTable> findByCapacityGreaterThanEqual(Integer capacity);
    Boolean existsByTableNumber(Integer tableNumber);
}