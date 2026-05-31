package com.coffeeshop.dao;

import com.coffeeshop.model.OrderItems;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * orderitem_DAO - Truy xuất dữ liệu bảng order_items.
 * Kế thừa từ lớp DAO để điều khiển truy nhập chung vào CSDL.
 */
public class OrderItemDAO extends DAO {

    /**
     * Thêm chi tiết đơn hàng.
     * addOrderItem() - được gọi từ CheckoutView sau khi createOrder.
     */
    public boolean addOrderItem(OrderItems item) {
        String sql = "INSERT INTO order_items (order_id, product_id, snapshot_product_name, " +
                     "quantity, snapshot_unit_price, snapshot_options, sub_total, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, item.getOrderId());
            ps.setObject(2, item.getProductId());
            ps.setString(3, item.getSnapshotProductName());
            ps.setInt(4, item.getQuantity());
            ps.setBigDecimal(5, item.getSnapshotUnitPrice());
            ps.setString(6, item.getSnapshotOptions());
            ps.setBigDecimal(7, item.getSubTotal());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    item.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy danh sách chi tiết đơn hàng theo orderId.
     * getOrderItems() - được gọi từ OrderDetailView.
     */
    public List<OrderItems> getOrderItems(UUID orderId) {
        List<OrderItems> list = new ArrayList<>();
        String sql = "SELECT * FROM order_items WHERE order_id = ? ORDER BY id";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private OrderItems mapResultSet(ResultSet rs) throws SQLException {
        OrderItems item = new OrderItems();
        item.setId(rs.getInt("id"));
        item.setOrderId(UUID.fromString(rs.getString("order_id")));
        String productId = rs.getString("product_id");
        if (productId != null) item.setProductId(UUID.fromString(productId));
        item.setSnapshotProductName(rs.getString("snapshot_product_name"));
        item.setQuantity(rs.getInt("quantity"));
        item.setSnapshotUnitPrice(rs.getBigDecimal("snapshot_unit_price"));
        item.setSnapshotOptions(rs.getString("snapshot_options"));
        item.setSubTotal(rs.getBigDecimal("sub_total"));
        item.setCreatedAt(rs.getTimestamp("created_at"));
        item.setUpdatedAt(rs.getTimestamp("updated_at"));
        return item;
    }
}
