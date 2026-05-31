# Tài Liệu Hướng Dẫn & Phân Công CNPM (Coffee Shop Project)

Tài liệu này được viết theo chuẩn Công nghệ Phần mềm (CNPM) sử dụng ngôn ngữ tự nhiên, dễ hiểu nhằm giúp toàn bộ các thành viên trong nhóm (kể cả những người không trực tiếp code) nắm rõ được cấu trúc, nghiệp vụ và luồng xử lý của hệ thống.

---

## 1. Phần của Bách

### Bảng Thuật Ngữ (Glossary)
Để hiểu rõ hệ thống, nhóm chúng ta thống nhất một số thuật ngữ cơ bản sau:
*   **Hệ thống / Ứng dụng**: Phần mềm quản lý quán cà phê mà nhóm đang xây dựng.
*   **User (Người dùng)**: Bao gồm cả Khách hàng (Client) và Quản trị viên (Admin).
*   **Database (DB - Cơ sở dữ liệu)**: Nơi lưu trữ toàn bộ thông tin của hệ thống (danh sách món, tài khoản, đơn hàng). Dự án dùng PostgreSQL.
*   **UI (Giao diện người dùng) / View**: Các màn hình mà người dùng nhìn thấy và thao tác (nút bấm, bảng biểu). Dự án dùng công nghệ **JavaFX**.
*   **Entity (Thực thể)**: Các đối tượng trong code đại diện cho dữ liệu thực tế (Ví dụ: Thực thể `Products` đại diện cho 1 ly cà phê).
*   **DAO (Data Access Object)**: Các file code chuyên làm nhiệm vụ "nói chuyện" với Cơ sở dữ liệu. Ví dụ: `ProductDAO` sẽ làm nhiệm vụ lấy danh sách món từ DB lên, hoặc thêm món mới vào DB.
*   **Use case (Ca sử dụng)**: Một chức năng cụ thể mà người dùng có thể thực hiện trên hệ thống (Ví dụ: Đặt hàng, Hủy đơn).

### Use Case: Xem Thống Kê
*   **Tác nhân**: Admin (Quản lý).
*   **Mục đích**: Xem doanh thu của quán theo từng tháng hoặc theo từng món đồ uống để biết món nào bán chạy.
*   **Luồng xử lý (Bằng lời)**:
    1. Quản lý đăng nhập và chọn mục **Thống kê** trên thanh menu bên trái.
    2. Hệ thống (`StatView`) yêu cầu `OrderDAO` lấy dữ liệu tổng doanh thu và số lượng đơn hàng từ DB.
    3. `OrderDAO` tính toán số liệu và trả về cho giao diện.
    4. Giao diện hiển thị các con số tổng quát và vẽ biểu đồ Cột/Tròn (Chart) trực quan.
    5. Quản lý có thể ấn vào từng món để xem chi tiết doanh thu cụ thể của món đó (`StatDetailView`).

---

## 2. Phần của Quỳnh

### Sơ Đồ Use Case Tổng Quan (Hướng dẫn vẽ)
*Hệ thống được chia làm 2 cụm Use case chính cho 2 đối tượng:*
*   **Khách hàng (Client)**: Đăng nhập/Đăng ký, Xem thực đơn, Thêm vào giỏ hàng, Đặt hàng, Xem lịch sử đơn hàng, Hủy đơn.
*   **Quản trị viên (Admin)**: Đăng nhập, Quản lý danh mục (Thêm/Sửa/Xóa), Quản lý sản phẩm (Thêm/Sửa/Xóa), Quản lý đơn hàng (Duyệt/Giao/Hủy), Xem thống kê doanh thu.

### Use Case: Quản Lý Sản Phẩm
*   **Tác nhân**: Admin.
*   **Mục đích**: Thêm đồ uống mới, sửa giá, hoặc ngừng bán một món.
*   **Luồng xử lý (Bằng lời)**:
    1. Admin vào màn hình **Quản lý Sản phẩm**. Hệ thống tải toàn bộ danh sách đồ uống hiển thị lên bảng.
    2. **Thêm mới**: Admin bấm "Thêm sản phẩm", điền tên, giá, chọn hình ảnh. Hệ thống (`ProductDAO`) lưu thông tin xuống Database.
    3. **Chỉnh sửa**: Admin chọn một món trên bảng, bấm "Sửa". Giao diện (`EditProductView`) hiện ra thông tin cũ, Admin sửa lại giá và bấm "Lưu". Database cập nhật giá mới.
    4. **Xóa**: Admin chọn món và bấm "Xóa". Hệ thống cảnh báo xác nhận, nếu đồng ý, món đó sẽ bị xóa khỏi Database (hoặc ẩn đi).

### Use Case: Quản Lý Danh Mục
*   **Tác nhân**: Admin.
*   **Mục đích**: Nhóm các đồ uống lại với nhau (Ví dụ: Cà phê, Trà, Sinh tố).
*   **Luồng xử lý**: Tương tự Quản lý sản phẩm, Admin thao tác trên bảng Danh mục. Giao diện (`ManageCategoryView`) tương tác với `CategoryDAO` để thêm/sửa/xóa các nhóm đồ uống.

---

## 3. Phần của Long

### Mô Tả Hệ Thống (Bằng ngôn ngữ tự nhiên)
Phần mềm Quản lý Quán Cà phê là một ứng dụng Desktop (chạy trên máy tính) giúp số hóa quy trình bán hàng. 
*   Về phía khách hàng, họ có thể tạo tài khoản, lướt xem menu các loại đồ uống với hình ảnh bắt mắt, chọn size/đá/đường và đặt hàng trực tuyến. Hệ thống giúp khách hàng theo dõi xem đơn hàng của mình đang ở trạng thái nào (Chờ xác nhận, Đang giao, v.v.).
*   Về phía quản lý quán, phần mềm cung cấp một bảng điều khiển (Dashboard) trực quan để thêm bớt các món trong menu, tiếp nhận đơn đặt hàng của khách để tiến hành pha chế, và cuối tháng có thể xem biểu đồ thống kê doanh thu một cách chính xác mà không cần tính sổ tay.

### Use Case: Theo Dõi Đơn Hàng
*   **Tác nhân**: Khách hàng.
*   **Mục đích**: Kiểm tra xem ly cà phê mình đặt đã được làm chưa, hay đang được giao tới đâu.
*   **Luồng xử lý**:
    1. Khách hàng bấm vào mục **Lịch sử đơn hàng** trên thanh điều hướng.
    2. Hệ thống (`OrderListView`) gọi xuống Database để lấy danh sách các đơn mà khách này đã đặt.
    3. Trạng thái của đơn (Mới đặt, Đã duyệt, Đang giao) được hiển thị bằng các nhãn màu khác nhau (Badge) để khách dễ nhìn.

---

## 4. Phần của Thi

### Trích Các Lớp Thực Thể (Entities)
Thực thể là các đối tượng cốt lõi chứa dữ liệu. Trong hệ thống có các thực thể chính sau:
1.  **Users (Người dùng)**: Chứa thông tin tài khoản, mật khẩu, tên, số điện thoại, và vai trò (là Admin hay Khách).
2.  **Categories (Danh mục)**: Chứa tên nhóm đồ uống (VD: Cà phê máy).
3.  **Products (Sản phẩm)**: Chứa tên đồ uống, giá tiền, mô tả, hình ảnh và thuộc nhóm danh mục nào.
4.  **CartItems (Giỏ hàng)**: Chứa thông tin tạm thời món đồ khách chọn trước khi thanh toán.
5.  **Orders (Đơn hàng)**: Chứa thông tin hóa đơn tổng (Tên người nhận, địa chỉ, tổng tiền, trạng thái đơn).
6.  **OrderItems (Chi tiết đơn hàng)**: Nằm bên trong Đơn hàng, ghi chi tiết đơn này mua mấy ly đen đá, mấy ly trà đào.

### Use Case: Đặt Hàng
*   **Tác nhân**: Khách hàng.
*   **Mục đích**: Hoàn tất việc mua các món trong giỏ.
*   **Luồng xử lý**:
    1. Khách hàng vào màn hình **Thanh toán** (`CheckoutView`).
    2. Điền thông tin giao hàng (Tên, Số điện thoại, Địa chỉ).
    3. Bấm "Xác nhận đặt hàng". Hệ thống (`OrderDAO`) sẽ tạo một hóa đơn mới lưu vào DB, đồng thời chuyển toàn bộ đồ trong Giỏ hàng sang thành Chi tiết đơn hàng, và cuối cùng làm trống Giỏ hàng. Sinh ra một mã Tracking Code (Ví dụ: ORD-12345).

### Use Case: Quản Lý Giỏ Hàng
*   **Tác nhân**: Khách hàng.
*   **Mục đích**: Thêm, sửa, xóa đồ uống trước khi chốt đơn.
*   **Luồng xử lý**: Khách hàng ở màn hình `CartView`, có thể bấm nút `+` hoặc `-` để tăng giảm số lượng ly. Hệ thống gọi `CartDAO` để cập nhật lại DB và tính lại tổng tiền ngay lập tức.

---

## 5. Phần của Hà

### Use Case: Quản Lý Đơn Hàng (Admin)
*   **Tác nhân**: Admin (Nhân viên quán / Quản lý).
*   **Mục đích**: Tiếp nhận đơn khách đặt, điều phối pha chế và giao hàng.
*   **Luồng xử lý (Bằng lời)**:
    1. **Xem danh sách**: Admin vào mục **Quản lý đơn hàng**. Màn hình (`OrderManagementView`) hiển thị danh sách tất cả đơn hàng từ mới nhất đến cũ nhất.
    2. **Xem chi tiết**: Admin bấm "Xem chi tiết" một đơn để biết khách gọi những món gì, đá/đường ra sao (`OrderDetailView`).
    3. **Cập nhật trạng thái (Confirm / Ship / Complete)**:
        *   Khi thấy đơn mới, Admin bấm **Xác nhận (Confirm)**. Hệ thống đổi trạng thái đơn thành "Đã xác nhận".
        *   Khi pha chế xong, đưa cho shipper, Admin bấm **Giao hàng (Ship)**. Trạng thái đổi thành "Đang giao".
        *   Khi shipper báo đã thu tiền, Admin bấm **Hoàn thành (Complete)**.
    4. **Hủy đơn (Cancel)**: Nếu khách ghi sai địa chỉ hoặc quán hết nguyên liệu, Admin có thể bấm nút **Hủy đơn**. Trạng thái hóa đơn chuyển thành "Đã hủy" màu đỏ. Tất cả thao tác này đều được `OrderDAO` lưu lịch sử xuống Database.
