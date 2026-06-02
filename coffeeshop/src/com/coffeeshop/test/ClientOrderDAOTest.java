package com.coffeeshop.test;

import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import com.coffeeshop.dao.OrderDAO;
import com.coffeeshop.model.Orders;

/**
 * Lớp kiểm thử (JUnit) cho Module Theo dõi đơn hàng (Client/Customer).
 * Phụ trách bởi Long - Nhóm chức năng: Theo dõi đơn hàng (bằng tracking_code hoặc SĐT).
 */
public class ClientOrderDAOTest {

    @Test
    public void testTrackOrderStatus_ValidTrackingCode() {
        OrderDAO dao = new OrderDAO();
        List<Orders> orders = dao.searchOrders("CS-0001");
        
        Assert.assertNotNull("Phải trả về danh sách", orders);
        if (!orders.isEmpty()) {
            Orders order = orders.get(0);
            Assert.assertEquals("Mã tracking code phải khớp CS-0001", "CS-0001", order.getTrackingCode());
            Assert.assertNotNull("Trạng thái đơn hàng không được null", order.getStatus());
        }
    }

    @Test
    public void testTrackOrderStatus_InvalidTrackingCode() {
        OrderDAO dao = new OrderDAO();
        List<Orders> orders = dao.searchOrders("CS-9999");
        
        Assert.assertNotNull("Danh sách trả về không được null", orders);
        Assert.assertTrue("Không được tìm thấy đơn hàng cho tracking code ma (CS-9999)", orders.isEmpty());
    }
}
