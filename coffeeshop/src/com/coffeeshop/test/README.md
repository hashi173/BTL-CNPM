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
2. **Cập nhật thư viện:**
   - Trong thư mục `lib/` của project, nhóm đã để sẵn 2 file: `junit-4.13.2.jar` và `hamcrest-core-1.3.jar`.
   - **IntelliJ:** Bấm chuột phải vào 2 file này chọn `Add as Library...`.
   - **Eclipse:** Chuột phải vào project -> Build Path -> Configure Build Path -> Libraries -> Add JARs -> trỏ tới 2 file này.
   - **VS Code:** IDE tự động nhận diện nếu bạn mở đúng folder `coffeeshop` hoặc có thể cấu hình ở mục `Java: Dependencies`.
3. Sau khi hết báo đỏ, mở một trong các file Test (Ví dụ: `OrderDAOTest.java`).
4. Nhấn chuột phải vào vùng code hoặc nhấn vào biểu tượng 🟢 Nút Play màu xanh lá ở bên trái (cạnh tên class hoặc tên hàm).
5. Chọn **Run 'OrderDAOTest'**.
6. Giao diện Test Runner của IDE sẽ hiện ra ở phía dưới. 
   - **Tích xanh (✔)**: Test thành công (Code chuẩn).
   - **Gạch đỏ (✘)**: Test thất bại (Kết quả không như kỳ vọng).

## Cách đọc kết quả Test (Dành cho người mới)
Đặc thù của kiểm thử tự động (Unit Test) là nguyên tắc **"No news is good news"** (Không báo gì nghĩa là code chạy hoàn hảo).
- **Kỳ vọng khi Test Pass (Thành công):** Bạn sẽ **không** thấy các dòng chữ báo lỗi đỏ lòm, màn hình Debug Console thường sẽ trống trơn hoặc lướt qua rất nhanh các mã hệ thống. Thay vào đó, bạn hãy nhìn tìm các **dấu tích xanh (✔)** xuất hiện bên cạnh tên hàm.
- **Để xem log chi tiết (những dòng chữ System.out.println):**
  - **Trên VS Code:** Đừng nhìn ở tab *Debug Console* hay *Terminal*. Hãy chuyển sang tab **Test Results** ở dưới cùng màn hình. Hoặc mở tab **Testing** (icon hình bình thí nghiệm 🧪) bên lề trái -> Click đúp vào tên bài test đã pass để xem chữ in ra.
  - **Trên IntelliJ / Eclipse:** Cửa sổ *Run/Test* sẽ hiện ra ngay bên dưới, liệt kê danh sách các test có dấu tích xanh, click vào từng test để xem log console ở bên phải.

## Cách 2: Chạy bằng dòng lệnh (VS Code / Terminal)
Nếu chạy bằng dòng lệnh Java thuần (ít dùng), bạn cần compile các file test này cùng với thư viện `junit.jar` và `hamcrest-core.jar` rồi dùng `org.junit.runner.JUnitCore` để thực thi.
Do cấu trúc project hiện tại không dùng Maven/Gradle, khuyến khích các bạn dùng **Cách 1** để IDE tự động cấu hình classpath nhanh nhất.

> **Lưu ý quan trọng trước khi chạy Test:**
> - Một số Test yêu cầu có dữ liệu sẵn trong Database (như mã `CS-0001` hay một số UUID cụ thể). Hãy chắc chắn rằng bạn đã chạy file `seed_full.sql` vào cơ sở dữ liệu `coffeeshop` trước khi tiến hành test để đảm bảo không bị lỗi dữ liệu `null`.
