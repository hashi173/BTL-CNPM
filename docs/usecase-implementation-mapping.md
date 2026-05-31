# Usecase to Implementation Mapping

This document traces the use cases from `BTL-CNPM1.md` to their concrete JavaFX implementations.

## Client Usecases

### 1. Đặt đơn hàng (Order Placement)
- **Flow**: `LoginView` -> `HomeView` -> `MenuView` -> `ProductDetailView` -> `CartView` -> `CheckoutView` -> `OrderListView`
- **Actions**:
  - Xem danh sách món: `MenuView.loadProducts()` -> `ProductDAO.getAllProducts()`
  - Tùy chỉnh (Đường, Đá, Size): `ProductDetailView`
  - Giỏ hàng: `CartDAO.addCartItem()`, `CartDAO.getAllCart()`
  - Thanh toán: `CheckoutView` xử lý xác nhận -> `OrderDAO.createOrder()` -> `OrderItemDAO.addOrderItem()` -> `CartDAO.clearCart()`

### 2. Theo dõi đơn hàng (Order Tracking & Cancellation)
- **Flow**: `HomeView` -> `OrderListView` -> `ClientOrderDetailView` -> `CancelConfirmView`
- **Actions**:
  - Xem đơn hàng: `OrderListView.loadOrders()` -> `OrderDAO.getOrdersByUser()`
  - Xác nhận hủy: `CancelConfirmView` -> `OrderDAO.checkOrderStatus()` -> `OrderDAO.cancelOrder()`

### 3. Quản lý giỏ hàng (Cart Management)
- **Flow**: `CartView`
- **Actions**:
  - Sửa số lượng / Xóa món: `CartDAO.updateQuantity()`, `CartDAO.removeCartItem()`

---

## Admin Usecases

### 4. Quản lý sản phẩm (Product Management)
- **Flow**: `AdminHomeView` -> `ManageProductView` -> `EditProductView`
- **Actions**:
  - Tìm kiếm / Hiển thị: `ManageProductView.loadAllProducts()` -> `ProductDAO.getAllProductsAdmin()` / `ProductDAO.searchProduct()`
  - Thêm / Sửa / Xóa: `ProductDAO.addProduct()`, `ProductDAO.updateProduct()`, `ProductDAO.deleteProduct()`

### 5. Quản lý danh mục (Category Management)
- **Flow**: `AdminHomeView` -> `ManageCategoryView` -> `AddCategoryView`
- **Actions**:
  - Quản lý danh sách: `ManageCategoryView.loadAllCategories()` -> `CategoryDAO.getAllCategories()`
  - Thêm / Sửa / Xóa: `CategoryDAO.addCategory()`, `CategoryDAO.updateCategory()`, `CategoryDAO.deleteCategory()`

### 6. Quản lý đơn hàng (Order Management)
- **Flow**: `AdminHomeView` -> `OrderManagementView` -> `OrderDetailView`
- **Actions**:
  - Tìm kiếm / Hiển thị: `OrderManagementView.loadAllOrders()` -> `OrderDAO.getAllOrders()` / `OrderDAO.searchOrders()`
  - Cập nhật trạng thái: `OrderDetailView` xử lý đổi trạng thái -> `OrderDAO.updateOrderStatus()`

### 7. Xem thống kê (Statistics)
- **Flow**: `AdminHomeView` -> `StatView` -> `StatDetailView`
- **Actions**:
  - Thống kê doanh thu theo món: `StatView.loadStatData()` -> `OrderDAO.getStatByProduct()`
