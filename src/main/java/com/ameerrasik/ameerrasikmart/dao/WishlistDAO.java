package com.ameerrasik.ameerrasikmart.dao;

import com.ameerrasik.ameerrasikmart.dto.WishlistItemDTO;
import com.ameerrasik.ameerrasikmart.model.WishlistItem;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Data Access Object interface for Wishlist operations.
 */
public interface WishlistDAO {
    /**
     * Finds a wishlist item for a specific user and product.
     */
    Optional<WishlistItem> findWishlistItem(Long userId, Long productId);

    /**
     * Adds a product to user's wishlist.
     */
    boolean addToWishlist(Long userId, Long productId);

    /**
     * Removes a product from user's wishlist.
     */
    boolean removeFromWishlist(Long userId, Long productId);

    /**
     * Checks if a product is in user's wishlist.
     */
    boolean isInWishlist(Long userId, Long productId);

    /**
     * Gets all wishlist items for a user with joined product details.
     */
    List<WishlistItemDTO> getWishlistByUserId(Long userId);

    /**
     * Gets all product IDs currently in the user's wishlist.
     */
    Set<Long> getWishlistProductIds(Long userId);

    /**
     * Gets total count of wishlisted items for a user.
     */
    int getWishlistCount(Long userId);

    /**
     * Clears all items in user's wishlist.
     */
    boolean clearWishlist(Long userId);
}
