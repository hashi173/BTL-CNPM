package com.coffeeshop.test;

import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import com.coffeeshop.dao.OrderDAO;

/**
 * Lớp kiểm thử (JUnit) cho phần Thống kê.
 * Phụ trách bởi Bách - Nhóm chức năng: Xem thống kê.
 * Ghi chú: Logic thống kê được hiện thực bên trong OrderDAO.
 */
public class StatsDAOTest {

    /**
     * Kịch bản: Quản lý muốn xem thống kê tổng thể toàn bộ hệ thống (doanh thu tổng).
     * Kỳ vọng: Hàm trả về một số lớn hơn hoặc bằng 0.
     */
    @Test
    public void testGetOverallStats_WithData() {
        OrderDAO dao = new OrderDAO();
        double totalRev = dao.getTotalRevenue();
        
        Assert.assertTrue("Doanh thu tổng không được âm", totalRev >= 0);
    }

    /**
     * Kịch bản: Quản lý muốn xem doanh thu hôm nay.
     * Kỳ vọng: Hàm trả về doanh thu hôm nay >= 0.
     */
    @Test
    public void testGetTodayStats() {
        OrderDAO dao = new OrderDAO();
        double todayRev = dao.getTodayRevenue();
        
        Assert.assertTrue("Doanh thu hôm nay không được âm", todayRev >= 0);
    }

    /**
     * Kịch bản: Quản lý muốn xem biểu đồ doanh thu theo tháng.
     * Kỳ vọng: Trả về một List danh sách mảng Object chứa (Tháng, Năm, Doanh thu).
     */
    @Test
    public void testGetMonthlyStats() {
        OrderDAO dao = new OrderDAO();
        List<Object[]> monthlyStats = dao.getMonthlyRevenue();
        
        Assert.assertNotNull("Danh sách thống kê tháng không được null", monthlyStats);
    }
}
