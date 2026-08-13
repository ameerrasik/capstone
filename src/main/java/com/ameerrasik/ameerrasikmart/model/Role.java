package com.ameerrasik.ameerrasikmart.model;

/**
 * Represents user roles within AmeerRasik Mart.
 */
public enum Role {
    BUYER,
    SELLER,
    ADMIN;

    public static Role fromString(String value) {
        if (value == null) {
            return BUYER;
        }
        try {
            return Role.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return BUYER;
        }
    }
}
