package com.ameerrasik.ameerrasikmart.service;

import com.ameerrasik.ameerrasikmart.dao.ProductDAO;
import com.ameerrasik.ameerrasikmart.dao.ProductDAOImpl;
import com.ameerrasik.ameerrasikmart.dao.WishlistDAO;
import com.ameerrasik.ameerrasikmart.dao.WishlistDAOImpl;
import com.ameerrasik.ameerrasikmart.dto.WishlistItemDTO;
import com.ameerrasik.ameerrasikmart.exception.ResourceNotFoundException;
import com.ameerrasik.ameerrasikmart.exception.ValidationException;
import com.ameerrasik.ameerrasikmart.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Implementation of WishlistService logic.
 */
public class WishlistServiceImpl implements WishlistService {
    private static final Logger logger = LoggerFactory.getLogger(WishlistServiceImpl.class);

    private final WishlistDAO wishlistDAO;
    private final ProductDAO productDAO;
    private final CartService cartService;

    public WishlistServiceImpl() {
        this.wishlistDAO = new WishlistDAOImpl();
        this.productDAO = new ProductDAOImpl();
        this.cartService = new CartServiceImpl();
    }

    public WishlistServiceImpl(WishlistDAO wishlistDAO, ProductDAO productDAO, CartService cartService) {
        this.wishlistDAO = wishlistDAO;
        this.productDAO = productDAO;
        this.cartService = cartService;
    }

    @Override
    public boolean addToWishlist(Long userId, Long productId) {
        if (userId == null) {
            throw new ValidationException("User authentication required to manage wishlist.");
        }
        if (productId == null) {
            throw new ValidationException("Product ID is required.");
        }

        Optional<Product> productOpt = productDAO.findById(productId);
        if (productOpt.isEmpty()) {
            throw new ResourceNotFoundException("Product not found.");
        }

        logger.info("Adding product ID: {} to wishlist for User ID: {}", productId, userId);
        return wishlistDAO.addToWishlist(userId, productId);
    }

    @Override
    public boolean removeFromWishlist(Long userId, Long productId) {
        if (userId == null) {
            throw new ValidationException("User authentication required.");
        }
        if (productId == null) {
            throw new ValidationException("Product ID is required.");
        }

        logger.info("Removing product ID: {} from wishlist for User ID: {}", productId, userId);
        return wishlistDAO.removeFromWishlist(userId, productId);
    }

    @Override
    public boolean toggleWishlist(Long userId, Long productId) {
        if (userId == null) {
            throw new ValidationException("User authentication required to manage wishlist.");
        }
        if (productId == null) {
            throw new ValidationException("Product ID is required.");
        }

        if (wishlistDAO.isInWishlist(userId, productId)) {
            wishlistDAO.removeFromWishlist(userId, productId);
            logger.info("Toggled OFF wishlist for product ID: {}, User ID: {}", productId, userId);
            return false;
        } else {
            Optional<Product> productOpt = productDAO.findById(productId);
            if (productOpt.isEmpty()) {
                throw new ResourceNotFoundException("Product not found.");
            }
            wishlistDAO.addToWishlist(userId, productId);
            logger.info("Toggled ON wishlist for product ID: {}, User ID: {}", productId, userId);
            return true;
        }
    }

    @Override
    public boolean isInWishlist(Long userId, Long productId) {
        if (userId == null || productId == null) {
            return false;
        }
        return wishlistDAO.isInWishlist(userId, productId);
    }

    @Override
    public List<WishlistItemDTO> getWishlistItems(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return wishlistDAO.getWishlistByUserId(userId);
    }

    @Override
    public Set<Long> getWishlistProductIds(Long userId) {
        if (userId == null) {
            return Collections.emptySet();
        }
        return wishlistDAO.getWishlistProductIds(userId);
    }

    @Override
    public int getWishlistCount(Long userId) {
        if (userId == null) {
            return 0;
        }
        return wishlistDAO.getWishlistCount(userId);
    }

    @Override
    public boolean moveToCart(Long userId, Long productId, int quantity) {
        if (userId == null) {
            throw new ValidationException("User authentication required.");
        }
        if (productId == null) {
            throw new ValidationException("Product ID is required.");
        }

        // Add to cart with full stock and quantity validation in CartService
        boolean added = cartService.addToCart(userId, productId, quantity);
        if (added) {
            wishlistDAO.removeFromWishlist(userId, productId);
            logger.info("Moved product ID: {} from wishlist to cart for User ID: {}", productId, userId);
            return true;
        }
        return false;
    }

    @Override
    public boolean clearWishlist(Long userId) {
        if (userId == null) {
            return false;
        }
        return wishlistDAO.clearWishlist(userId);
    }
}
