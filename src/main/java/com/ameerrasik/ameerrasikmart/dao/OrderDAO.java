package com.ameerrasik.ameerrasikmart.dao;

import com.ameerrasik.ameerrasikmart.model.Order;
import com.ameerrasik.ameerrasikmart.model.OrderItem;
import com.ameerrasik.ameerrasikmart.model.OrderStatus;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Order management.
 */
public interface OrderDAO {
    /**
     * Creates an order and its associated order items within an active JDBC transaction.
     */
    Order createOrder(Order order, List<OrderItem> items, Connection conn);

    /**
     * Finds an order by ID including all items and buyer details.
     */
    Optional<Order> findById(Long id);

    /**
     * Lists orders placed by a specific buyer.
     */
    List<Order> findOrdersByBuyerId(Long buyerId);

    /**
     * Lists orders containing products listed by a specific seller.
     */
    List<Order> findOrdersBySellerId(Long sellerId);

    /**
     * Lists all marketplace orders for admin overview.
     */
    List<Order> findAllOrders();

    /**
     * Updates status of an order.
     */
    boolean updateOrderStatus(Long orderId, OrderStatus status);
}
