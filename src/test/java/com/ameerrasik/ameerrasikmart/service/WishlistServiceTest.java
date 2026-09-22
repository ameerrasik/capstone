package com.ameerrasik.ameerrasikmart.service;

import com.ameerrasik.ameerrasikmart.dao.ProductDAO;
import com.ameerrasik.ameerrasikmart.dao.WishlistDAO;
import com.ameerrasik.ameerrasikmart.dto.WishlistItemDTO;
import com.ameerrasik.ameerrasikmart.exception.ResourceNotFoundException;
import com.ameerrasik.ameerrasikmart.exception.ValidationException;
import com.ameerrasik.ameerrasikmart.model.Product;
import com.ameerrasik.ameerrasikmart.model.WishlistItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class WishlistServiceTest {

    private WishlistDAO wishlistDAO;
    private ProductDAO productDAO;
    private CartService cartService;
    private WishlistService wishlistService;

    @BeforeEach
    void setUp() {
        wishlistDAO = Mockito.mock(WishlistDAO.class);
        productDAO = Mockito.mock(ProductDAO.class);
        cartService = Mockito.mock(CartService.class);
        wishlistService = new WishlistServiceImpl(wishlistDAO, productDAO, cartService);
    }

    @Test
    void testAddToWishlist_Success() {
        Long userId = 1L;
        Long productId = 10L;
        Product product = new Product(productId, 2L, "Keyboard", "Mechanical", new BigDecimal("1200.00"), 5, "Electronics", null, null);

        when(productDAO.findById(productId)).thenReturn(Optional.of(product));
        when(wishlistDAO.addToWishlist(userId, productId)).thenReturn(true);

        boolean result = wishlistService.addToWishlist(userId, productId);

        assertTrue(result);
        verify(productDAO).findById(productId);
        verify(wishlistDAO).addToWishlist(userId, productId);
    }

    @Test
    void testAddToWishlist_ProductNotFound() {
        Long userId = 1L;
        Long productId = 999L;

        when(productDAO.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            wishlistService.addToWishlist(userId, productId);
        });

        verify(wishlistDAO, never()).addToWishlist(anyLong(), anyLong());
    }

    @Test
    void testAddToWishlist_NullUserId() {
        assertThrows(ValidationException.class, () -> {
            wishlistService.addToWishlist(null, 10L);
        });
    }

    @Test
    void testAddToWishlist_NullProductId() {
        assertThrows(ValidationException.class, () -> {
            wishlistService.addToWishlist(1L, null);
        });
    }

    @Test
    void testRemoveFromWishlist_Success() {
        Long userId = 1L;
        Long productId = 10L;

        when(wishlistDAO.removeFromWishlist(userId, productId)).thenReturn(true);

        boolean result = wishlistService.removeFromWishlist(userId, productId);

        assertTrue(result);
        verify(wishlistDAO).removeFromWishlist(userId, productId);
    }

    @Test
    void testToggleWishlist_WhenAlreadyInWishlist_RemovesItem() {
        Long userId = 1L;
        Long productId = 10L;

        when(wishlistDAO.isInWishlist(userId, productId)).thenReturn(true);
        when(wishlistDAO.removeFromWishlist(userId, productId)).thenReturn(true);

        boolean result = wishlistService.toggleWishlist(userId, productId);

        assertFalse(result, "Should return false when toggling off / removing from wishlist");
        verify(wishlistDAO).removeFromWishlist(userId, productId);
        verify(wishlistDAO, never()).addToWishlist(anyLong(), anyLong());
    }

    @Test
    void testToggleWishlist_WhenNotInWishlist_AddsItem() {
        Long userId = 1L;
        Long productId = 10L;
        Product product = new Product(productId, 2L, "Keyboard", "Mechanical", new BigDecimal("1200.00"), 5, "Electronics", null, null);

        when(wishlistDAO.isInWishlist(userId, productId)).thenReturn(false);
        when(productDAO.findById(productId)).thenReturn(Optional.of(product));
        when(wishlistDAO.addToWishlist(userId, productId)).thenReturn(true);

        boolean result = wishlistService.toggleWishlist(userId, productId);

        assertTrue(result, "Should return true when toggling on / adding to wishlist");
        verify(wishlistDAO).addToWishlist(userId, productId);
    }

    @Test
    void testToggleWishlist_ProductNotFound() {
        Long userId = 1L;
        Long productId = 999L;

        when(wishlistDAO.isInWishlist(userId, productId)).thenReturn(false);
        when(productDAO.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            wishlistService.toggleWishlist(userId, productId);
        });

        verify(wishlistDAO, never()).addToWishlist(anyLong(), anyLong());
    }

    @Test
    void testIsInWishlist() {
        when(wishlistDAO.isInWishlist(1L, 10L)).thenReturn(true);

        assertTrue(wishlistService.isInWishlist(1L, 10L));
        assertFalse(wishlistService.isInWishlist(null, 10L));
        assertFalse(wishlistService.isInWishlist(1L, null));
    }

    @Test
    void testGetWishlistItems() {
        WishlistItem item = new WishlistItem(1L, 1L, 10L, new Timestamp(System.currentTimeMillis()));
        Product product = new Product(10L, 2L, "Keyboard", "Mechanical", new BigDecimal("1200.00"), 5, "Electronics", null, null);
        WishlistItemDTO dto = new WishlistItemDTO(item, product);
        when(wishlistDAO.getWishlistByUserId(1L)).thenReturn(List.of(dto));

        List<WishlistItemDTO> items = wishlistService.getWishlistItems(1L);
        assertEquals(1, items.size());
        assertEquals("Keyboard", items.get(0).getProductName());

        assertTrue(wishlistService.getWishlistItems(null).isEmpty());
    }

    @Test
    void testGetWishlistProductIds() {
        when(wishlistDAO.getWishlistProductIds(1L)).thenReturn(Set.of(10L, 20L));

        Set<Long> ids = wishlistService.getWishlistProductIds(1L);
        assertEquals(2, ids.size());
        assertTrue(ids.contains(10L));

        assertTrue(wishlistService.getWishlistProductIds(null).isEmpty());
    }

    @Test
    void testGetWishlistCount() {
        when(wishlistDAO.getWishlistCount(1L)).thenReturn(3);

        assertEquals(3, wishlistService.getWishlistCount(1L));
        assertEquals(0, wishlistService.getWishlistCount(null));
    }

    @Test
    void testMoveToCart_Success() {
        Long userId = 1L;
        Long productId = 10L;
        int qty = 1;

        when(cartService.addToCart(userId, productId, qty)).thenReturn(true);
        when(wishlistDAO.removeFromWishlist(userId, productId)).thenReturn(true);

        boolean result = wishlistService.moveToCart(userId, productId, qty);

        assertTrue(result);
        verify(cartService).addToCart(userId, productId, qty);
        verify(wishlistDAO).removeFromWishlist(userId, productId);
    }

    @Test
    void testMoveToCart_CartAdditionFails() {
        Long userId = 1L;
        Long productId = 10L;
        int qty = 1;

        when(cartService.addToCart(userId, productId, qty)).thenReturn(false);

        boolean result = wishlistService.moveToCart(userId, productId, qty);

        assertFalse(result);
        verify(cartService).addToCart(userId, productId, qty);
        verify(wishlistDAO, never()).removeFromWishlist(anyLong(), anyLong());
    }

    @Test
    void testClearWishlist() {
        when(wishlistDAO.clearWishlist(1L)).thenReturn(true);

        assertTrue(wishlistService.clearWishlist(1L));
        assertFalse(wishlistService.clearWishlist(null));
    }
}
