package com.restaurant.management.repository;

import com.restaurant.management.model.ERole;
import com.restaurant.management.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Role entity operations.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    /**
     * Find role by name.
     * 
     * @param name the role name to search for
     * @return Optional containing the role if found
     */
    Optional<Role> findByName(ERole name);
}