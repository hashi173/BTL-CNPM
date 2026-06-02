package com.coffeeshop.test;

import org.junit.Assert;
import org.junit.Test;
import java.util.UUID;
import com.coffeeshop.dao.ProductDAO;
import com.coffeeshop.dao.CategoryDAO;

/**
 * Lớp kiểm thử (JUnit) cho ProductDAO và CategoryDAO.
 * Phụ trách bởi Quỳnh - Nhóm chức năng: Quản lý sản phẩm và danh mục (Thêm, Sửa, Xoá, Vô hiệu hoá).
 */
public class ProductAndCategoryDAOTest {

    // ==========================================
    // PHẦN 1: QUẢN LÝ SẢN PHẨM (PRODUCT)
    // ==========================================

    /**
     * Kịch bản: Quản trị viên thêm một sản phẩm mới hợp lệ.
     * Kỳ vọng: Trả về true, sản phẩm được lưu vào DB.
     */
    @Test
    public void testAddProduct_Success() {
        ProductDAO dao = new ProductDAO();
        boolean result = dao.addProduct("Espresso", 35000.0, "UUID-CAT-1", "Delicious");
        Assert.assertTrue("Thêm sản phẩm phải thành công (true)", result);
    }

    /**
     * Kịch bản: Quản trị viên cập nhật tên và giá của một sản phẩm đã có.
     * Kỳ vọng: Trả về true, thông tin mới đè lên thông tin cũ.
     */
    @Test
    public void testUpdateProduct_Success() {
        ProductDAO dao = new ProductDAO();
        UUID productId = UUID.fromString("UUID-PROD-1"); // Giả sử ID này tồn tại trong DB test
        boolean result = dao.updateProduct(productId, "Espresso Large", 45000.0);
        Assert.assertTrue("Cập nhật sản phẩm phải thành công (true)", result);
    }

    /**
     * Kịch bản: Quản trị viên vô hiệu hoá (inactive) một sản phẩm thay vì xoá cứng.
     * Kỳ vọng: Trả về true và trạng thái isProductActive của sản phẩm đó thành false.
     */
    @Test
    public void testInactiveProduct_Success() {
        ProductDAO dao = new ProductDAO();
        UUID productId = UUID.fromString("UUID-PROD-1");
        
        // Vô hiệu hoá sản phẩm
        boolean result = dao.inactiveProduct(productId);
        Assert.assertTrue("Vô hiệu hoá sản phẩm phải thành công (true)", result);
        
        // Kiểm tra lại trạng thái
        Assert.assertFalse("Sản phẩm không còn active nữa", dao.isProductActive(productId));
    }

    // ==========================================
    // PHẦN 2: QUẢN LÝ DANH MỤC (CATEGORY)
    // ==========================================

    /**
     * Kịch bản: Quản trị viên thêm một danh mục mới.
     * Kỳ vọng: Trả về true, danh mục mới tạo thành công.
     */
    @Test
    public void testAddCategory_Success() {
        CategoryDAO dao = new CategoryDAO();
        boolean result = dao.addCategory("Coffee");
        Assert.assertTrue("Thêm danh mục phải thành công (true)", result);
    }

    /**
     * Kịch bản: Quản trị viên đổi tên danh mục.
     * Kỳ vọng: Trả về true.
     */
    @Test
    public void testUpdateCategory_Success() {
        CategoryDAO dao = new CategoryDAO();
        UUID catId = UUID.fromString("UUID-CAT-1");
        boolean result = dao.updateCategory(catId, "Hot Coffee");
        Assert.assertTrue("Cập nhật danh mục phải thành công (true)", result);
    }

    /**
     * Kịch bản: Quản trị viên xoá hẳn một danh mục.
     * Kỳ vọng: Trả về true (thường kéo theo CASCADE hoặc chuyển sản phẩm sang danh mục khác).
     */
    @Test
    public void testDeleteCategory_Success() {
        CategoryDAO dao = new CategoryDAO();
        UUID catId = UUID.fromString("UUID-CAT-1");
        boolean result = dao.deleteCategory(catId);
        Assert.assertTrue("Xoá danh mục phải thành công (true)", result);
    }
}
