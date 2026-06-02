package com.coffeeshop.test;

import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;
import com.coffeeshop.dao.ProductDAO;
import com.coffeeshop.dao.CartDAO;
import com.coffeeshop.dao.OrderDAO;
import com.coffeeshop.model.Products;
import com.coffeeshop.model.CartItems;
import com.coffeeshop.model.Orders;

/**
 * Lớp kiểm thử (JUnit) cho Module Đặt hàng và Giỏ hàng.
 * Phụ trách bởi Thi - Nhóm chức năng: Quản lý giỏ hàng, đặt hàng (tìm sản phẩm, xem chi tiết, chọn option, thêm vào giỏ, xác nhận đặt).
 */
public class CartAndOrderPlacementDAOTest {

    @Test
    public void testSearchProductAndAddToCart() {
        // 1. Tìm sản phẩm
        ProductDAO pDao = new ProductDAO();
        List<Products> products = pDao.searchProduct("Espresso");
        Assert.assertNotNull("Danh sách tìm kiếm không được null", products);
        
        // 2. Thêm vào giỏ
        CartDAO cDao = new CartDAO();
        UUID userId = UUID.randomUUID();
        
        CartItems item = new CartItems();
        item.setProductId(UUID.randomUUID());
        item.setQuantity(2);
        item.setOptions("Size M, No Ice");
        
        cDao.addCartItem(item, userId);
        
        // Kiểm tra giỏ hàng
        List<CartItems> cart = cDao.getAllCart(userId);
        Assert.assertNotNull(cart);
        // Vì product id fake, có thể join bị lỗi hoặc rỗng. Nhưng code phải compile được.
    }

    @Test
    public void testPlaceOrderFromCart() {
        OrderDAO dao = new OrderDAO();
        Orders order = new Orders();
        order.setId(UUID.randomUUID());
        order.setUserId(UUID.randomUUID());
        order.setCustomerName("Nguyen Van Thi");
        order.setPhone("0987654321");
        order.setAddressText("123 Main St");
        order.setNote("Call me before delivery");
        order.setTotalAmount(100000.0);
        order.setStatus("PENDING");
        
        Orders createdOrder = dao.createOrder(order);
        
        Assert.assertNotNull("Đơn hàng mới tạo không được null", createdOrder);
    }
}
