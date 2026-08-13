package com.ameerrasik.ameerrasikmart.model;

/**
 * Represents life-cycle status of orders in AmeerRasik Mart.
 */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    public static OrderStatus fromString(String value) {
        if (value == null) {
            return PENDING;
        }
        try {
            return OrderStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return PENDING;
        }
    }
}
