package com.coffeeshop.test;

import org.junit.Assert;
import org.junit.Test;
import com.coffeeshop.model.Orders;
import com.coffeeshop.dao.OrderDAO;

/**
 * Lớp kiểm thử (JUnit) cho chức năng Theo dõi đơn hàng (Client side).
 * Phụ trách bởi Long - Nhóm chức năng: Khách hàng tra cứu mã đơn hàng.
 */
public class ClientOrderDAOTest {

    /**
     * Kịch bản: Khách hàng nhập mã tracking_code ĐÚNG ("CS-0001").
     * Kỳ vọng: Hàm trả về đối tượng Orders chứa đúng mã đó và trạng thái hiện tại.
     */
    @Test
    public void testTrackOrderStatus_ValidTrackingCode() {
        OrderDAO dao = new OrderDAO();
        Orders order = dao.getOrderByTrackingCode("CS-0001"); // Đơn hàng test đã tạo sẵn
        
        Assert.assertNotNull("Phải tìm thấy đơn hàng", order);
        Assert.assertEquals("Mã tracking code phải khớp CS-0001", "CS-0001", order.getTrackingCode());
        Assert.assertEquals("Trạng thái đơn hàng đầu tiên thường là PENDING", "PENDING", order.getStatus());
    }

    /**
     * Kịch bản: Khách hàng nhập mã tracking_code SAI hoặc không tồn tại ("CS-9999").
     * Kỳ vọng: Hàm trả về null, thông báo cho User là không tìm thấy đơn.
     */
    @Test
    public void testTrackOrderStatus_InvalidTrackingCode() {
        OrderDAO dao = new OrderDAO();
        Orders order = dao.getOrderByTrackingCode("CS-9999");
        
        Assert.assertNull("Không được tìm thấy đơn hàng cho tracking code ma (CS-9999)", order);
    }
}
