package com.coffeeshop.model;

import java.sql.Timestamp;
import java.util.UUID;

public class Orders {
    private UUID id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String addressText;
    private String customerName;
    private String note;
    private String orderType;
    private String phone;
    private String status;
    private Double subTotal;
    private Double totalAmount;
    private String trackingCode;
    private UUID userId;

    public Orders() {}

    public Orders(UUID id, Timestamp createdAt, Timestamp updatedAt, String addressText,
                  String customerName, String note,
                  String orderType, String phone, String status, Double subTotal,
                  Double totalAmount, String trackingCode, UUID userId) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.addressText = addressText;
        this.customerName = customerName;
        this.note = note;
        this.orderType = orderType;
        this.phone = phone;
        this.status = status;
        this.subTotal = subTotal;
        this.totalAmount = totalAmount;
        this.trackingCode = trackingCode;
        this.userId = userId;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public String getAddressText() { return addressText; }
    public void setAddressText(String addressText) { this.addressText = addressText; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getSubTotal() { return subTotal; }
    public void setSubTotal(Double subTotal) { this.subTotal = subTotal; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public String getTrackingCode() { return trackingCode; }
    public void setTrackingCode(String trackingCode) { this.trackingCode = trackingCode; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    // Helper cho CreateOrder
    public static Orders createOrder(UUID userId, String customerName, String phone,
                                     String addressText, String note, Double totalAmount) {
        Orders order = new Orders();
        order.setId(UUID.randomUUID());
        order.setUserId(userId);
        order.setCustomerName(customerName);
        order.setPhone(phone);
        order.setAddressText(addressText);
        order.setNote(note);
        order.setSubTotal(totalAmount);
        order.setTotalAmount(totalAmount);
        order.setStatus("PENDING");
        order.setOrderType("ONLINE");
        order.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        order.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return order;
    }
}
