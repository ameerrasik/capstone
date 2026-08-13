package com.ameerrasik.ameerrasikmart.dao;

import com.ameerrasik.ameerrasikmart.exception.DatabaseException;
import com.ameerrasik.ameerrasikmart.model.Order;
import com.ameerrasik.ameerrasikmart.model.OrderItem;
import com.ameerrasik.ameerrasikmart.model.OrderStatus;
import com.ameerrasik.ameerrasikmart.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of OrderDAO.
 */
public class OrderDAOImpl implements OrderDAO {
    private static final Logger logger = LoggerFactory.getLogger(OrderDAOImpl.class);

    @Override
    public Order createOrder(Order order, List<OrderItem> items, Connection conn) {
        String insertOrderSql = "INSERT INTO orders (buyer_id, status, total_amount) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, order.getBuyerId());
            pstmt.setString(2, order.getStatus() != null ? order.getStatus().name() : OrderStatus.CONFIRMED.name());
            pstmt.setBigDecimal(3, order.getTotalAmount());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Creating order failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setId(generatedKeys.getLong(1));
                } else {
                    throw new DatabaseException("Creating order failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating order header for buyer " + order.getBuyerId(), e);
            throw new DatabaseException("Database error creating order header", e);
        }

        String insertItemSql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertItemSql, Statement.RETURN_GENERATED_KEYS)) {
            for (OrderItem item : items) {
                pstmt.setLong(1, order.getId());
                pstmt.setLong(2, item.getProductId());
                pstmt.setInt(3, item.getQuantity());
                pstmt.setBigDecimal(4, item.getUnitPrice());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            logger.error("Error creating order items for order " + order.getId(), e);
            throw new DatabaseException("Database error creating order items", e);
        }

        order.setItems(items);
        return order;
    }

    @Override
    public Optional<Order> findById(Long id) {
        String sql = "SELECT o.id, o.buyer_id, o.status, o.total_amount, o.created_at, "
                   + "u.name as buyer_name, u.email as buyer_email "
                   + "FROM orders o "
                   + "JOIN users u ON o.buyer_id = u.id "
                   + "WHERE o.id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(fetchOrderItems(order.getId(), conn));
                    return Optional.of(order);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding order by ID: " + id, e);
            throw new DatabaseException("Database error finding order", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Order> findOrdersByBuyerId(Long buyerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.id, o.buyer_id, o.status, o.total_amount, o.created_at, "
                   + "u.name as buyer_name, u.email as buyer_email "
                   + "FROM orders o "
                   + "JOIN users u ON o.buyer_id = u.id "
                   + "WHERE o.buyer_id = ? "
                   + "ORDER BY o.created_at DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, buyerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(fetchOrderItems(order.getId(), conn));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding orders for buyer ID: " + buyerId, e);
            throw new DatabaseException("Database error retrieving buyer orders", e);
        }
        return orders;
    }

    @Override
    public List<Order> findOrdersBySellerId(Long sellerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT DISTINCT o.id, o.buyer_id, o.status, o.total_amount, o.created_at, "
                   + "u.name as buyer_name, u.email as buyer_email "
                   + "FROM orders o "
                   + "JOIN users u ON o.buyer_id = u.id "
                   + "JOIN order_items oi ON o.id = oi.order_id "
                   + "JOIN products p ON oi.product_id = p.id "
                   + "WHERE p.seller_id = ? "
                   + "ORDER BY o.created_at DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, sellerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = mapResultSetToOrder(rs);
                    order.setItems(fetchOrderItemsForSeller(order.getId(), sellerId, conn));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding incoming orders for seller ID: " + sellerId, e);
            throw new DatabaseException("Database error retrieving seller orders", e);
        }
        return orders;
    }

    @Override
    public List<Order> findAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.id, o.buyer_id, o.status, o.total_amount, o.created_at, "
                   + "u.name as buyer_name, u.email as buyer_email "
                   + "FROM orders o "
                   + "JOIN users u ON o.buyer_id = u.id "
                   + "ORDER BY o.created_at DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                order.setItems(fetchOrderItems(order.getId(), conn));
                orders.add(order);
            }
        } catch (SQLException e) {
            logger.error("Error fetching all orders for admin", e);
            throw new DatabaseException("Database error fetching orders", e);
        }
        return orders;
    }

    @Override
    public boolean updateOrderStatus(Long orderId, OrderStatus status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status.name());
            pstmt.setLong(2, orderId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error updating status for order ID: " + orderId, e);
            throw new DatabaseException("Database error updating order status", e);
        }
    }

    private List<OrderItem> fetchOrderItems(Long orderId, Connection conn) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.id, oi.order_id, oi.product_id, oi.quantity, oi.unit_price, oi.created_at, "
                   + "p.name as product_name, p.category as product_category, p.image_url as product_image "
                   + "FROM order_items oi "
                   + "LEFT JOIN products p ON oi.product_id = p.id "
                   + "WHERE oi.order_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapResultSetToOrderItem(rs));
                }
            }
        }
        return items;
    }

    private List<OrderItem> fetchOrderItemsForSeller(Long orderId, Long sellerId, Connection conn) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.id, oi.order_id, oi.product_id, oi.quantity, oi.unit_price, oi.created_at, "
                   + "p.name as product_name, p.category as product_category, p.image_url as product_image "
                   + "FROM order_items oi "
                   + "JOIN products p ON oi.product_id = p.id "
                   + "WHERE oi.order_id = ? AND p.seller_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, orderId);
            pstmt.setLong(2, sellerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapResultSetToOrderItem(rs));
                }
            }
        }
        return items;
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getLong("id"));
        o.setBuyerId(rs.getLong("buyer_id"));
        o.setStatus(OrderStatus.fromString(rs.getString("status")));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        o.setBuyerName(rs.getString("buyer_name"));
        o.setBuyerEmail(rs.getString("buyer_email"));
        return o;
    }

    private OrderItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getLong("id"));
        item.setOrderId(rs.getLong("order_id"));
        item.setProductId(rs.getLong("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        item.setCreatedAt(rs.getTimestamp("created_at"));
        item.setProductName(rs.getString("product_name"));
        item.setProductCategory(rs.getString("product_category"));
        item.setProductImage(rs.getString("product_image"));
        return item;
    }
}
