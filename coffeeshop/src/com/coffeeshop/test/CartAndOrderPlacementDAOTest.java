package com.coffeeshop.test;

import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import java.util.UUID;
import com.coffeeshop.dao.ProductDAO;
import com.coffeeshop.dao.CartDAO;
import com.coffeeshop.dao.OrderDAO;

/**
 * Lớp kiểm thử (JUnit) cho quy trình đặt hàng của khách hàng.
 * Phụ trách bởi Thi - Nhóm chức năng: Tìm kiếm, xem sản phẩm, thêm vào giỏ, đặt hàng.
 */
public class CartAndOrderPlacementDAOTest {

    /**
     * Kịch bản: Khách hàng tìm kiếm sản phẩm theo từ khoá (vd: "Espresso").
     * Kỳ vọng: Trả về một List danh sách sản phẩm chứa từ khoá.
     */
    @Test
    public void testSearchProduct_Found() {
        ProductDAO dao = new ProductDAO();
        List<Object> products = dao.searchProducts("Espresso");
        
        Assert.assertNotNull("Danh sách tìm kiếm không được null", products);
        Assert.assertTrue("Phải tìm thấy ít nhất 1 sản phẩm có chữ Espresso", products.size() > 0);
    }

    /**
     * Kịch bản: Khách hàng nhấn vào một sản phẩm cụ thể để xem chi tiết.
     * Kỳ vọng: Truy vấn chi tiết sản phẩm thành công và trả về thông tin đầy đủ.
     */
    @Test
    public void testViewProductDetail() {
        ProductDAO dao = new ProductDAO();
        UUID productId = UUID.fromString("UUID-PROD-1"); // ID sản phẩm test
        Object product = dao.getProductDetail(productId);
        
        Assert.assertNotNull("Chi tiết sản phẩm không được null", product);
    }

    /**
     * Kịch bản: Khách hàng chọn cấu hình (Size, Đá, Đường) và ấn "Thêm vào giỏ".
     * Kỳ vọng: Lưu trữ tạm vào bảng giỏ hàng thành công (true).
     */
    @Test
    public void testAddToCart() {
        CartDAO dao = new CartDAO();
        UUID userId = UUID.fromString("UUID-USER-1");
        UUID productId = UUID.fromString("UUID-PROD-1");
        
        // Thêm 2 ly Size M, không đá
        boolean result = dao.addToCart(userId, productId, "Size M, No Ice", 2);
        Assert.assertTrue("Thêm sản phẩm vào giỏ phải thành công (true)", result);
    }

    /**
     * Kịch bản: Khách hàng vào giỏ hàng, điền thông tin người nhận và chốt "Xác nhận đặt hàng".
     * Kỳ vọng: Tạo mới một Order trong Database thành công và trả về ID của Order đó.
     */
    @Test
    public void testConfirmOrderPlacement() {
        OrderDAO dao = new OrderDAO();
        UUID userId = UUID.fromString("UUID-USER-1");
        
        // Đặt hàng từ dữ liệu trong giỏ của User
        UUID orderId = dao.placeOrderFromCart(userId, "Nguyen Van Thi", "0987654321", "123 Main St", "Call me before delivery");
        
        Assert.assertNotNull("Đặt hàng thành công phải trả về UUID của đơn hàng mới", orderId);
    }
}
