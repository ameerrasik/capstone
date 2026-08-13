package com.ameerrasik.ameerrasikmart.service;

import com.ameerrasik.ameerrasikmart.model.Product;
import com.ameerrasik.ameerrasikmart.model.User;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Product catalogue and seller product management.
 */
public interface ProductService {
    /**
     * Gets product details by ID.
     */
    Optional<Product> getProductById(Long id);

    /**
     * Searches, filters, and sorts product catalogue.
     */
    List<Product> getAllProducts(String category, String search, String sortBy);

    /**
     * Lists products for a given seller.
     */
    List<Product> getProductsBySeller(Long sellerId);

    /**
     * Creates a new product listing by a seller.
     */
    Product createProduct(Product product, User seller);

    /**
     * Updates an existing product listing.
     */
    boolean updateProduct(Product product, User seller);

    /**
     * Deletes a product listing (by seller owner or admin).
     */
    boolean deleteProduct(Long productId, User user);

    /**
     * Gets all distinct categories available.
     */
    List<String> getCategories();
}
