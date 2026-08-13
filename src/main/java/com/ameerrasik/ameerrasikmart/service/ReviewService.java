package com.ameerrasik.ameerrasikmart.service;

import com.ameerrasik.ameerrasikmart.model.Review;
import java.util.List;

/**
 * Service interface for Product review management.
 */
public interface ReviewService {
    /**
     * Adds a buyer review for a product.
     */
    Review addReview(Long productId, Long userId, Integer rating, String comment);

    /**
     * Retrieves reviews for a product.
     */
    List<Review> getReviewsByProduct(Long productId);

    /**
     * Gets average rating for a product.
     */
    Double getAverageRating(Long productId);
}
