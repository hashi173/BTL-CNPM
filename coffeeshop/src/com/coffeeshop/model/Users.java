package com.coffeeshop.model;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Thực thể Users - Thông tin tài khoản người dùng.
 */
public class Users {
    private UUID id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean active;
    private String email;
    private String fullName;
    private Double hourlyRate;
    private String password;
    private String phone;
    private String role; // ADMIN, CLIENT
    private String userCode;
    private String username;

    public Users() {
    }

    public Users(UUID id, Timestamp createdAt, Timestamp updatedAt, boolean active, String email,
            String fullName, String password, String phone,
            String role, String username) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.active = active;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.username = username;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
