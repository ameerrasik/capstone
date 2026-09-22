package com.ameerrasik.ameerrasikmart.dao;

import com.ameerrasik.ameerrasikmart.dto.WishlistItemDTO;
import com.ameerrasik.ameerrasikmart.exception.DatabaseException;
import com.ameerrasik.ameerrasikmart.model.WishlistItem;
import com.ameerrasik.ameerrasikmart.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * JDBC implementation of WishlistDAO.
 */
public class WishlistDAOImpl implements WishlistDAO {
    private static final Logger logger = LoggerFactory.getLogger(WishlistDAOImpl.class);

    @Override
    public Optional<WishlistItem> findWishlistItem(Long userId, Long productId) {
        String sql = "SELECT id, user_id, product_id, created_at FROM wishlist_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            pstmt.setLong(2, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    WishlistItem item = new WishlistItem(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getLong("product_id"),
                        rs.getTimestamp("created_at")
                    );
                    return Optional.of(item);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding wishlist item for user " + userId + ", product " + productId, e);
            throw new DatabaseException("Database error finding wishlist item", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean addToWishlist(Long userId, Long productId) {
        if (isInWishlist(userId, productId)) {
            return true; // Already wishlisted
        }

        String insertSql = "INSERT INTO wishlist_items (user_id, product_id) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {

            pstmt.setLong(1, userId);
            pstmt.setLong(2, productId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error adding product to wishlist for user " + userId, e);
            throw new DatabaseException("Database error adding item to wishlist", e);
        }
    }

    @Override
    public boolean removeFromWishlist(Long userId, Long productId) {
        String sql = "DELETE FROM wishlist_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            pstmt.setLong(2, productId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error removing product from wishlist for user " + userId, e);
            throw new DatabaseException("Database error removing item from wishlist", e);
        }
    }

    @Override
    public boolean isInWishlist(Long userId, Long productId) {
        String sql = "SELECT 1 FROM wishlist_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            pstmt.setLong(2, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            logger.error("Error checking wishlist status for user " + userId + ", product " + productId, e);
            throw new DatabaseException("Database error checking wishlist status", e);
        }
    }

    @Override
    public List<WishlistItemDTO> getWishlistByUserId(Long userId) {
        List<WishlistItemDTO> items = new ArrayList<>();
        String sql = "SELECT w.id as wishlist_id, w.user_id, w.product_id, w.created_at, "
                   + "p.name as product_name, p.price as unit_price, p.stock_qty as available_stock, "
                   + "p.category, p.image_url "
                   + "FROM wishlist_items w "
                   + "JOIN products p ON w.product_id = p.id "
                   + "WHERE w.user_id = ? "
                   + "ORDER BY w.created_at DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    WishlistItemDTO dto = new WishlistItemDTO();
                    dto.setId(rs.getLong("wishlist_id"));
                    dto.setUserId(rs.getLong("user_id"));
                    dto.setProductId(rs.getLong("product_id"));
                    dto.setProductName(rs.getString("product_name"));
                    dto.setUnitPrice(rs.getBigDecimal("unit_price"));
                    dto.setAvailableStock(rs.getInt("available_stock"));
                    dto.setCategory(rs.getString("category"));
                    dto.setImageUrl(rs.getString("image_url"));
                    dto.setCreatedAt(rs.getTimestamp("created_at"));
                    items.add(dto);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching wishlist items for user " + userId, e);
            throw new DatabaseException("Database error fetching wishlist items", e);
        }
        return items;
    }

    @Override
    public Set<Long> getWishlistProductIds(Long userId) {
        Set<Long> productIds = new HashSet<>();
        if (userId == null) {
            return productIds;
        }

        String sql = "SELECT product_id FROM wishlist_items WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    productIds.add(rs.getLong("product_id"));
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching wishlist product IDs for user " + userId, e);
            throw new DatabaseException("Database error fetching wishlist product IDs", e);
        }
        return productIds;
    }

    @Override
    public int getWishlistCount(Long userId) {
        if (userId == null) {
            return 0;
        }

        String sql = "SELECT COUNT(*) FROM wishlist_items WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.error("Error counting wishlist items for user " + userId, e);
            throw new DatabaseException("Database error counting wishlist items", e);
        }
        return 0;
    }

    @Override
    public boolean clearWishlist(Long userId) {
        String sql = "DELETE FROM wishlist_items WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            return pstmt.executeUpdate() >= 0;
        } catch (SQLException e) {
            logger.error("Error clearing wishlist for user " + userId, e);
            throw new DatabaseException("Database error clearing wishlist", e);
        }
    }
}
