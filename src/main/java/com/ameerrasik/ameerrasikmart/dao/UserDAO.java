package com.ameerrasik.ameerrasikmart.dao;

import com.ameerrasik.ameerrasikmart.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for managing User persistence.
 */
public interface UserDAO {
    /**
     * Finds a user by ID.
     */
    Optional<User> findById(Long id);

    /**
     * Finds a user by email address.
     */
    Optional<User> findByEmail(String email);

    /**
     * Creates a new user record.
     */
    User createUser(User user);

    /**
     * Updates an existing user record.
     */
    boolean updateUser(User user);

    /**
     * Lists all registered users.
     */
    List<User> findAllUsers();
}
