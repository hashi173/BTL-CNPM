package com.coffeeshop.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Thực thể OrderItems - Chi tiết từng mặt hàng trong đơn hàng.
 * Lớp này là thành phần của thực thể Orders.
 */
public class OrderItems {
    private Integer id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int quantity;
    private String snapshotOptions;
    private String snapshotProductName;
    private BigDecimal snapshotUnitPrice;
    private BigDecimal subTotal;
    private UUID orderId;
    private UUID productId;

    public OrderItems() {}

    public OrderItems(Integer id, Timestamp createdAt, Timestamp updatedAt, int quantity,
                      String snapshotOptions, String snapshotProductName,
                      BigDecimal snapshotUnitPrice, BigDecimal subTotal,
                      UUID orderId, UUID productId) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.quantity = quantity;
        this.snapshotOptions = snapshotOptions;
        this.snapshotProductName = snapshotProductName;
        this.snapshotUnitPrice = snapshotUnitPrice;
        this.subTotal = subTotal;
        this.orderId = orderId;
        this.productId = productId;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getSnapshotOptions() { return snapshotOptions; }
    public void setSnapshotOptions(String snapshotOptions) { this.snapshotOptions = snapshotOptions; }

    public String getSnapshotProductName() { return snapshotProductName; }
    public void setSnapshotProductName(String snapshotProductName) { this.snapshotProductName = snapshotProductName; }

    public BigDecimal getSnapshotUnitPrice() { return snapshotUnitPrice; }
    public void setSnapshotUnitPrice(BigDecimal snapshotUnitPrice) { this.snapshotUnitPrice = snapshotUnitPrice; }

    public BigDecimal getSubTotal() { return subTotal; }
    public void setSubTotal(BigDecimal subTotal) { this.subTotal = subTotal; }

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }
}
