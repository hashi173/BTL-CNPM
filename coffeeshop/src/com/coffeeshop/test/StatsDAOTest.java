package com.coffeeshop.test;

import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import java.util.Map;
import com.coffeeshop.dao.StatsDAO;

/**
 * Lớp kiểm thử (JUnit) cho StatsDAO.
 * Phụ trách bởi Bách - Nhóm chức năng: Xem thống kê.
 */
public class StatsDAOTest {

    /**
     * Kịch bản: Quản lý muốn xem thống kê tổng thể toàn bộ hệ thống.
     * Kỳ vọng: Hàm trả về một Map không rỗng, chứa các key cơ bản như 'totalRevenue' (tổng doanh thu) và 'totalOrders' (tổng đơn hàng).
     */
    @Test
    public void testGetOverallStats_WithData() {
        StatsDAO dao = new StatsDAO();
        Map<String, Object> stats = dao.getOverallStats();
        
        Assert.assertNotNull("Kết quả thống kê không được null", stats);
        Assert.assertTrue("Phải có key thống kê tổng doanh thu", stats.containsKey("totalRevenue"));
        Assert.assertTrue("Phải có key thống kê tổng số đơn hàng", stats.containsKey("totalOrders"));
    }

    /**
     * Kịch bản: Quản lý muốn xem thống kê của một tháng cụ thể có dữ liệu (Tháng 10/2023).
     * Kỳ vọng: Trả về một List danh sách số liệu thống kê của tháng đó.
     */
    @Test
    public void testGetMonthlyStats_ValidMonth() {
        StatsDAO dao = new StatsDAO();
        List<Map<String, Object>> monthlyStats = dao.getMonthlyStats(10, 2023);
        
        Assert.assertNotNull("Danh sách thống kê tháng không được null", monthlyStats);
    }

    /**
     * Kịch bản: Quản lý xem thống kê của một tháng không hợp lệ hoặc chưa có dữ liệu (Tháng 13/2023).
     * Kỳ vọng: Trả về một List rỗng (không có lỗi exception ném ra).
     */
    @Test
    public void testGetMonthlyStats_NoData() {
        StatsDAO dao = new StatsDAO();
        List<Map<String, Object>> monthlyStats = dao.getMonthlyStats(13, 2023);
        
        Assert.assertNotNull("Danh sách thống kê tháng không được null", monthlyStats);
        Assert.assertTrue("Danh sách thống kê phải rỗng vì tháng 13 không tồn tại", monthlyStats.isEmpty());
    }
}
