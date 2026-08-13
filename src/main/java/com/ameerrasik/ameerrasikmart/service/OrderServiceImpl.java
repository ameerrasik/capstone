package com.ameerrasik.ameerrasikmart.service;

import com.ameerrasik.ameerrasikmart.dao.*;
import com.ameerrasik.ameerrasikmart.dto.CartItemDTO;
import com.ameerrasik.ameerrasikmart.exception.DatabaseException;
import com.ameerrasik.ameerrasikmart.exception.ValidationException;
import com.ameerrasik.ameerrasikmart.model.Order;
import com.ameerrasik.ameerrasikmart.model.OrderItem;
import com.ameerrasik.ameerrasikmart.model.OrderStatus;
import com.ameerrasik.ameerrasikmart.model.Product;
import com.ameerrasik.ameerrasikmart.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of OrderService with transactional checkout management.
 */
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final CartDAO cartDAO;
    private final ProductDAO productDAO;
    private final OrderDAO orderDAO;

    public OrderServiceImpl() {
        this.cartDAO = new CartDAOImpl();
        this.productDAO = new ProductDAOImpl();
        this.orderDAO = new OrderDAOImpl();
    }

    public OrderServiceImpl(CartDAO cartDAO, ProductDAO productDAO, OrderDAO orderDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
        this.orderDAO = orderDAO;
    }

    @Override
    public Order processCheckout(Long buyerId, String paymentMethod) {
        if (buyerId == null) {
            throw new ValidationException("User authentication required for checkout.");
        }

        List<CartItemDTO> cartItems = cartDAO.getCartItemsByUserId(buyerId);
        if (cartItems.isEmpty()) {
            throw new ValidationException("Your shopping cart is empty.");
        }

        // 1 & 2. Validate stock and calculate total server-side
        BigDecimal calculatedTotal = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItemDTO cartItem : cartItems) {
            Optional<Product> productOpt = productDAO.findById(cartItem.getProductId());
            if (productOpt.isEmpty()) {
                throw new ValidationException("Product '" + cartItem.getProductName() + "' is no longer available.");
            }
            Product product = productOpt.get();
            if (product.getStockQty() < cartItem.getQuantity()) {
                throw new ValidationException("Insufficient stock for product '" + product.getName() + 
                    "'. Available: " + product.getStockQty() + ", Requested: " + cartItem.getQuantity());
            }

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            calculatedTotal = calculatedTotal.add(itemTotal);

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(product.getPrice());
            orderItems.add(orderItem);
        }

        // Execute transactional checkout
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            // 4. Create Order Header
            Order order = new Order();
            order.setBuyerId(buyerId);
            order.setStatus(OrderStatus.CONFIRMED);
            order.setTotalAmount(calculatedTotal);

            Order createdOrder = orderDAO.createOrder(order, orderItems, conn);

            // 6. Reduce Product Stock
            for (OrderItem item : orderItems) {
                boolean stockReduced = productDAO.reduceStock(item.getProductId(), item.getQuantity(), conn);
                if (!stockReduced) {
                    throw new ValidationException("Failed to reduce stock for product ID: " + item.getProductId());
                }
            }

            // 7. Clear Buyer Cart
            cartDAO.clearCart(buyerId, conn);

            // Commit Transaction
            conn.commit();
            logger.info("Successfully placed Order ID: {} for Buyer ID: {}, Total: {}", createdOrder.getId(), buyerId, calculatedTotal);
            return createdOrder;
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    logger.warn("Rolled back checkout transaction for Buyer ID: {}", buyerId);
                } catch (SQLException rollbackEx) {
                    logger.error("Error during transaction rollback", rollbackEx);
                }
            }
            if (e instanceof ValidationException) {
                throw (ValidationException) e;
            }
            throw new DatabaseException("Failed to complete checkout transaction", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Error resetting connection autoCommit", e);
                }
            }
        }
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderDAO.findById(id);
    }

    @Override
    public List<Order> getBuyerOrders(Long buyerId) {
        return orderDAO.findOrdersByBuyerId(buyerId);
    }

    @Override
    public List<Order> getSellerOrders(Long sellerId) {
        return orderDAO.findOrdersBySellerId(sellerId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderDAO.findAllOrders();
    }

    @Override
    public boolean updateOrderStatus(Long orderId, OrderStatus status) {
        return orderDAO.updateOrderStatus(orderId, status);
    }
}
