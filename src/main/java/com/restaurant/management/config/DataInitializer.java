package com.restaurant.management.config;

import com.restaurant.management.model.*;
import com.restaurant.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Data Initializer component for the Restaurant Management System.
 * Automatically populates the database with essential data on application startup.
 * 
 * Features
 * - Creates default admin user for system access
 * - Runs only once when database is empty
 * - Uses CommandLineRunner for startup execution
 * 
 * This ensures the system has the necessary foundation data to operate properly.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    /** Repository for managing user roles */
    @Autowired
    private RoleRepository roleRepository;

    /** Repository for managing users */
    @Autowired
    private UserRepository userRepository;

    /** Repository for managing menu items */
    @Autowired
    private MenuItemRepository menuItemRepository;

    /** Repository for managing restaurant tables */
    @Autowired
    private RestaurantTableRepository tableRepository;

    /** Password encoder for securing user passwords */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Executes the data initialization process on application startup.
     * This method is called automatically by Spring Boot after the application context is loaded.
     * 
     * @param args command line arguments (not used)
     * @throws Exception if initialization fails
     */
    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeUsers();
    }

    /**
     * Initializes the default user roles in the system.
     * Creates ADMIN, MANAGER, and STAFF roles if they don't exist.
     * These roles are essential for the role-based access control system.
     */
    private void initializeRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
            roleRepository.save(new Role(ERole.ROLE_MANAGER));
            roleRepository.save(new Role(ERole.ROLE_STAFF));
            System.out.println("✓ Default roles initialized (ADMIN, MANAGER, STAFF)");
        }
    }

    /**
     * Initializes the default admin user for system access.
     * Creates an admin user with full system privileges if no users exist.
     * This ensures there's always a way to access the system initially.
     * 
     * Default credentials:
     * - Username: admin
     * - Password: admin123
     * - Role: ADMIN
     */
    private void initializeUsers() {
        if (userRepository.count() == 0) {
            // Create admin user with encoded password
            User admin = new User("admin", "admin@restaurant.com", 
                passwordEncoder.encode("admin123"), "Admin", "User");
            
            // Assign admin role
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Admin role not found"));
            admin.setRoles(Set.of(adminRole));
            
            userRepository.save(admin);

            System.out.println("✓ Default admin user created:");
            System.out.println("  Username: admin");
            System.out.println("  Password: admin123");
            System.out.println("  Role: ADMIN");
        }
    }


}