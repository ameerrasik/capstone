package com.ameerrasik.ameerrasikmart.service;

import com.ameerrasik.ameerrasikmart.dao.ProductDAO;
import com.ameerrasik.ameerrasikmart.dao.ProductDAOImpl;
import com.ameerrasik.ameerrasikmart.exception.AuthorizationException;
import com.ameerrasik.ameerrasikmart.exception.ResourceNotFoundException;
import com.ameerrasik.ameerrasikmart.exception.ValidationException;
import com.ameerrasik.ameerrasikmart.model.Product;
import com.ameerrasik.ameerrasikmart.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of ProductService logic.
 */
public class ProductServiceImpl implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductDAO productDAO;

    public ProductServiceImpl() {
        this.productDAO = new ProductDAOImpl();
    }

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productDAO.findById(id);
    }

    @Override
    public List<Product> getAllProducts(String category, String search, String sortBy) {
        return productDAO.findAll(category, search, sortBy);
    }

    @Override
    public List<Product> getProductsBySeller(Long sellerId) {
        return productDAO.findBySellerId(sellerId);
    }

    @Override
    public Product createProduct(Product product, User seller) {
        if (seller == null || (!seller.isSeller() && !seller.isAdmin())) {
            throw new AuthorizationException("Only sellers or admins can create products.");
        }
        validateProduct(product);

        product.setSellerId(seller.getId());
        if (product.getImageUrl() == null || product.getImageUrl().trim().isEmpty()) {
            product.setImageUrl("https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=600&auto=format&fit=crop&q=80");
        }

        Product created = productDAO.createProduct(product);
        logger.info("Product created with ID: {} by seller ID: {}", created.getId(), seller.getId());
        return created;
    }

    @Override
    public boolean updateProduct(Product product, User seller) {
        if (seller == null) {
            throw new AuthorizationException("User authentication required.");
        }
        Optional<Product> existing = productDAO.findById(product.getId());
        if (existing.isEmpty()) {
            throw new ResourceNotFoundException("Product with ID " + product.getId() + " not found.");
        }

        Product existingProduct = existing.get();
        if (!seller.isAdmin() && !existingProduct.getSellerId().equals(seller.getId())) {
            throw new AuthorizationException("You are not authorized to update another seller's product.");
        }

        validateProduct(product);
        product.setSellerId(existingProduct.getSellerId());
        return productDAO.updateProduct(product);
    }

    @Override
    public boolean deleteProduct(Long productId, User user) {
        if (user == null) {
            throw new AuthorizationException("User authentication required.");
        }
        Optional<Product> existing = productDAO.findById(productId);
        if (existing.isEmpty()) {
            throw new ResourceNotFoundException("Product with ID " + productId + " not found.");
        }

        Product existingProduct = existing.get();
        if (!user.isAdmin() && !existingProduct.getSellerId().equals(user.getId())) {
            throw new AuthorizationException("You are not authorized to delete another seller's product.");
        }

        logger.info("Deleting product ID: {} requested by User ID: {}", productId, user.getId());
        return productDAO.deleteProduct(productId);
    }

    @Override
    public List<String> getCategories() {
        return productDAO.getCategories();
    }

    private void validateProduct(Product product) {
        if (product == null) {
            throw new ValidationException("Product data is required.");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new ValidationException("Product name is required.");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Product price must be greater than zero.");
        }
        if (product.getStockQty() == null || product.getStockQty() < 0) {
            throw new ValidationException("Product stock quantity cannot be negative.");
        }
        if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
            throw new ValidationException("Product category is required.");
        }
    }
}
