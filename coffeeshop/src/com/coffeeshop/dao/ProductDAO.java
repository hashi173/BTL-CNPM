package com.coffeeshop.dao;

import com.coffeeshop.model.Products;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * product_DAO - Truy xuất dữ liệu bảng products.
 * 
 * [CNPM] Use Case: Quản lý sản phẩm
 * Phụ trách: Quỳnh
 * Mô tả: DAO xử lý các thao tác lấy danh sách, tìm kiếm, thêm, sửa, xóa sản phẩm.
 */
public class ProductDAO extends DAO {

    public List<Products> getAllProducts() {
        List<Products> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "WHERE p.is_available = true ORDER BY p.created_at DESC";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Products> getAllProductsAdmin() {
        List<Products> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id ORDER BY p.created_at DESC";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Products getProductDetail(UUID id) {
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id WHERE p.id = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Products> searchProduct(String keyword) {
        List<Products> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "WHERE LOWER(p.name) LIKE LOWER(?) OR CAST(p.id AS VARCHAR) LIKE ? " +
                     "ORDER BY p.name";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updatePrice(UUID id, BigDecimal newPrice) {
        String sql = "UPDATE products SET base_price = ? WHERE id = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setBigDecimal(1, newPrice);
            ps.setObject(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateProduct(Products product) {
        String sql = "UPDATE products SET name = ?, description = ?, base_price = ?, " +
                     "category_id = ?, is_available = ?, image_path = ? WHERE id = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setBigDecimal(3, product.getBasePrice());
            ps.setObject(4, product.getCategoryId());
            ps.setBoolean(5, product.isAvailable());
            ps.setString(6, product.getImagePath());
            ps.setObject(7, product.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addProduct(Products product) {
        String sql = "INSERT INTO products (id, name, description, base_price, category_id, is_available, image_path, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            UUID id = product.getId() != null ? product.getId() : UUID.randomUUID();
            ps.setObject(1, id);
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setBigDecimal(4, product.getBasePrice());
            ps.setObject(5, product.getCategoryId());
            ps.setBoolean(6, product.isAvailable());
            ps.setString(7, product.getImagePath());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteProduct(UUID id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return updateStatus(id, false);
        }
    }

    public boolean updateStatus(UUID id, boolean available) {
        String sql = "UPDATE products SET is_available = ? WHERE id = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setBoolean(1, available);
            ps.setObject(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Products mapResultSet(ResultSet rs) throws SQLException {
        Products p = new Products();
        p.setId(UUID.fromString(rs.getString("id")));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setBasePrice(rs.getBigDecimal("base_price"));
        String catId = rs.getString("category_id");
        if (catId != null) p.setCategoryId(UUID.fromString(catId));
        p.setAvailable(rs.getBoolean("is_available"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        try {
            p.setImagePath(rs.getString("image_path"));
        } catch (SQLException ignored) {}
        try {
            p.setCategoryName(rs.getString("category_name"));
        } catch (SQLException ignored) {}
        return p;
    }
}
