# Hệ Thống Quản Lý Quán Cà Phê (Coffee Shop Management System)

Ứng dụng quản lý quán cà phê được xây dựng bằng JavaFX (giao diện AtlantaFX) và PostgreSQL.
Dự án phục vụ môn học Phân Tích Thiết Kế Hệ Thống & Công Nghệ Phần Mềm.

---

## Tính Năng Nổi Bật

- Kiến trúc MVC + DAO.
- Chức năng Khách hàng: Xem danh mục, giỏ hàng, thanh toán, theo dõi đơn hàng, gợi ý sản phẩm.
- Chức năng Quản trị viên: Thống kê doanh thu, quản lý danh mục, sản phẩm, và đơn hàng.

---

## Yêu Cầu Hệ Thống

- Hệ điều hành: Windows, macOS, hoặc Linux.
- Java: JDK 17+.
- Cơ sở dữ liệu: PostgreSQL 14+.

---

## Hướng Dẫn Chạy

Di chuyển vào thư mục `coffeeshop` và chạy lệnh sau:

- Windows: Chạy file `build.bat`, sau đó chạy `run.bat`
- macOS/Linux: Chạy `./build.sh`, sau đó chạy `./run.sh`

---

## Cấu Hình CSDL

1. Tạo CSDL tên `coffeeshop`.
2. Tạo tài khoản PostgreSQL: Username `coffee_admin`, Password `123`. Gán quyền Owner CSDL `coffeeshop` cho tài khoản này.
3. Chạy các lệnh nạp dữ liệu:
   ```bash
   psql -U postgres -d coffeeshop -f coffeeshop/sql/schema.sql
   psql -U postgres -d coffeeshop -f coffeeshop/sql/seed_full.sql
   ```

## Tài Khoản Đăng Nhập

- Admin: `admin` / `123456`
- Khách hàng: `client` / `123456`

---

## Tài Liệu Tham Khảo

Các tài liệu chi tiết nằm trong thư mục `docs/`:
- [Tài Liệu Hướng Dẫn & Phân Công Use Case](docs/cnpm-assignment-guide.md): Giải thích kiến trúc dự án dễ hiểu cho mọi thành viên và phân chia Use Case cụ thể cho 5 thành viên (Bách, Quỳnh, Long, Thi, Hà).
- [Tài Liệu Triển Khai Chi Tiết (Deployment Guide)](docs/deployment.md): Hướng dẫn từ A-Z cách thiết lập môi trường, biên dịch thủ công hoặc đóng gói file thực thi `CoffeeShop.jar`.
- [Bản Đồ Ánh Xạ Kiến Trúc (Architecture Mapping)](docs/cnpm-architecture-mapping.md): Ánh xạ cấu trúc mã nguồn thực tế với biểu đồ kiến trúc hệ thống.
- [Ánh Xạ Use Case (Usecase Mapping)](docs/usecase-implementation-mapping.md): Mô tả luồng xử lý và các thành phần tham gia trực tiếp của từng Use Case trong mã nguồn.
