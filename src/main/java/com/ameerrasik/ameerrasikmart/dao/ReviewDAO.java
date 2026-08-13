package com.ameerrasik.ameerrasikmart.dao;

import com.ameerrasik.ameerrasikmart.model.Review;
import java.util.List;

/**
 * Data Access Object interface for Review management.
 */
public interface ReviewDAO {
    /**
     * Adds a buyer review for a product.
     */
    Review addReview(Review review);

    /**
     * Retrieves all reviews for a product joined with reviewer names.
     */
    List<Review> findByProductId(Long productId);

    /**
     * Gets average rating for a product.
     */
    Double getAverageRating(Long productId);
}
