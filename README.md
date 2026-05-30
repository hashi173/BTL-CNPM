# Quản Lý Quán Cà Phê (Coffee Shop Management)

Đây là hệ thống quản lý quán cà phê được xây dựng bằng **Java Swing** và **PostgreSQL** tuân theo kiến trúc **MVC + DAO**. 
Hệ thống được phát triển dựa trên tài liệu phân tích thiết kế `BTL-CNPM1.md`.

## Yêu cầu hệ thống
- **Java**: JDK 11 trở lên (khuyến nghị JDK 17).
- **Cơ sở dữ liệu**: PostgreSQL.
- **Thư viện JDBC**: `postgresql-42.x.x.jar`.

## Hướng dẫn cài đặt & chạy ứng dụng

### 1. Khởi tạo Cơ sở dữ liệu
1. Mở PostgreSQL (pgAdmin hoặc psql).
2. Tạo database mới tên là `coffeeshop`.
3. Tạo user PostgreSQL có username `cafe_admin` và password `123`, gán quyền truy cập vào db `coffeeshop`.
4. Mở script `coffeeshop/sql/schema.sql` và chạy toàn bộ lệnh SQL trong đó để khởi tạo bảng và chèn dữ liệu mẫu.

### 2. Biên dịch & Chạy
Dùng Command Line hoặc IDE (IntelliJ / Eclipse):
- **Class Main**: `com.coffeeshop.Main`
- Đảm bảo bạn đã thêm file `.jar` của PostgreSQL vào `classpath` khi biên dịch/chạy.

### 3. Tài khoản Đăng nhập (Mặc định)
Hệ thống đã chèn sẵn 2 tài khoản:
- **Admin**:
  - Username: `admin`
  - Password: `123456`
- **Khách hàng (Client)**:
  - Username: `client`
  - Password: `123456`

## Tài liệu tham khảo
Vui lòng tham khảo thư mục `docs/` để xem chi tiết kiến trúc và luồng xử lý:
- `cnpm-architecture-mapping.md`: Kiến trúc tổng quan.
- `usecase-implementation-mapping.md`: Luồng chi tiết từng chức năng.
