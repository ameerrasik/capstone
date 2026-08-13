package com.ameerrasik.ameerrasikmart.dao;

import com.ameerrasik.ameerrasikmart.exception.DatabaseException;
import com.ameerrasik.ameerrasikmart.model.Review;
import com.ameerrasik.ameerrasikmart.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of ReviewDAO.
 */
public class ReviewDAOImpl implements ReviewDAO {
    private static final Logger logger = LoggerFactory.getLogger(ReviewDAOImpl.class);

    @Override
    public Review addReview(Review review) {
        String sql = "INSERT INTO reviews (product_id, user_id, rating, comment) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, review.getProductId());
            pstmt.setLong(2, review.getUserId());
            pstmt.setInt(3, review.getRating());
            pstmt.setString(4, review.getComment());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Adding review failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    review.setId(generatedKeys.getLong(1));
                }
            }
            return review;
        } catch (SQLException e) {
            logger.error("Error adding review for product " + review.getProductId(), e);
            throw new DatabaseException("Database error adding review", e);
        }
    }

    @Override
    public List<Review> findByProductId(Long productId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.id, r.product_id, r.user_id, r.rating, r.comment, r.created_at, "
                   + "u.name as user_name "
                   + "FROM reviews r "
                   + "JOIN users u ON r.user_id = u.id "
                   + "WHERE r.product_id = ? "
                   + "ORDER BY r.created_at DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Review r = new Review();
                    r.setId(rs.getLong("id"));
                    r.setProductId(rs.getLong("product_id"));
                    r.setUserId(rs.getLong("user_id"));
                    r.setRating(rs.getInt("rating"));
                    r.setComment(rs.getString("comment"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    r.setUserName(rs.getString("user_name"));
                    reviews.add(r);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching reviews for product " + productId, e);
            throw new DatabaseException("Database error fetching reviews", e);
        }
        return reviews;
    }

    @Override
    public Double getAverageRating(Long productId) {
        String sql = "SELECT AVG(rating) FROM reviews WHERE product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            logger.error("Error calculating average rating for product " + productId, e);
            throw new DatabaseException("Database error calculating rating", e);
        }
        return 0.0;
    }
}
