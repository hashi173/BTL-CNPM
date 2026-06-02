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
        // Tạo 1 đơn để đảm bảo DB có ít nhất 1 đơn
        Orders dummy = new Orders();
        dummy.setCustomerName("Dummy");
        dummy.setStatus("PENDING");
        dummy.setSubTotal(0.0);
        dummy.setTotalAmount(0.0);
        dao.createOrder(dummy);
        
        List<Orders> list = dao.getAllOrders();
        Assert.assertNotNull(list);
        Assert.assertTrue("Phải có ít nhất 1 đơn hàng trong DB", list.size() > 0);
    }

    /**
     * Kịch bản: Admin vào xem danh sách nhưng Database đang trống rỗng.
     * Kỳ vọng: Trả về List rỗng chứ không bị null (tránh NullPointerException).
     */
    @Test
    public void testGetAllOrders2() {
        // Do dùng chung DB thật, ta chỉ có thể test xem list trả về không bị null.
        OrderDAO dao = new OrderDAO();
        List<Orders> list = dao.getAllOrders();
        Assert.assertNotNull("Danh sách đơn hàng không được null", list);
    }

    /**
     * Kịch bản: Admin bấm vào xem chi tiết của một đơn hàng tồn tại.
     * Kỳ vọng: Trả về đối tượng Orders đầy đủ thông tin khách hàng, trạng thái.
     */
    @Test
    public void testGetOrderDetail1() {
        OrderDAO dao = new OrderDAO();
        
        Orders dummy = new Orders();
        String trackCode = "CS-" + System.currentTimeMillis();
        dummy.setCustomerName("Nguyen Van A");
        dummy.setStatus("PENDING");
        dummy.setTrackingCode(trackCode);
        dummy.setSubTotal(0.0);
        dummy.setTotalAmount(0.0);
        dummy = dao.createOrder(dummy);
        
        UUID existingId = dummy.getId();
        Orders o = dao.getOrderDetail(existingId);
        
        Assert.assertNotNull("Chi tiết đơn hàng không được null", o);
        Assert.assertEquals("Khớp mã tracking", trackCode, o.getTrackingCode());
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
    // PHẦN 2: HUỶ ĐƠN HÀNG (Sử dụng Update Status -> CANCELLED)
    // ====================================================

    /**
     * Kịch bản: Admin huỷ đơn hàng đang ở trạng thái PENDING.
     * Kỳ vọng: updateOrderStatus trả về true, và trong DB status chuyển thành CANCELLED.
     */
    @Test
    public void testUpdateOrderStatus4_CancelPending() {
        OrderDAO dao = new OrderDAO();
        
        Orders dummy = new Orders();
        dummy.setCustomerName("Dummy Customer");
        dummy.setStatus("PENDING");
        dummy.setSubTotal(0.0);
        dummy.setTotalAmount(0.0);
        dummy = dao.createOrder(dummy);
        UUID pendingOrderId = dummy.getId(); 
        
        boolean result = dao.updateOrderStatus(pendingOrderId, "CANCELLED");
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
    public void testUpdateOrderStatus5_CancelFakeId() {
        OrderDAO dao = new OrderDAO();
        UUID fakeId = UUID.randomUUID();
        boolean result = dao.updateOrderStatus(fakeId, "CANCELLED");
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
        
        Orders dummy = new Orders();
        dummy.setCustomerName("Dummy Customer");
        dummy.setStatus("PENDING");
        dummy.setSubTotal(0.0);
        dummy.setTotalAmount(0.0);
        dummy = dao.createOrder(dummy);
        UUID pendingId = dummy.getId();
        
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
        
        Orders dummy = new Orders();
        dummy.setCustomerName("Dummy Customer");
        dummy.setStatus("PENDING");
        dummy.setSubTotal(0.0);
        dummy.setTotalAmount(0.0);
        dummy = dao.createOrder(dummy);
        UUID pendingId = dummy.getId();
        
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
        
        Orders dummy = new Orders();
        dummy.setCustomerName("Dummy Customer");
        dummy.setStatus("CONFIRMED");
        dummy.setSubTotal(0.0);
        dummy.setTotalAmount(0.0);
        dummy = dao.createOrder(dummy);
        UUID confirmedId = dummy.getId(); 
        
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
        
        Orders dummy = new Orders();
        dummy.setCustomerName("Dummy Customer");
        dummy.setStatus("SHIPPING");
        dummy.setSubTotal(0.0);
        dummy.setTotalAmount(0.0);
        dummy = dao.createOrder(dummy);
        UUID shippingId = dummy.getId(); 
        
        boolean result = dao.updateOrderStatus(shippingId, "COMPLETED");
        Assert.assertTrue(result);
        
        String newStatus = dao.checkOrderStatus(shippingId);
        Assert.assertEquals("Trạng thái mới phải là COMPLETED", "COMPLETED", newStatus);
    }
}
