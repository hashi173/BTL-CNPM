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
import com.coffeeshop.dao.OrderItemDAO;
import com.coffeeshop.model.OrderItems;

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
        UUID userId = UUID.fromString("b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a22"); // Seeded client user
        
        CartItems item = new CartItems();
        item.setProductId(UUID.fromString("f1eebc99-9c0b-4ef8-bb6d-6bb9bd380f11")); // Seeded product
        item.setQuantity(2);
        item.setOptions("Size M, No Ice");
        
        cDao.addCartItem(item, userId);
        
        // Kiểm tra giỏ hàng
        List<CartItems> cart = cDao.getAllCart(userId);
        Assert.assertNotNull(cart);
        System.out.println(">>> [THÀNH CÔNG] Đã tìm thấy sản phẩm và thêm vào giỏ hàng cho User: " + userId);
    }

    @Test
    public void testPlaceOrderFromCart() {
        OrderDAO dao = new OrderDAO();
        Orders order = new Orders();
        order.setId(UUID.randomUUID());
        order.setUserId(UUID.fromString("b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a22"));
        order.setCustomerName("Nguyen Van Thi");
        order.setPhone("0987654321");
        order.setAddressText("123 Main St");
        order.setNote("Call me before delivery");
        order.setTotalAmount(100000.0);
        order.setSubTotal(100000.0);
        order.setStatus("PENDING");
        
        Orders createdOrder = dao.createOrder(order);
        
        Assert.assertNotNull("Đơn hàng mới tạo không được null", createdOrder);
        System.out.println(">>> [THÀNH CÔNG] Đã khởi tạo đơn hàng mới với ID: " + createdOrder.getId());
    }

    @Test
    public void testAddOrderItem() {
        // 1. Tạo một đơn hàng trước (để đảm bảo không bị lỗi khóa ngoại)
        OrderDAO orderDao = new OrderDAO();
        Orders order = new Orders();
        UUID orderId = UUID.randomUUID();
        order.setId(orderId);
        order.setUserId(UUID.fromString("b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a22")); // Seeded user
        order.setCustomerName("Test OrderItem User");
        order.setSubTotal(50000.0);
        order.setTotalAmount(50000.0);
        order.setStatus("PENDING");
        orderDao.createOrder(order);

        // 2. Thêm 1 OrderItem vào đơn hàng vừa tạo
        OrderItemDAO itemDao = new OrderItemDAO();
        OrderItems item = new OrderItems();
        item.setOrderId(orderId);
        item.setProductId(UUID.fromString("f1eebc99-9c0b-4ef8-bb6d-6bb9bd380f11")); // Seeded product
        item.setSnapshotProductName("Espresso Test");
        item.setQuantity(1);
        item.setSnapshotUnitPrice(new BigDecimal("50000"));
        item.setSubTotal(new BigDecimal("50000"));
        item.setSnapshotOptions("No Sugar");

        boolean result = itemDao.addOrderItem(item);
        
        Assert.assertTrue("Thêm OrderItem phải trả về true", result);
        Assert.assertTrue("ID tự tăng của OrderItem phải > 0", item.getId() > 0);
        
        System.out.println(">>> [THÀNH CÔNG] Đã thêm thành công 1 OrderItem (ID = " + item.getId() + ") vào đơn hàng: " + orderId);
    }
}
