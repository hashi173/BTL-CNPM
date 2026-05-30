# Usecase to Implementation Mapping

This document traces the use cases from `BTL-CNPM1.md` to their concrete Java Swing implementations.

## Client Usecases

### 1. Đặt đơn hàng (Order Placement)
- **Flow**: `LoginFrm` -> `HomeFrm` -> `MenuFrm` -> `ProductDetailFrm` -> `CartFrm` -> `CheckoutFrm` -> `OrderListFrm`
- **Actions**:
  - Xem danh sách món: `MenuFrm.loadProducts()` -> `ProductDAO.getAllProducts()`
  - Tùy chỉnh (Đường, Đá, Size): `ProductDetailFrm`
  - Giỏ hàng: `CartDAO.addCartItem()`, `CartDAO.getAllCart()`
  - Thanh toán: `CheckoutFrm.actionPerformed()` -> `OrderDAO.createOrder()` -> `OrderItemDAO.addOrderItem()` -> `CartDAO.clearCart()`

### 2. Theo dõi đơn hàng (Order Tracking & Cancellation)
- **Flow**: `HomeFrm` -> `OrderListFrm` -> `CancelConfirmFrm`
- **Actions**:
  - Xem đơn hàng: `OrderListFrm.loadOrders()` -> `OrderDAO.getOrdersByUser()`
  - Xác nhận hủy: `CancelConfirmFrm.actionPerformed()` -> `OrderDAO.checkOrderStatus()` -> `OrderDAO.cancelOrder()`

### 3. Quản lý giỏ hàng (Cart Management)
- **Flow**: `CartFrm`
- **Actions**:
  - Sửa số lượng / Xóa món: `CartDAO.updateQuantity()`, `CartDAO.removeCartItem()`

---

## Admin Usecases

### 4. Quản lý sản phẩm (Product Management)
- **Flow**: `AdminHomeFrm` -> `ManageProductFrm` -> `EditProductFrm`
- **Actions**:
  - Tìm kiếm / Hiển thị: `ManageProductFrm.loadAllProducts()` -> `ProductDAO.getAllProductsAdmin()` / `ProductDAO.searchProduct()`
  - Thêm / Sửa / Xóa: `ProductDAO.addProduct()`, `ProductDAO.updateProduct()`, `ProductDAO.deleteProduct()`

### 5. Quản lý danh mục (Category Management)
- **Flow**: `AdminHomeFrm` -> `ManageCategoryFrm` -> `AddCategoryFrm`
- **Actions**:
  - Quản lý danh sách: `ManageCategoryFrm.loadAllCategories()` -> `CategoryDAO.getAllCategories()`
  - Thêm / Sửa / Xóa: `CategoryDAO.addCategory()`, `CategoryDAO.updateCategory()`, `CategoryDAO.deleteCategory()`

### 6. Quản lý đơn hàng (Order Management)
- **Flow**: `AdminHomeFrm` -> `OrderManagementFrm` -> `OrderDetailFrm`
- **Actions**:
  - Tìm kiếm / Hiển thị: `OrderManagementFrm.loadAllOrders()` -> `OrderDAO.getAllOrders()` / `OrderDAO.searchOrders()`
  - Cập nhật trạng thái: `OrderDetailFrm.actionPerformed()` -> `OrderDAO.updateOrderStatus()`

### 7. Xem thống kê (Statistics)
- **Flow**: `AdminHomeFrm` -> `StatFrm`
- **Actions**:
  - Thống kê doanh thu theo món: `StatFrm.loadStatData()` -> `OrderDAO.getStatByProduct()`
