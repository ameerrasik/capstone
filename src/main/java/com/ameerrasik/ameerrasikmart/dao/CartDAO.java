package com.ameerrasik.ameerrasikmart.dao;

import com.ameerrasik.ameerrasikmart.dto.CartItemDTO;
import com.ameerrasik.ameerrasikmart.model.CartItem;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Cart operations.
 */
public interface CartDAO {
    /**
     * Finds a specific cart item by user ID and product ID.
     */
    Optional<CartItem> findCartItem(Long userId, Long productId);

    /**
     * Adds an item to the user's cart or increments quantity if already exists.
     */
    boolean addToCart(Long userId, Long productId, int quantity);

    /**
     * Updates the exact quantity of a cart item.
     */
    boolean updateQuantity(Long userId, Long productId, int quantity);

    /**
     * Removes an item from the user's cart.
     */
    boolean removeFromCart(Long userId, Long productId);

    /**
     * Gets all cart items joined with product details for a user.
     */
    List<CartItemDTO> getCartItemsByUserId(Long userId);

    /**
     * Clears all items in a user's cart using an active Connection (for transaction).
     */
    boolean clearCart(Long userId, Connection conn);

    /**
     * Clears all items in a user's cart directly.
     */
    boolean clearCart(Long userId);
}
