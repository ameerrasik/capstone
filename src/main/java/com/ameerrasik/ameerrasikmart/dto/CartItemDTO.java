package com.ameerrasik.ameerrasikmart.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import com.ameerrasik.ameerrasikmart.model.CartItem;
import com.ameerrasik.ameerrasikmart.model.Product;

/**
 * Data Transfer Object combining CartItem and Product details for UI rendering.
 */
public class CartItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private String productName;
    private BigDecimal unitPrice;
    private Integer availableStock;
    private String category;
    private String imageUrl;
    private BigDecimal subtotal;

    public CartItemDTO() {
    }

    public CartItemDTO(CartItem item, Product product) {
        if (item != null) {
            this.id = item.getId();
            this.userId = item.getUserId();
            this.productId = item.getProductId();
            this.quantity = item.getQuantity();
        }
        if (product != null) {
            this.productName = product.getName();
            this.unitPrice = product.getPrice();
            this.availableStock = product.getStockQty();
            this.category = product.getCategory();
            this.imageUrl = product.getImageUrl();
        }
        calculateSubtotal();
    }

    public void calculateSubtotal() {
        if (unitPrice != null && quantity != null) {
            this.subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        } else {
            this.subtotal = BigDecimal.ZERO;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        calculateSubtotal();
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
