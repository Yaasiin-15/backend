package com.restaurant.management.service.impl;

import com.restaurant.management.exception.AuthenticationException;
import com.restaurant.management.exception.ResourceNotFoundException;
import com.restaurant.management.exception.UserAlreadyExistsException;
import com.restaurant.management.model.ERole;
import com.restaurant.management.model.Role;
import com.restaurant.management.model.User;
import com.restaurant.management.payload.AdminCreateUserRequest;
import com.restaurant.management.payload.AdminCreateUserResponse;
import com.restaurant.management.payload.JwtResponse;
import com.restaurant.management.payload.LoginRequest;
import com.restaurant.management.payload.SignupRequest;
import com.restaurant.management.repository.RoleRepository;
import com.restaurant.management.repository.UserRepository;
import com.restaurant.management.security.JwtUtils;
import com.restaurant.management.security.UserDetailsImpl;
import com.restaurant.management.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of AuthService for handling authentication operations.
 * Provides user authentication and registration functionality.
 */
@Service
@Transactional
public class AuthServiceImpl implements AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder encoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        logger.info("Attempting to authenticate user: {}", loginRequest.getUsername());
        
        try {
            // Authenticate user credentials
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );
            
            // Set authentication in security context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Generate JWT token
            String jwt = jwtUtils.generateJwtToken(authentication);
            
            // Extract user details
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
            
            logger.info("User {} authenticated successfully", loginRequest.getUsername());
            
            return new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
            );
            
        } catch (BadCredentialsException e) {
            logger.warn("Authentication failed for user: {}", loginRequest.getUsername());
            throw new AuthenticationException("Invalid username or password");
        } catch (Exception e) {
            logger.error("Authentication error for user: {}", loginRequest.getUsername(), e);
            throw new AuthenticationException("Authentication failed", e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String registerUser(SignupRequest signupRequest) {
        logger.info("Attempting to register user: {}", signupRequest.getUsername());
        
        // Check if username already exists
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            logger.warn("Registration failed - username already exists: {}", signupRequest.getUsername());
            throw new UserAlreadyExistsException("username", signupRequest.getUsername());
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            logger.warn("Registration failed - email already exists: {}", signupRequest.getEmail());
            throw new UserAlreadyExistsException("email", signupRequest.getEmail());
        }
        
        try {
            // Create new user
            User user = new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                encoder.encode(signupRequest.getPassword()),
                signupRequest.getFirstName(),
                signupRequest.getLastName()
            );
            
            user.setPhone(signupRequest.getPhone());
            
            // Assign roles
            Set<Role> roles = assignRoles(signupRequest.getRole());
            user.setRoles(roles);
            
            // Save user
            userRepository.save(user);
            
            logger.info("User {} registered successfully", signupRequest.getUsername());
            return "User registered successfully!";
            
        } catch (Exception e) {
            logger.error("Registration error for user: {}", signupRequest.getUsername(), e);
            throw new RuntimeException("User registration failed", e);
        }
    }
    
    /**
     * Assigns roles to a user based on the provided role strings.
     * 
     * @param strRoles set of role strings
     * @return set of Role entities
     */
    private Set<Role> assignRoles(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();
        
        if (strRoles == null || strRoles.isEmpty()) {
            // Default role is STAFF
            Role userRole = roleRepository.findByName(ERole.ROLE_STAFF)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", "ROLE_STAFF"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role.toLowerCase()) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new ResourceNotFoundException("Role", "name", "ROLE_ADMIN"));
                        roles.add(adminRole);
                        break;
                    case "manager":
                        Role managerRole = roleRepository.findByName(ERole.ROLE_MANAGER)
                            .orElseThrow(() -> new ResourceNotFoundException("Role", "name", "ROLE_MANAGER"));
                        roles.add(managerRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_STAFF)
                            .orElseThrow(() -> new ResourceNotFoundException("Role", "name", "ROLE_STAFF"));
                        roles.add(userRole);
                }
            });
        }
        
        return roles;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public AdminCreateUserResponse createUserByAdmin(AdminCreateUserRequest adminCreateUserRequest) {
        logger.info("Admin attempting to create user: {}", adminCreateUserRequest.getUsername());
        
        // Check if username already exists
        if (userRepository.existsByUsername(adminCreateUserRequest.getUsername())) {
            logger.warn("User creation failed - username already exists: {}", adminCreateUserRequest.getUsername());
            throw new UserAlreadyExistsException("username", adminCreateUserRequest.getUsername());
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(adminCreateUserRequest.getEmail())) {
            logger.warn("User creation failed - email already exists: {}", adminCreateUserRequest.getEmail());
            throw new UserAlreadyExistsException("email", adminCreateUserRequest.getEmail());
        }
        
        try {
            // Create new user
            User user = new User(
                adminCreateUserRequest.getUsername(),
                adminCreateUserRequest.getEmail(),
                encoder.encode(adminCreateUserRequest.getPassword()),
                adminCreateUserRequest.getFirstName(),
                adminCreateUserRequest.getLastName()
            );
            
            user.setPhone(adminCreateUserRequest.getPhone());
            
            // Assign roles
            Set<Role> roles = assignRoles(adminCreateUserRequest.getRoles());
            user.setRoles(roles);
            
            // Save user
            userRepository.save(user);
            
            logger.info("User {} created successfully by admin", adminCreateUserRequest.getUsername());
            
            String fullName = adminCreateUserRequest.getFirstName() + " " + adminCreateUserRequest.getLastName();
            return new AdminCreateUserResponse(
                "User created successfully",
                adminCreateUserRequest.getUsername(),
                adminCreateUserRequest.getPassword(),
                fullName,
                adminCreateUserRequest.getEmail()
            );
            
        } catch (Exception e) {
            logger.error("User creation error for user: {}", adminCreateUserRequest.getUsername(), e);
            throw new RuntimeException("User creation failed", e);
        }
    }
}