package com.coffeeshop.test;

import org.junit.Assert;
import org.junit.Test;
import com.coffeeshop.model.Orders;
import com.coffeeshop.dao.OrderDAO;
import java.util.List;
import java.util.UUID;

/**
 * Lớp kiểm thử (JUnit) cho nghiệp vụ quản lý đơn hàng của Admin.
 * Phụ trách bởi Hà - Nhóm chức năng: Xem danh sách, xem chi tiết, huỷ đơn, cập nhật trạng thái đơn hàng.
 * File code này đi kèm theo chuẩn trong file docs Hashi.md.
 */
public class OrderDAOTest {

    // ====================================================
    // PHẦN 1: XEM DANH SÁCH & CHI TIẾT ĐƠN HÀNG (View Order)
    // ====================================================

    /**
     * Kịch bản: Admin muốn xem tất cả đơn hàng (chuẩn: đang có 3 đơn trong DB giả lập).
     * Kỳ vọng: Trả về list gồm 3 phần tử. Đơn đầu tiên là CS-0001.
     */
    @Test
    public void testGetAllOrders1() {
        OrderDAO dao = new OrderDAO();
        List<Orders> list = dao.getAllOrders();
        Assert.assertNotNull(list);
        Assert.assertEquals("Phải có 3 đơn hàng trong DB", 3, list.size());
        Assert.assertEquals("Mã tracking của đơn đầu tiên phải là CS-0001", "CS-0001", list.get(0).getTrackingCode());
    }

    /**
     * Kịch bản: Admin vào xem danh sách nhưng Database đang trống rỗng.
     * Kỳ vọng: Trả về List rỗng chứ không bị null (tránh NullPointerException).
     */
    @Test
    public void testGetAllOrders2() {
        // Giả lập DB đã bị xoá hết dữ liệu
        OrderDAO dao = new OrderDAO();
        List<Orders> list = dao.getAllOrders();
        Assert.assertNotNull(list);
        Assert.assertEquals("Danh sách đơn hàng phải rỗng", 0, list.size());
    }

    /**
     * Kịch bản: Admin bấm vào xem chi tiết của một đơn hàng tồn tại.
     * Kỳ vọng: Trả về đối tượng Orders đầy đủ thông tin khách hàng, trạng thái.
     */
    @Test
    public void testGetOrderDetail1() {
        OrderDAO dao = new OrderDAO();
        UUID existingId = UUID.fromString("<UUID-1>"); // UUID của đơn CS-0001
        Orders o = dao.getOrderDetail(existingId);
        
        Assert.assertNotNull("Chi tiết đơn hàng không được null", o);
        Assert.assertEquals("Khớp mã tracking", "CS-0001", o.getTrackingCode());
        Assert.assertEquals("Khớp tên người mua", "Nguyen Van A", o.getCustomerName());
        Assert.assertEquals("Trạng thái phải là PENDING", "PENDING", o.getStatus());
    }

    /**
     * Kịch bản: Admin truy xuất một orderId (UUID) bậy bạ hoặc không tồn tại.
     * Kỳ vọng: Trả về null thay vì crash hệ thống.
     */
    @Test
    public void testGetOrderDetail2() {
        OrderDAO dao = new OrderDAO();
        UUID nonExistingId = UUID.randomUUID();
        Orders o = dao.getOrderDetail(nonExistingId);
        Assert.assertNull("Đơn hàng ma phải trả về null", o);
    }

    // ====================================================
    // PHẦN 2: HUỶ ĐƠN HÀNG (Cancel Order)
    // ====================================================

    /**
     * Kịch bản: Admin huỷ đơn hàng đang ở trạng thái PENDING.
     * Kỳ vọng: cancelOrder trả về true, và trong DB status chuyển thành CANCELLED.
     */
    @Test
    public void testCancelOrder1() {
        OrderDAO dao = new OrderDAO();
        UUID pendingOrderId = UUID.fromString("<UUID-1>"); 
        
        boolean result = dao.cancelOrder(pendingOrderId);
        Assert.assertTrue("Huỷ đơn hàng tồn tại phải thành công (true)", result);
        
        // Kiểm tra lại database xem đã cập nhật thành CANCELLED chưa
        String status = dao.checkOrderStatus(pendingOrderId);
        Assert.assertEquals("Trạng thái trong DB phải là CANCELLED", "CANCELLED", status);
    }

    /**
     * Kịch bản: Admin cố tình gọi lệnh huỷ cho một đơn hàng không tồn tại.
     * Kỳ vọng: Không có dòng nào bị ảnh hưởng, trả về false.
     */
    @Test
    public void testCancelOrder2() {
        OrderDAO dao = new OrderDAO();
        UUID fakeId = UUID.randomUUID();
        boolean result = dao.cancelOrder(fakeId);
        Assert.assertFalse("Huỷ đơn hàng không tồn tại phải trả về false", result);
    }

    // ====================================================
    // PHẦN 3: CẬP NHẬT TRẠNG THÁI (Update Status)
    // ====================================================

    /**
     * Helper Test: Kiểm tra lấy trạng thái hiện tại của đơn hàng.
     */
    @Test
    public void testCheckOrderStatus1() {
        OrderDAO dao = new OrderDAO();
        UUID pendingId = UUID.fromString("<UUID-1>");
        String status = dao.checkOrderStatus(pendingId);
        Assert.assertNotNull(status);
        Assert.assertEquals("Trạng thái lấy lên phải là PENDING", "PENDING", status);
    }

    /**
     * Helper Test: Lấy trạng thái của ID sai.
     */
    @Test
    public void testCheckOrderStatus3() {
        OrderDAO dao = new OrderDAO();
        UUID fakeId = UUID.randomUUID();
        String status = dao.checkOrderStatus(fakeId);
        Assert.assertNull("Lấy trạng thái từ ID sai phải trả về null", status);
    }

    /**
     * Kịch bản: Cập nhật trạng thái PENDING sang CONFIRMED (Đã xác nhận).
     */
    @Test
    public void testUpdateOrderStatus1() {
        OrderDAO dao = new OrderDAO();
        UUID pendingId = UUID.fromString("<UUID-1>");
        
        boolean result = dao.updateOrderStatus(pendingId, "CONFIRMED");
        Assert.assertTrue("Update status phải trả về true", result);
        
        String newStatus = dao.checkOrderStatus(pendingId);
        Assert.assertEquals("Trạng thái mới phải là CONFIRMED", "CONFIRMED", newStatus);
    }

    /**
     * Kịch bản: Cập nhật trạng thái CONFIRMED sang SHIPPING (Đang giao).
     */
    @Test
    public void testUpdateOrderStatus2() {
        OrderDAO dao = new OrderDAO();
        UUID confirmedId = UUID.fromString("<UUID-2>"); 
        
        boolean result = dao.updateOrderStatus(confirmedId, "SHIPPING");
        Assert.assertTrue(result);
        
        String newStatus = dao.checkOrderStatus(confirmedId);
        Assert.assertEquals("Trạng thái mới phải là SHIPPING", "SHIPPING", newStatus);
    }

    /**
     * Kịch bản: Cập nhật trạng thái SHIPPING sang COMPLETED (Hoàn thành).
     */
    @Test
    public void testUpdateOrderStatus3() {
        OrderDAO dao = new OrderDAO();
        UUID shippingId = UUID.fromString("<UUID-2>"); 
        
        boolean result = dao.updateOrderStatus(shippingId, "COMPLETED");
        Assert.assertTrue(result);
        
        String newStatus = dao.checkOrderStatus(shippingId);
        Assert.assertEquals("Trạng thái mới phải là COMPLETED", "COMPLETED", newStatus);
    }
}
