# ☕ Hệ Thống Quản Lý Quán Cà Phê (Coffee Shop Management System)

Chào mừng bạn đến với **Coffee Shop Management System** — ứng dụng quản lý quán cà phê hiện đại, trực quan được xây dựng hoàn toàn bằng **JavaFX**, giao diện cao cấp **AtlantaFX (Primer Light)** và cơ sở dữ liệu **PostgreSQL**. 

Dự án này được phát triển, tối ưu hóa và làm sạch triệt để cho môn học **Phân Tích Thiết Kế Hệ Thống & Công Nghệ Phần Mềm (CNPM)**.

---

## 🎨 Điểm Nổi Bật & Giao Diện (Features)

*   **Kiến trúc MVC + DAO thuần khiết**: Chia tách rõ ràng giữa giao diện hiển thị (`View`), mô hình dữ liệu (`Model`), nghiệp vụ điều hướng (`SceneManager`) và tầng truy xuất cơ sở dữ liệu độc lập (`DAO`).
*   **Giao diện AtlantaFX Đẳng Cấp**: Trải nghiệm UI/UX mượt mà, chuyên nghiệp với phong cách thiết kế hiện đại, tinh gọn, thay thế hoàn toàn giao diện Swing cũ kỹ.
*   **Chức năng cho Khách hàng (Client)**:
    *   Xem danh mục món ăn uống trực quan, tìm kiếm nhanh và lọc sản phẩm.
    *   Giỏ hàng thông minh, tăng giảm số lượng sản phẩm nhanh chóng.
    *   Quy trình thanh toán (Checkout) và theo dõi trạng thái đơn hàng thời gian thực.
    *   **Gợi ý thông minh (Smart Recommendation)**: Tự động gợi ý các món bán chạy nhất cho khách hàng mới đăng ký.
*   **Chức năng cho Quản trị viên (Admin)**:
    *   Trang chủ thống kê trực quan với danh sách sản phẩm bán chạy nhất kèm hình ảnh.
    *   Quản lý danh mục & Quản lý sản phẩm (thêm/sửa/xóa/khóa) có **xem trước ảnh trực tiếp (Live Image Preview)**.
    *   Quản lý đơn hàng toàn diện: xem chi tiết, duyệt đơn, giao hàng, hoàn thành, hoặc hủy đơn.
    *   **Thống kê & Biểu đồ doanh thu**: Xem doanh số theo từng món ăn hoặc doanh thu chi tiết theo tháng dưới dạng biểu đồ trực quan.

---

## ⚙️ Yêu Cầu Hệ Thống (System Requirements)
- **Hệ điều hành**: Windows, macOS, hoặc Linux.
- **Java**: JDK 17 trở lên.
- **Cơ sở dữ liệu**: PostgreSQL 14 trở lên.

---

## 🚀 Khởi Chạy Nhanh (Quick Start)

Dự án đã tích hợp sẵn bộ thư viện đầy đủ và các script tự động hóa, giúp bạn biên dịch và chạy phần mềm chỉ trong vài giây!

Di chuyển vào thư mục `coffeeshop` bằng Terminal/CMD của bạn và chạy:

### Trên Windows
*   **Biên dịch & Đóng gói**: Chạy file `build.bat`
*   **Khởi chạy ứng dụng**: Chạy file `run.bat`

### Trên macOS / Linux
*   **Cấp quyền thực thi**: `chmod +x build.sh run.sh`
*   **Biên dịch & Đóng gói**: `./build.sh`
*   **Khởi chạy ứng dụng**: `./run.sh`

---

## 💾 Cấu Hình Cơ Sở Dữ Liệu (Database Setup)

1.  Mở công cụ quản lý PostgreSQL (pgAdmin 4 hoặc Command Line).
2.  Tạo cơ sở dữ liệu mới tên là: `coffeeshop`.
3.  Tạo tài khoản quản trị PostgreSQL (nếu chưa có):
    *   **Username**: `coffee_admin`
    *   **Password**: `123`
    *   *Gán quyền sở hữu (Owner) db `coffeeshop` cho tài khoản này.*
4.  Nạp cấu trúc bảng và dữ liệu mẫu bằng cách chạy 2 lệnh sau từ terminal tại thư mục dự án:
    ```bash
    # Nạp cấu trúc bảng
    psql -U postgres -d coffeeshop -f coffeeshop/sql/schema.sql

    # Nạp dữ liệu mẫu hoàn chỉnh
    psql -U postgres -d coffeeshop -f coffeeshop/sql/seed_full.sql
    ```

---

## 🔑 Tài Khoản Đăng Nhập Mặc Định

Bạn có thể sử dụng các tài khoản có sẵn trong cơ sở dữ liệu mẫu để trải nghiệm hệ thống ngay lập tức:

| Vai Trò (Role) | Tên Đăng Nhập | Mật Khẩu |
| :--- | :--- | :--- |
| **Quản trị viên (Admin)** | `admin` | `123456` |
| **Khách hàng (Client)** | `client` | `123456` |

---

## 📂 Tài Liệu Tham Khảo Chi Tiết (Documentation)

Hệ thống tài liệu hướng dẫn chuẩn chỉ dành cho nhóm học tập và báo cáo giảng viên được lưu trữ đầy đủ trong thư mục `docs/`:

*   📄 **[Tài Liệu Hướng Dẫn & Phân Công Use Case](file:///d:/Project/BTL-CNPM/docs/cnpm-assignment-guide.md)**: Giải thích kiến trúc dự án dễ hiểu cho mọi thành viên và phân chia Use Case cụ thể cho 5 thành viên (Bách, Quỳnh, Long, Thi, Hà).
*   📄 **[Tài Liệu Triển Khai Chi Tiết (Deployment Guide)](file:///d:/Project/BTL-CNPM/docs/deployment.md)**: Hướng dẫn từ A-Z cách thiết lập môi trường, biên dịch thủ công hoặc đóng gói file thực thi `CoffeeShop.jar`.
*   📄 **[Bản Đồ Ánh Xạ Kiến Trúc (Architecture Mapping)](file:///d:/Project/BTL-CNPM/docs/cnpm-architecture-mapping.md)**: Ánh xạ cấu trúc mã nguồn thực tế với biểu đồ kiến trúc hệ thống.
*   📄 **[Ánh Xạ Use Case (Usecase Mapping)](file:///d:/Project/BTL-CNPM/docs/usecase-implementation-mapping.md)**: Mô tả luồng xử lý và các thành phần tham gia trực tiếp của từng Use Case trong mã nguồn.
