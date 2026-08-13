package com.ameerrasik.ameerrasikmart.service;

import com.ameerrasik.ameerrasikmart.dao.UserDAO;
import com.ameerrasik.ameerrasikmart.dao.UserDAOImpl;
import com.ameerrasik.ameerrasikmart.exception.AuthenticationException;
import com.ameerrasik.ameerrasikmart.exception.ValidationException;
import com.ameerrasik.ameerrasikmart.model.Role;
import com.ameerrasik.ameerrasikmart.model.User;
import com.ameerrasik.ameerrasikmart.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Implementation of UserService logic.
 */
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private final UserDAO userDAO;

    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User registerUser(String name, String email, String password, String confirmPassword, Role role) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Full name is required.");
        }
        if (email == null || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new ValidationException("A valid email address is required.");
        }
        if (password == null || password.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters long.");
        }
        if (!password.equals(confirmPassword)) {
            throw new ValidationException("Password and confirm password do not match.");
        }
        if (role == null || role == Role.ADMIN) {
            throw new ValidationException("Public registration is restricted to BUYER or SELLER roles.");
        }

        String normalizedEmail = email.trim().toLowerCase();
        if (userDAO.findByEmail(normalizedEmail).isPresent()) {
            throw new ValidationException("An account with email " + normalizedEmail + " already exists.");
        }

        String hashedPassword = PasswordUtil.hashPassword(password);
        User user = new User(null, name.trim(), normalizedEmail, hashedPassword, role, null);
        User createdUser = userDAO.createUser(user);
        logger.info("Registered new user with ID: {}, Role: {}", createdUser.getId(), createdUser.getRole());
        return createdUser;
    }

    @Override
    public User loginUser(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email address is required.");
        }
        if (password == null || password.isEmpty()) {
            throw new ValidationException("Password is required.");
        }

        String normalizedEmail = email.trim().toLowerCase();
        Optional<User> userOpt = userDAO.findByEmail(normalizedEmail);
        if (userOpt.isEmpty()) {
            logger.warn("Failed login attempt for non-existent email: {}", normalizedEmail);
            throw new AuthenticationException("Invalid email or password.");
        }

        User user = userOpt.get();
        if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            logger.warn("Failed login attempt due to invalid password for email: {}", normalizedEmail);
            throw new AuthenticationException("Invalid email or password.");
        }

        logger.info("User logged in successfully: {}, Role: {}", user.getEmail(), user.getRole());
        return user;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userDAO.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.findAllUsers();
    }
}
