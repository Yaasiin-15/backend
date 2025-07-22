package com.restaurant.management.config;

import com.restaurant.management.security.AuthEntryPointJwt;
import com.restaurant.management.security.AuthTokenFilter;
import com.restaurant.management.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

/**
 * Web Security Configuration for the Restaurant Management System.
 * Configures JWT-based authentication, CORS, and method-level security.
 * 
 * Features:
 * - JWT token-based authentication
 * - CORS configuration for frontend integration
 * - Method-level security with @PreAuthorize annotations
 * - BCrypt password encoding
 * - Stateless session management
 * - Custom authentication entry point for unauthorized access
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    
    /** Custom user details service for authentication */
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    /** Custom authentication entry point for handling unauthorized access */
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    /** Allowed origins for CORS configuration from application properties */
    @Value("${app.cors.allowedOrigins:http://localhost:5173,http://localhost:3000}")
    private String allowedOrigins;

    /**
     * Creates the JWT authentication filter bean.
     * This filter intercepts requests to validate JWT tokens.
     * 
     * @return AuthTokenFilter instance for JWT validation
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(jwtUtils(), userDetailsService);
    }
    
    @Bean
    public com.restaurant.management.security.JwtUtils jwtUtils() {
        return new com.restaurant.management.security.JwtUtils();
    }

    /**
     * Configures the DAO authentication provider.
     * Combines user details service with password encoder for authentication.
     * 
     * @return DaoAuthenticationProvider configured with user service and password encoder
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Provides the authentication manager bean.
     * Required for manual authentication in controllers.
     * 
     * @param authConfig the authentication configuration
     * @return AuthenticationManager instance
     * @throws Exception if configuration fails
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configures the password encoder using BCrypt.
     * BCrypt is a strong hashing function designed for password storage.
     * 
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the main security filter chain.
     * Sets up CORS, CSRF, authentication, and authorization rules.
     * 
     * Key configurations:
     * - CORS enabled for frontend integration
     * - CSRF disabled for stateless API
     * - JWT-based stateless authentication
     * - Public access to auth endpoints
     * - All other endpoints require authentication
     * 
     * @param http the HttpSecurity configuration object
     * @return SecurityFilterChain configured for the application
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth ->
                auth.requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/test/**").permitAll()
                    .anyRequest().authenticated()
            );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configures CORS (Cross-Origin Resource Sharing) settings.
     * Allows the frontend application to make requests to the backend API.
     * 
     * Configuration includes:
     * - Allowed origins from application properties
     * - All HTTP methods (GET, POST, PUT, DELETE, OPTIONS)
     * - All headers allowed
     * - Credentials support enabled
     * 
     * @return CorsConfigurationSource with configured CORS settings
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow specific origins
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:3000"));
        // Do NOT use setAllowedOriginPatterns when allowCredentials is true
        // configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:*")); // REMOVE THIS LINE

        // Allow all HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));
        
        // Allow all headers
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Expose headers that frontend might need
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        
        // Set max age for preflight requests
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}