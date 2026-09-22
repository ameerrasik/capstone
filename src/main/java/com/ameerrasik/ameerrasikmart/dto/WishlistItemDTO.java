package com.ameerrasik.ameerrasikmart.dto;

import com.ameerrasik.ameerrasikmart.model.Product;
import com.ameerrasik.ameerrasikmart.model.WishlistItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Data Transfer Object combining WishlistItem and Product details for UI rendering.
 */
public class WishlistItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private Integer availableStock;
    private String category;
    private String imageUrl;
    private Timestamp createdAt;

    public WishlistItemDTO() {
    }

    public WishlistItemDTO(WishlistItem item, Product product) {
        if (item != null) {
            this.id = item.getId();
            this.userId = item.getUserId();
            this.productId = item.getProductId();
            this.createdAt = item.getCreatedAt();
        }
        if (product != null) {
            this.productName = product.getName();
            this.unitPrice = product.getPrice();
            this.availableStock = product.getStockQty();
            this.category = product.getCategory();
            this.imageUrl = product.getImageUrl();
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isInStock() {
        return availableStock != null && availableStock > 0;
    }
}
