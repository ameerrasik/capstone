package com.ameerrasik.ameerrasikmart.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for hashing and verifying passwords using BCrypt.
 */
public class PasswordUtil {

    private PasswordUtil() {
    }

    /**
     * Hashes a raw plaintext password using BCrypt.
     *
     * @param plainTextPassword The raw password
     * @return Hashed password string
     */
    public static String hashPassword(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(10));
    }

    /**
     * Verifies a raw plaintext password against a stored BCrypt hash.
     *
     * @param plainTextPassword Raw password string
     * @param hashedPassword Hashed password string from database
     * @return true if matches, false otherwise
     */
    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        if (plainTextPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainTextPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
