package com.coffeeshop.dao;

import com.coffeeshop.model.Orders;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * order_DAO - Truy xuất dữ liệu bảng orders.
 * Kế thừa từ lớp DAO để điều khiển truy nhập chung vào CSDL.
 */
public class OrderDAO extends DAO {

    /**
     * Tạo đơn hàng mới.
     * createOrder() - được gọi từ CheckoutFrm.
     */
    public Orders createOrder(Orders order) {
        String sql = "INSERT INTO orders (id, user_id, customer_name, phone, address_text, note, " +
                     "sub_total, total_amount, order_type, status, tracking_code, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            UUID id = order.getId() != null ? order.getId() : UUID.randomUUID();
            order.setId(id);
            String trackingCode = order.getTrackingCode() != null ? order.getTrackingCode() : generateTrackingCode();
            order.setTrackingCode(trackingCode);

            ps.setObject(1, id);
            ps.setObject(2, order.getUserId());
            ps.setString(3, order.getCustomerName());
            ps.setString(4, order.getPhone());
            ps.setString(5, order.getAddressText());
            ps.setString(6, order.getNote());
            ps.setDouble(7, order.getSubTotal());
            ps.setDouble(8, order.getTotalAmount());
            ps.setString(9, order.getOrderType());
            ps.setString(10, order.getStatus());
            ps.setString(11, trackingCode);
            ps.executeUpdate();
            return order;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy tất cả đơn hàng.
     * getAllOrders() - được gọi từ OrderManagementFrm.
     */
    public List<Orders> getAllOrders() {
        List<Orders> list = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY created_at DESC";
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
     * Lấy danh sách đơn hàng theo user.
     * getOrdersByUser() - được gọi từ OrderListFrm.
     */
    public List<Orders> getOrdersByUser(UUID userId) {
        List<Orders> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";
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

    public List<Orders> searchOrdersByUser(UUID userId, String keyword) {
        List<Orders> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? " +
                     "AND (LOWER(tracking_code) LIKE LOWER(?) OR " +
                     "CAST(created_at AS VARCHAR) LIKE ?) ORDER BY created_at DESC";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, userId);
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
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
     * Lấy chi tiết đơn hàng theo ID.
     * getOrderDetail() - được gọi từ OrderDetailFrm và CancelConfirmFrm.
     */
    public Orders getOrderDetail(UUID orderId) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, orderId);
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
     * Kiểm tra trạng thái đơn hàng.
     * checkOrderStatus() - kiểm tra xem đơn có thể hủy hay không.
     */
    public String checkOrderStatus(UUID orderId) {
        String sql = "SELECT status FROM orders WHERE id = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Hủy đơn hàng - cập nhật trạng thái thành CANCELLED.
     * cancelOrder() - được gọi từ CancelConfirmFrm.
     */
    public boolean cancelOrder(UUID orderId) {
        String sql = "UPDATE orders SET status = 'CANCELLED', updated_at = NOW() WHERE id = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật trạng thái đơn hàng.
     * updateOrderStatus() - được gọi từ OrderDetailFrm.
     */
    public boolean updateOrderStatus(UUID orderId, String status) {
        String sql = "UPDATE orders SET status = ?, updated_at = NOW() WHERE id = ?";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setObject(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Thống kê theo sản phẩm.
     * getStatByProduct() - được gọi từ StatFrm.
     */
    public List<Object[]> getStatByProduct(String productName) {
        return getStatByProductAndTime(productName, null, null);
    }

    public List<Object[]> getStatByProductAndTime(String productName, String fromDate, String toDate) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT oi.snapshot_product_name, SUM(oi.quantity) AS total_qty, " +
                     "SUM(oi.sub_total) AS total_revenue " +
                     "FROM order_items oi JOIN orders o ON oi.order_id = o.id " +
                     "WHERE o.status IN ('COMPLETED', 'DELIVERED') ";
        if (productName != null && !productName.trim().isEmpty()) {
            sql += "AND LOWER(oi.snapshot_product_name) LIKE LOWER(?) ";
        }
        if (fromDate != null && !fromDate.trim().isEmpty()) {
            sql += "AND o.created_at >= ?::timestamp ";
        }
        if (toDate != null && !toDate.trim().isEmpty()) {
            sql += "AND o.created_at <= (?::timestamp + interval '1 day') ";
        }
        sql += "GROUP BY oi.snapshot_product_name ORDER BY total_revenue DESC";

        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            int pIndex = 1;
            if (productName != null && !productName.trim().isEmpty()) {
                ps.setString(pIndex++, "%" + productName.trim() + "%");
            }
            if (fromDate != null && !fromDate.trim().isEmpty()) {
                ps.setString(pIndex++, fromDate.trim());
            }
            if (toDate != null && !toDate.trim().isEmpty()) {
                ps.setString(pIndex++, toDate.trim());
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[3];
                row[0] = rs.getString("snapshot_product_name");
                row[1] = rs.getLong("total_qty");
                row[2] = rs.getDouble("total_revenue");
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Thống kê doanh thu theo tháng.
     * getMonthlyRevenue() - được gọi từ StatFrm.
     */
    public List<Object[]> getMonthlyRevenue() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT EXTRACT(YEAR FROM created_at) AS year, " +
                     "EXTRACT(MONTH FROM created_at) AS month, " +
                     "SUM(total_amount) AS total " +
                     "FROM orders WHERE status = 'COMPLETED' " +
                     "GROUP BY EXTRACT(YEAR FROM created_at), EXTRACT(MONTH FROM created_at) " +
                     "ORDER BY year DESC, month DESC";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[3];
                row[0] = rs.getInt("year");
                row[1] = rs.getInt("month");
                row[2] = rs.getDouble("total");
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Tìm kiếm đơn hàng theo từ khóa (tên khách, SĐT, mã đơn).
     */
    public List<Orders> searchOrders(String keyword) {
        List<Orders> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE LOWER(customer_name) LIKE LOWER(?) OR phone LIKE ? OR LOWER(tracking_code) LIKE LOWER(?) OR CAST(id AS VARCHAR) LIKE ? ORDER BY created_at DESC";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ps.setString(4, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private String generateTrackingCode() {
        String sql = "SELECT COUNT(*) FROM orders";
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return String.format("ORD-%06d", rs.getLong(1) + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "ORD-" + System.currentTimeMillis();
    }

    private Orders mapResultSet(ResultSet rs) throws SQLException {
        Orders o = new Orders();
        o.setId(UUID.fromString(rs.getString("id")));
        String userId = rs.getString("user_id");
        if (userId != null) o.setUserId(UUID.fromString(userId));
        o.setCustomerName(rs.getString("customer_name"));
        o.setPhone(rs.getString("phone"));
        o.setAddressText(rs.getString("address_text"));
        o.setNote(rs.getString("note"));
        o.setSubTotal(rs.getDouble("sub_total"));
        o.setTotalAmount(rs.getDouble("total_amount"));
        o.setOrderType(rs.getString("order_type"));
        o.setStatus(rs.getString("status"));
        o.setTrackingCode(rs.getString("tracking_code"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        o.setUpdatedAt(rs.getTimestamp("updated_at"));
        return o;
    }

    public List<Object[]> getOrderDetailsByProductAndTime(String productName, String fromDate, String toDate) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT o.tracking_code, o.customer_name, o.phone, o.total_amount, o.status, o.created_at, oi.quantity " +
                     "FROM orders o JOIN order_items oi ON o.id = oi.order_id " +
                     "WHERE o.status IN ('COMPLETED', 'DELIVERED') AND LOWER(oi.snapshot_product_name) = LOWER(?) ";
        
        if (fromDate != null && !fromDate.trim().isEmpty()) {
            sql += "AND o.created_at >= ?::timestamp ";
        }
        if (toDate != null && !toDate.trim().isEmpty()) {
            sql += "AND o.created_at <= (?::timestamp + interval '1 day') ";
        }
        sql += "ORDER BY o.created_at DESC";

        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            int pIndex = 1;
            ps.setString(pIndex++, productName);
            
            if (fromDate != null && !fromDate.trim().isEmpty()) {
                ps.setString(pIndex++, fromDate.trim());
            }
            if (toDate != null && !toDate.trim().isEmpty()) {
                ps.setString(pIndex++, toDate.trim());
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[7];
                row[0] = rs.getString("tracking_code");
                row[1] = rs.getString("customer_name");
                row[2] = rs.getString("phone");
                row[3] = rs.getDouble("total_amount");
                row[4] = rs.getString("status");
                row[5] = rs.getTimestamp("created_at");
                row[6] = rs.getInt("quantity");
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
