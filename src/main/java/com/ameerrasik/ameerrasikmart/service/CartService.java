package com.ameerrasik.ameerrasikmart.service;

import com.ameerrasik.ameerrasikmart.dto.CartItemDTO;
import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for Buyer shopping cart management.
 */
public interface CartService {
    /**
     * Adds a product to the buyer's cart with stock validation.
     */
    boolean addToCart(Long userId, Long productId, int quantity);

    /**
     * Updates quantity of a product in cart.
     */
    boolean updateQuantity(Long userId, Long productId, int quantity);

    /**
     * Removes an item from cart.
     */
    boolean removeFromCart(Long userId, Long productId);

    /**
     * Gets all cart items for buyer.
     */
    List<CartItemDTO> getCartItems(Long userId);

    /**
     * Computes grand total for buyer cart.
     */
    BigDecimal getCartTotal(Long userId);

    /**
     * Clears all items in buyer cart.
     */
    boolean clearCart(Long userId);
}
