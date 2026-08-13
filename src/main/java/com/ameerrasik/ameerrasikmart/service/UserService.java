package com.ameerrasik.ameerrasikmart.service;

import com.ameerrasik.ameerrasikmart.model.Role;
import com.ameerrasik.ameerrasikmart.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for User account management and authentication.
 */
public interface UserService {
    /**
     * Registers a new user account (BUYER or SELLER).
     */
    User registerUser(String name, String email, String password, String confirmPassword, Role role);

    /**
     * Authenticates user credentials.
     */
    User loginUser(String email, String password);

    /**
     * Retrieves user by ID.
     */
    Optional<User> getUserById(Long id);

    /**
     * Retrieves all users for Admin management.
     */
    List<User> getAllUsers();
}
