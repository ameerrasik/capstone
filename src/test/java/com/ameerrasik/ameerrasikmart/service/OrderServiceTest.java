package com.ameerrasik.ameerrasikmart.service;

import com.ameerrasik.ameerrasikmart.dao.CartDAO;
import com.ameerrasik.ameerrasikmart.dao.OrderDAO;
import com.ameerrasik.ameerrasikmart.dao.ProductDAO;
import com.ameerrasik.ameerrasikmart.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class OrderServiceTest {

    private CartDAO cartDAO;
    private ProductDAO productDAO;
    private OrderDAO orderDAO;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        cartDAO = Mockito.mock(CartDAO.class);
        productDAO = Mockito.mock(ProductDAO.class);
        orderDAO = Mockito.mock(OrderDAO.class);
        orderService = new OrderServiceImpl(cartDAO, productDAO, orderDAO);
    }

    @Test
    void testCheckoutWithEmptyCartThrowsValidationException() {
        when(cartDAO.getCartItemsByUserId(1L)).thenReturn(Collections.emptyList());

        assertThrows(ValidationException.class, () -> {
            orderService.processCheckout(1L, "Mock Card");
        });
    }
}
