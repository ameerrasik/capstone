package com.ameerrasik.ameerrasikmart.service;

import com.ameerrasik.ameerrasikmart.dto.WishlistItemDTO;

import java.util.List;
import java.util.Set;

/**
 * Service interface for managing buyer wishlist operations.
 */
public interface WishlistService {
    /**
     * Adds a product to the buyer's wishlist.
     */
    boolean addToWishlist(Long userId, Long productId);

    /**
     * Removes a product from the buyer's wishlist.
     */
    boolean removeFromWishlist(Long userId, Long productId);

    /**
     * Toggles a product's presence in the wishlist.
     *
     * @return true if item is now in the wishlist (added), false if removed
     */
    boolean toggleWishlist(Long userId, Long productId);

    /**
     * Checks if a product is in buyer's wishlist.
     */
    boolean isInWishlist(Long userId, Long productId);

    /**
     * Gets all wishlist items for a buyer.
     */
    List<WishlistItemDTO> getWishlistItems(Long userId);

    /**
     * Gets all product IDs in buyer's wishlist.
     */
    Set<Long> getWishlistProductIds(Long userId);

    /**
     * Gets count of items in buyer's wishlist.
     */
    int getWishlistCount(Long userId);

    /**
     * Moves a wishlisted product to the buyer's cart and removes it from the wishlist.
     */
    boolean moveToCart(Long userId, Long productId, int quantity);

    /**
     * Clears all items in buyer's wishlist.
     */
    boolean clearWishlist(Long userId);
}
