package com.ameerrasik.ameerrasikmart.service;

import com.ameerrasik.ameerrasikmart.dao.ProductDAO;
import com.ameerrasik.ameerrasikmart.exception.ValidationException;
import com.ameerrasik.ameerrasikmart.model.Product;
import com.ameerrasik.ameerrasikmart.model.Role;
import com.ameerrasik.ameerrasikmart.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    private ProductDAO productDAO;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productDAO = Mockito.mock(ProductDAO.class);
        productService = new ProductServiceImpl(productDAO);
    }

    @Test
    void testCreateProductSuccess() {
        User seller = new User(2L, "Seller", "seller@test.com", "hash", Role.SELLER, null);
        Product product = new Product(null, 2L, "Wireless Mouse", "Good mouse", new BigDecimal("999.00"), 10, "Electronics", null, null);

        when(productDAO.createProduct(any(Product.class))).thenAnswer(i -> {
            Product p = i.getArgument(0);
            p.setId(100L);
            return p;
        });

        Product created = productService.createProduct(product, seller);
        assertNotNull(created);
        assertEquals(100L, created.getId());
    }

    @Test
    void testCreateProductZeroPrice() {
        User seller = new User(2L, "Seller", "seller@test.com", "hash", Role.SELLER, null);
        Product product = new Product(null, 2L, "Free Mouse", "Free item", BigDecimal.ZERO, 10, "Electronics", null, null);

        assertThrows(ValidationException.class, () -> {
            productService.createProduct(product, seller);
        });
    }
}
