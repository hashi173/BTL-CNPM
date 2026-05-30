package com.coffeeshop.dao;

import com.coffeeshop.model.Users;

import java.sql.*;
import java.util.UUID;

/**
 * user_DAO - Truy xuất dữ liệu bảng users.
 * Kế thừa từ lớp DAO để điều khiển truy nhập chung vào CSDL.
 */
public class UserDAO extends DAO {

    /**
     * Kiểm tra thông tin đăng nhập.
     * checkLogin() - kiểm tra username/password trong CSDL.
     */
    public Users checkLogin(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND active = true";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
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
     * Lấy thông tin user theo ID.
     */
    public Users getUserById(UUID id) {
        String sql = "SELECT * FROM users WHERE id = ?";
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
     * Kiểm tra username đã tồn tại chưa.
     */
    public boolean checkUsernameExist(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true; // Giả sử tồn tại nếu lỗi để an toàn
    }

    /**
     * Đăng ký tài khoản mới (mặc định CLIENT)
     */
    public boolean registerUser(Users user) {
        String sql = "INSERT INTO users (id, username, password, full_name, email, phone, role, active, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, 'CLIENT', true, NOW(), NOW())";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            UUID id = UUID.randomUUID();
            ps.setObject(1, id);
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getEmail());
            ps.setString(6, user.getPhone());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Map ResultSet sang đối tượng Users.
     */
    private Users mapResultSet(ResultSet rs) throws SQLException {
        Users user = new Users();
        user.setId(UUID.fromString(rs.getString("id")));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setRole(rs.getString("role"));
        user.setActive(rs.getBoolean("active"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        return user;
    }
}
