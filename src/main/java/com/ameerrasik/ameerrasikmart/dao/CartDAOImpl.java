package com.ameerrasik.ameerrasikmart.dao;

import com.ameerrasik.ameerrasikmart.dto.CartItemDTO;
import com.ameerrasik.ameerrasikmart.exception.DatabaseException;
import com.ameerrasik.ameerrasikmart.model.CartItem;
import com.ameerrasik.ameerrasikmart.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of CartDAO.
 */
public class CartDAOImpl implements CartDAO {
    private static final Logger logger = LoggerFactory.getLogger(CartDAOImpl.class);

    @Override
    public Optional<CartItem> findCartItem(Long userId, Long productId) {
        String sql = "SELECT id, user_id, product_id, quantity, created_at FROM cart_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            pstmt.setLong(2, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    CartItem item = new CartItem(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getLong("product_id"),
                        rs.getInt("quantity"),
                        rs.getTimestamp("created_at")
                    );
                    return Optional.of(item);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding cart item for user " + userId + ", product " + productId, e);
            throw new DatabaseException("Database error finding cart item", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean addToCart(Long userId, Long productId, int quantity) {
        String upsertSql = "MERGE INTO cart_items (user_id, product_id, quantity) KEY(user_id, product_id) VALUES (?, ?, COALESCE((SELECT quantity FROM cart_items WHERE user_id=? AND product_id=?), 0) + ?)";
        // Fallback standard ANSI SQL check-and-update for maximum H2 / MySQL compatibility:
        Optional<CartItem> existing = findCartItem(userId, productId);
        if (existing.isPresent()) {
            return updateQuantity(userId, productId, existing.get().getQuantity() + quantity);
        }

        String insertSql = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {

            pstmt.setLong(1, userId);
            pstmt.setLong(2, productId);
            pstmt.setInt(3, quantity);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error adding product to cart for user " + userId, e);
            throw new DatabaseException("Database error adding item to cart", e);
        }
    }

    @Override
    public boolean updateQuantity(Long userId, Long productId, int quantity) {
        if (quantity <= 0) {
            return removeFromCart(userId, productId);
        }

        String sql = "UPDATE cart_items SET quantity = ? WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, quantity);
            pstmt.setLong(2, userId);
            pstmt.setLong(3, productId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating cart quantity for user " + userId, e);
            throw new DatabaseException("Database error updating cart item", e);
        }
    }

    @Override
    public boolean removeFromCart(Long userId, Long productId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            pstmt.setLong(2, productId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error removing cart item for user " + userId, e);
            throw new DatabaseException("Database error removing cart item", e);
        }
    }

    @Override
    public List<CartItemDTO> getCartItemsByUserId(Long userId) {
        List<CartItemDTO> items = new ArrayList<>();
        String sql = "SELECT c.id as cart_id, c.user_id, c.product_id, c.quantity, "
                   + "p.name as product_name, p.price as unit_price, p.stock_qty as available_stock, "
                   + "p.category, p.image_url "
                   + "FROM cart_items c "
                   + "JOIN products p ON c.product_id = p.id "
                   + "WHERE c.user_id = ? "
                   + "ORDER BY c.created_at ASC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CartItemDTO dto = new CartItemDTO();
                    dto.setId(rs.getLong("cart_id"));
                    dto.setUserId(rs.getLong("user_id"));
                    dto.setProductId(rs.getLong("product_id"));
                    dto.setQuantity(rs.getInt("quantity"));
                    dto.setProductName(rs.getString("product_name"));
                    dto.setUnitPrice(rs.getBigDecimal("unit_price"));
                    dto.setAvailableStock(rs.getInt("available_stock"));
                    dto.setCategory(rs.getString("category"));
                    dto.setImageUrl(rs.getString("image_url"));
                    dto.calculateSubtotal();
                    items.add(dto);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching cart items for user " + userId, e);
            throw new DatabaseException("Database error fetching user cart", e);
        }
        return items;
    }

    @Override
    public boolean clearCart(Long userId, Connection conn) {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            return pstmt.executeUpdate() >= 0;
        } catch (SQLException e) {
            logger.error("Error clearing cart inside transaction for user " + userId, e);
            throw new DatabaseException("Database error clearing cart", e);
        }
    }

    @Override
    public boolean clearCart(Long userId) {
        try (Connection conn = DBUtil.getConnection()) {
            return clearCart(userId, conn);
        } catch (SQLException e) {
            logger.error("Error clearing cart for user " + userId, e);
            throw new DatabaseException("Database error clearing cart", e);
        }
    }
}
