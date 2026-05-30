package com.coffeeshop.dao;

import com.coffeeshop.model.CartItems;
import com.coffeeshop.model.Products;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * cart_DAO - Quản lý giỏ hàng (persist vào CSDL).
 * Kế thừa từ lớp DAO.
 */
public class CartDAO extends DAO {

    private final ProductDAO productDAO = new ProductDAO();

    /**
     * Thêm mặt hàng vào giỏ hàng.
     * addCartItem() - được gọi từ ProductDetailFrm.
     * Nếu đã có sản phẩm giống (cùng product + tùy chọn), tăng số lượng.
     */
    public void addCartItem(CartItems newItem, UUID userId) {
        // Kiểm tra đã có sản phẩm giống trong giỏ chưa
        List<CartItems> existingItems = getAllCart(userId);
        for (CartItems item : existingItems) {
            if (item.isSameItem(newItem)) {
                updateQuantity(item.getId(), item.getQuantity() + newItem.getQuantity());
                return;
            }
        }
        // Nếu chưa có, thêm mới
        String sql = "INSERT INTO cart_items (id, user_id, product_id, quantity, options, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, NOW(), NOW())";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            UUID id = newItem.getId() != null ? newItem.getId() : UUID.randomUUID();
            ps.setObject(1, id);
            ps.setObject(2, userId);
            ps.setObject(3, newItem.getProductId());
            ps.setInt(4, newItem.getQuantity());
            ps.setString(5, newItem.getOptions());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy danh sách tất cả mặt hàng trong giỏ của user.
     * getAllCart() - được gọi từ CartFrm và CheckoutFrm.
     */
    public List<CartItems> getAllCart(UUID userId) {
        List<CartItems> list = new ArrayList<>();
        String sql = "SELECT ci.*, p.name AS product_name, p.base_price " +
                     "FROM cart_items ci " +
                     "JOIN products p ON ci.product_id = p.id " +
                     "WHERE ci.user_id = ? ORDER BY ci.created_at";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, userId);
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
     * Cập nhật số lượng mặt hàng trong giỏ.
     */
    public void updateQuantity(UUID itemId, int quantity) {
        if (quantity <= 0) {
            removeCartItem(itemId);
            return;
        }
        String sql = "UPDATE cart_items SET quantity = ?, updated_at = NOW() WHERE id = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, quantity);
            ps.setObject(2, itemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Xóa một mặt hàng khỏi giỏ.
     * removeCartItem() - được gọi từ CartFrm.
     */
    public void removeCartItem(UUID itemId) {
        String sql = "DELETE FROM cart_items WHERE id = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, itemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Xóa toàn bộ giỏ hàng của user.
     * clearCart() - được gọi từ CheckoutFrm sau khi đặt hàng thành công.
     */
    public void clearCart(UUID userId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tính tổng tiền giỏ hàng.
     */
    public double getTotalAmount(UUID userId) {
        String sql = "SELECT SUM(ci.quantity * p.base_price) AS total " +
                     "FROM cart_items ci JOIN products p ON ci.product_id = p.id " +
                     "WHERE ci.user_id = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Tổng số lượng sản phẩm trong giỏ.
     */
    public int getTotalItems(UUID userId) {
        String sql = "SELECT COALESCE(SUM(quantity), 0) AS total FROM cart_items WHERE user_id = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private CartItems mapResultSet(ResultSet rs) throws SQLException {
        CartItems item = new CartItems();
        item.setId(UUID.fromString(rs.getString("id")));
        String userId = rs.getString("user_id");
        if (userId != null) item.setUserId(UUID.fromString(userId));
        item.setProductId(UUID.fromString(rs.getString("product_id")));
        item.setProductName(rs.getString("product_name"));
        item.setUnitPrice(rs.getDouble("base_price"));
        item.setQuantity(rs.getInt("quantity"));
        item.setOptions(rs.getString("options"));
        item.setCreatedAt(rs.getTimestamp("created_at"));
        item.setUpdatedAt(rs.getTimestamp("updated_at"));
        return item;
    }
}
