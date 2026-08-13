package com.ameerrasik.ameerrasikmart.service;

import com.ameerrasik.ameerrasikmart.dao.ReviewDAO;
import com.ameerrasik.ameerrasikmart.dao.ReviewDAOImpl;
import com.ameerrasik.ameerrasikmart.exception.ValidationException;
import com.ameerrasik.ameerrasikmart.model.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Implementation of ReviewService logic.
 */
public class ReviewServiceImpl implements ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewDAO reviewDAO;

    public ReviewServiceImpl() {
        this.reviewDAO = new ReviewDAOImpl();
    }

    public ReviewServiceImpl(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    @Override
    public Review addReview(Long productId, Long userId, Integer rating, String comment) {
        if (productId == null || userId == null) {
            throw new ValidationException("Product and User identification are required.");
        }
        if (rating == null || rating < 1 || rating > 5) {
            throw new ValidationException("Rating must be an integer between 1 and 5.");
        }
        if (comment == null || comment.trim().isEmpty()) {
            throw new ValidationException("Review comment cannot be empty.");
        }

        Review review = new Review(null, productId, userId, rating, comment.trim(), null);
        Review created = reviewDAO.addReview(review);
        logger.info("Added review ID: {} for Product ID: {} by User ID: {}", created.getId(), productId, userId);
        return created;
    }

    @Override
    public List<Review> getReviewsByProduct(Long productId) {
        return reviewDAO.findByProductId(productId);
    }

    @Override
    public Double getAverageRating(Long productId) {
        return reviewDAO.getAverageRating(productId);
    }
}
