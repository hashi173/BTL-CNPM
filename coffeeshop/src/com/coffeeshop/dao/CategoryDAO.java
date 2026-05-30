package com.coffeeshop.dao;

import com.coffeeshop.model.Categories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * category_DAO - Truy xuất dữ liệu bảng categories.
 * Kế thừa từ lớp DAO để điều khiển truy nhập chung vào CSDL.
 */
public class CategoryDAO extends DAO {

    /**
     * Lấy danh sách tất cả danh mục.
     * getAllCategories() - được gọi từ ManageCategoryFrm và EditProductFrm.
     */
    public List<Categories> getAllCategories() {
        List<Categories> list = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY name";
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

    /**
     * Lấy danh mục theo ID.
     */
    public Categories getCategoryById(UUID id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
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

    /**
     * Thêm danh mục mới.
     * addCategory() - được gọi từ AddCategoryFrm.
     */
    public boolean addCategory(Categories category) {
        String sql = "INSERT INTO categories (id, name, description, created_at, updated_at) VALUES (?, ?, ?, NOW(), NOW())";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            UUID id = category.getId() != null ? category.getId() : UUID.randomUUID();
            ps.setObject(1, id);
            ps.setString(2, category.getName());
            ps.setString(3, category.getDescription());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật danh mục.
     * updateCategory() - được gọi từ AddCategoryFrm (chế độ chỉnh sửa).
     */
    public boolean updateCategory(Categories category) {
        String sql = "UPDATE categories SET name = ?, description = ?, updated_at = NOW() WHERE id = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setObject(3, category.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Xóa danh mục.
     * deleteCategory() - được gọi từ ManageCategoryFrm.
     */
    public boolean deleteCategory(UUID id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Tìm kiếm danh mục theo từ khóa.
     * searchCategories() - được gọi từ ManageCategoryFrm.
     */
    public List<Categories> searchCategories(String keyword) {
        List<Categories> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE LOWER(name) LIKE LOWER(?) OR CAST(id AS VARCHAR) LIKE ? ORDER BY name";
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

    private Categories mapResultSet(ResultSet rs) throws SQLException {
        Categories c = new Categories();
        c.setId(UUID.fromString(rs.getString("id")));
        c.setName(rs.getString("name"));
        c.setDescription(rs.getString("description"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        c.setUpdatedAt(rs.getTimestamp("updated_at"));
        return c;
    }
}
