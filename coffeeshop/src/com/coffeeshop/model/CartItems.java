package com.coffeeshop.model;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Thực thể CartItems - Mặt hàng trong giỏ hàng.
 * Lưu trữ trong CSDL qua CartDAO.
 */
public class CartItems {
    private UUID id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int quantity;
    private String options;
    private UUID productId;
    private UUID userId;

    // Virtual fields
    private String productName;
    private double unitPrice;

    public CartItems() {}

    public CartItems(UUID id, Timestamp createdAt, Timestamp updatedAt, int quantity,
                     String options, UUID productId, UUID userId) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.quantity = quantity;
        this.options = options;
        this.productId = productId;
        this.userId = userId;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getOptions() { return options; }
    public void setOptions(String options) { this.options = options; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    /**
     * Tính tổng tiền của mặt hàng này.
     */
    public double getTotal() {
        return unitPrice * quantity;
    }

    /**
     * Kiểm tra hai CartItems có cùng sản phẩm và tùy chọn không.
     */
    public boolean isSameItem(CartItems other) {
        if (other == null) return false;
        if (!this.productId.equals(other.productId)) return false;
        if (this.options == null) return other.options == null;
        return this.options.equals(other.options);
    }
}
