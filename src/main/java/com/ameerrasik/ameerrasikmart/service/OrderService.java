package com.ameerrasik.ameerrasikmart.service;

import com.ameerrasik.ameerrasikmart.model.Order;
import com.ameerrasik.ameerrasikmart.model.OrderStatus;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Order processing and checkout transactions.
 */
public interface OrderService {
    /**
     * Executes the complete transactional checkout flow:
     * Validates cart & stock -> Computes server-side total -> Creates order header & items -> Reduces stock -> Clears cart.
     */
    Order processCheckout(Long buyerId, String paymentMethod);

    /**
     * Finds order by ID.
     */
    Optional<Order> getOrderById(Long id);

    /**
     * Lists orders placed by a buyer.
     */
    List<Order> getBuyerOrders(Long buyerId);

    /**
     * Lists orders containing products for a seller.
     */
    List<Order> getSellerOrders(Long sellerId);

    /**
     * Lists all marketplace orders for admin overview.
     */
    List<Order> getAllOrders();

    /**
     * Updates status of an order.
     */
    boolean updateOrderStatus(Long orderId, OrderStatus status);
}
