package com.coffeeshop.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Thực thể Products - Thông tin sản phẩm cà phê.
 */
public class Products {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private UUID categoryId;
    private boolean isAvailable;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String imagePath;

    // Transient field - tên danh mục (JOIN)
    private String categoryName;

    public Products() {}

    public Products(UUID id, String name, String description, BigDecimal basePrice,
                    UUID categoryId, boolean isAvailable, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.categoryId = categoryId;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }

    public UUID getCategoryId() { return categoryId; }
    public void setCategoryId(UUID categoryId) { this.categoryId = categoryId; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public void updatePrice(BigDecimal newPrice) {
        this.basePrice = newPrice;
    }

    @Override
    public String toString() {
        return name + " - " + basePrice + " VND";
    }
}
