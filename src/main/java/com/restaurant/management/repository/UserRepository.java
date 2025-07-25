package com.restaurant.management.repository;

import com.restaurant.management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by username.
     * 
     * @param username the username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email.
     * 
     * @param email the email to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if user exists by username.
     * 
     * @param username the username to check
     * @return true if user exists, false otherwise
     */
    Boolean existsByUsername(String username);
    
    /**
     * Check if user exists by email.
     * 
     * @param email the email to check
     * @return true if user exists, false otherwise
     */
    Boolean existsByEmail(String email);
}