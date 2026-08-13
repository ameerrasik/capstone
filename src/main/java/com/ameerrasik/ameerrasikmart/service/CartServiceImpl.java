package com.ameerrasik.ameerrasikmart.service;

import com.ameerrasik.ameerrasikmart.dao.CartDAO;
import com.ameerrasik.ameerrasikmart.dao.CartDAOImpl;
import com.ameerrasik.ameerrasikmart.dao.ProductDAO;
import com.ameerrasik.ameerrasikmart.dao.ProductDAOImpl;
import com.ameerrasik.ameerrasikmart.dto.CartItemDTO;
import com.ameerrasik.ameerrasikmart.exception.ResourceNotFoundException;
import com.ameerrasik.ameerrasikmart.exception.ValidationException;
import com.ameerrasik.ameerrasikmart.model.CartItem;
import com.ameerrasik.ameerrasikmart.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of CartService logic.
 */
public class CartServiceImpl implements CartService {
    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public CartServiceImpl() {
        this.cartDAO = new CartDAOImpl();
        this.productDAO = new ProductDAOImpl();
    }

    public CartServiceImpl(CartDAO cartDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    @Override
    public boolean addToCart(Long userId, Long productId, int quantity) {
        if (userId == null) {
            throw new ValidationException("User authentication required to manage cart.");
        }
        if (quantity <= 0) {
            throw new ValidationException("Cart quantity must be greater than zero.");
        }

        Optional<Product> productOpt = productDAO.findById(productId);
        if (productOpt.isEmpty()) {
            throw new ResourceNotFoundException("Product not found.");
        }

        Product product = productOpt.get();
        if (product.getStockQty() < quantity) {
            throw new ValidationException("Requested quantity (" + quantity + ") exceeds available stock (" + product.getStockQty() + ").");
        }

        Optional<CartItem> existingItem = cartDAO.findCartItem(userId, productId);
        int totalQty = quantity + existingItem.map(CartItem::getQuantity).orElse(0);

        if (totalQty > product.getStockQty()) {
            throw new ValidationException("Total cart quantity (" + totalQty + ") exceeds available stock (" + product.getStockQty() + ").");
        }

        logger.info("Adding product ID: {} (qty: {}) to cart for User ID: {}", productId, quantity, userId);
        return cartDAO.addToCart(userId, productId, quantity);
    }

    @Override
    public boolean updateQuantity(Long userId, Long productId, int quantity) {
        if (userId == null) {
            throw new ValidationException("User authentication required.");
        }
        if (quantity <= 0) {
            return cartDAO.removeFromCart(userId, productId);
        }

        Optional<Product> productOpt = productDAO.findById(productId);
        if (productOpt.isEmpty()) {
            throw new ResourceNotFoundException("Product not found.");
        }

        Product product = productOpt.get();
        if (quantity > product.getStockQty()) {
            throw new ValidationException("Quantity (" + quantity + ") exceeds available stock (" + product.getStockQty() + ").");
        }

        return cartDAO.updateQuantity(userId, productId, quantity);
    }

    @Override
    public boolean removeFromCart(Long userId, Long productId) {
        if (userId == null) {
            throw new ValidationException("User authentication required.");
        }
        return cartDAO.removeFromCart(userId, productId);
    }

    @Override
    public List<CartItemDTO> getCartItems(Long userId) {
        if (userId == null) {
            return List.of();
        }
        return cartDAO.getCartItemsByUserId(userId);
    }

    @Override
    public BigDecimal getCartTotal(Long userId) {
        List<CartItemDTO> items = getCartItems(userId);
        BigDecimal total = BigDecimal.ZERO;
        for (CartItemDTO item : items) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    @Override
    public boolean clearCart(Long userId) {
        if (userId == null) {
            return false;
        }
        return cartDAO.clearCart(userId);
    }
}
