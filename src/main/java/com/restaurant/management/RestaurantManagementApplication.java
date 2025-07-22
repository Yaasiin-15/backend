package com.restaurant.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Restaurant Management System.
 * This is the entry point for the Spring Boot application.
 * 
 * Features:
 * - Spring Boot auto-configuration
 * - Component scanning for all packages under com.restaurant.management
 * - Automatic configuration of JPA, Security, Web MVC, and other Spring modules
 * - Database initialization on startup
 * - JWT-based authentication system
 * - RESTful API endpoints for restaurant operations
 * 
 * The application provides a complete backend solution for restaurant management
 * including user authentication, menu management, order processing, table management,
 * and reservation handling.
 */
@SpringBootApplication
public class RestaurantManagementApplication {
    
    /**
     * Main method to start the Spring Boot application.
     * Initializes the Spring application context and starts the embedded web server.
     * 
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(RestaurantManagementApplication.class, args);
        
        System.out.println("🍽️  Restaurant Management System Started Successfully!");
        System.out.println("📊 API Documentation: http://localhost:8080/api");
        System.out.println("🔐 Default Admin: admin / admin123");
        System.out.println("🌐 CORS Enabled for Frontend Integration");
    }
}