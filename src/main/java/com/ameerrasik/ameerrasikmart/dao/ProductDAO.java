package com.ameerrasik.ameerrasikmart.dao;

import com.ameerrasik.ameerrasikmart.model.Product;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Product management.
 */
public interface ProductDAO {
    /**
     * Finds a product by ID.
     */
    Optional<Product> findById(Long id);

    /**
     * Lists products with optional search keyword, category filter, and sorting order.
     */
    List<Product> findAll(String category, String search, String sortBy);

    /**
     * Lists products belonging to a specific seller.
     */
    List<Product> findBySellerId(Long sellerId);

    /**
     * Creates a new product listing.
     */
    Product createProduct(Product product);

    /**
     * Updates an existing product details.
     */
    boolean updateProduct(Product product);

    /**
     * Deletes a product by ID.
     */
    boolean deleteProduct(Long id);

    /**
     * Updates product stock quantity using an active Connection context (for transactions).
     */
    boolean reduceStock(Long productId, int quantity, Connection conn);

    /**
     * Updates product stock quantity directly.
     */
    boolean updateStock(Long productId, int newStock);

    /**
     * Gets list of unique categories.
     */
    List<String> getCategories();
}
