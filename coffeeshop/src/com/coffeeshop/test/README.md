# Hướng dẫn chạy Test (JUnit)

Thư mục này chứa 5 file test tương ứng với 5 phân hệ do 5 thành viên trong nhóm phụ trách:
1. `StatsDAOTest.java` (Bách - Thống kê)
2. `ProductAndCategoryDAOTest.java` (Quỳnh - Sản phẩm & Danh mục)
3. `ClientOrderDAOTest.java` (Long - Theo dõi đơn hàng)
4. `CartAndOrderPlacementDAOTest.java` (Thi - Giỏ hàng & Đặt hàng)
5. `OrderDAOTest.java` (Hà - Quản lý đơn hàng)

## Cách 1: Chạy trực tiếp trên IDE (IntelliJ IDEA / Eclipse / NetBeans)
Đây là cách đơn giản và trực quan nhất.
1. Mở Project trên IDE.
2. Đảm bảo thư viện **JUnit 4** đã được add vào Project (Thường IDE sẽ gợi ý ấn `Alt + Enter` trên chữ `@Test` để tải thư viện về).
3. Mở một trong các file Test (Ví dụ: `OrderDAOTest.java`).
4. Nhấn chuột phải vào vùng code hoặc nhấn vào biểu tượng 🟢 Nút Play màu xanh lá ở bên trái (cạnh tên class hoặc tên hàm).
5. Chọn **Run 'OrderDAOTest'**.
6. Giao diện Test Runner của IDE sẽ hiện ra ở phía dưới. 
   - **Tích xanh (✔)**: Test thành công (Code chuẩn).
   - **Gạch đỏ (✘)**: Test thất bại (Kết quả không như kỳ vọng).

## Cách 2: Chạy bằng dòng lệnh (VS Code / Terminal)
Nếu chạy bằng dòng lệnh Java thuần (ít dùng), bạn cần compile các file test này cùng với thư viện `junit.jar` và `hamcrest-core.jar` rồi dùng `org.junit.runner.JUnitCore` để thực thi.
Do cấu trúc project hiện tại không dùng Maven/Gradle, khuyến khích các bạn dùng **Cách 1** để IDE tự động cấu hình classpath nhanh nhất.

> **Lưu ý quan trọng trước khi chạy Test:**
> - Một số Test yêu cầu có dữ liệu sẵn trong Database (như mã `CS-0001` hay một số UUID cụ thể). Hãy chắc chắn rằng bạn đã chạy file `seed_full.sql` vào cơ sở dữ liệu `coffeeshop` trước khi tiến hành test để đảm bảo không bị lỗi dữ liệu `null`.
