# ☕ Hướng dẫn cài đặt và chạy ứng dụng Quản lý Quán Cà Phê

Ứng dụng này được viết bằng Java (JavaFX + AtlantaFX) và sử dụng cơ sở dữ liệu PostgreSQL. Để ứng dụng hoạt động trên một máy tính mới, bạn cần cài đặt môi trường Java và thiết lập Database theo các bước dưới đây.

## 📋 Yêu cầu hệ thống
- **Hệ điều hành:** Windows, macOS, hoặc Linux.
- **Java:** JDK 17 (hoặc mới hơn).
- **Cơ sở dữ liệu:** PostgreSQL 14 (hoặc mới hơn).

---

## 🚀 Hướng dẫn Cài đặt

### Bước 1: Cài đặt Java (Nếu chưa có)
1. Tải và cài đặt Java (JDK 17+) từ [trang chủ Oracle](https://www.oracle.com/java/technologies/downloads/) hoặc [Adoptium](https://adoptium.net/).
2. Kiểm tra xem máy đã nhận Java chưa bằng cách mở Terminal (macOS) hoặc Command Prompt / PowerShell (Windows) và gõ:
   ```bash
   java -version
   ```
   *(Nếu hiện ra phiên bản Java tức là bạn đã cài thành công).*

### Bước 2: Cài đặt PostgreSQL
1. Tải và cài đặt PostgreSQL từ [trang chủ PostgreSQL](https://www.postgresql.org/download/).
2. Trong quá trình cài đặt, hãy giữ nguyên các thông số mặc định (Port: `5432`). 
3. Thiết lập mật khẩu cho tài khoản siêu quản trị `postgres` là `123` (hoặc cấu hình tùy ý, nhưng đảm bảo khớp với cài đặt database).

### Bước 3: Thiết lập Cơ sở dữ liệu (Database)
Để ứng dụng có dữ liệu hoạt động, bạn cần tạo Database và nạp dữ liệu mẫu:
1. Mở công cụ **pgAdmin 4** (được cài đặt kèm PostgreSQL) hoặc dùng dòng lệnh Terminal.
2. Tạo một Database mới với tên là: `coffeeshop`.
3. Tìm 2 file `schema.sql` và `seed_full.sql` nằm trong thư mục `sql/` (hoặc trong gói release tải về) trên máy.
4. Mở Terminal / PowerShell tại thư mục đó và chạy lần lượt 2 lệnh sau để nạp dữ liệu (bạn sẽ được yêu cầu nhập mật khẩu database):
   ```bash
   # Nạp cấu trúc bảng
   psql -U postgres -d coffeeshop -f sql/schema.sql
   
   # Nạp dữ liệu mẫu
   psql -U postgres -d coffeeshop -f sql/seed_full.sql
   ```

---

## ☕ Chạy Ứng Dụng

Sau khi đã hoàn tất cài đặt môi trường và Database, bạn có thể chạy ứng dụng theo một trong các cách sau:

### Cách 1: Chạy bằng dòng lệnh (Khuyên dùng và cực kỳ ổn định)
1. Mở Terminal (macOS) hoặc Command Prompt/PowerShell (Windows) tại thư mục chứa file `CoffeeShop.jar`.
2. Chạy câu lệnh tương ứng sau:
   - **Trên Windows / macOS / Linux**:
     ```bash
     java --module-path "lib/javafx-sdk-17.0.12/lib" --add-modules javafx.controls -jar CoffeeShop.jar
     ```

*(Lưu ý: Bạn không cần khai báo thêm các thư viện PostgreSQL hay AtlantaFX ở dòng lệnh vì file JAR đã được cấu hình tự động tìm chúng trong thư mục `lib` qua Manifest).*

### Cách 2: Kích đúp chuột (Double-click) để chạy trực tiếp (Không cần dòng lệnh)
Để có thể kích đúp chuột chạy trực tiếp file `CoffeeShop.jar` mà không cần thông qua Terminal:
* Máy tính của bạn (hoặc giáo viên chấm điểm) cần sử dụng phiên bản **JDK tích hợp sẵn JavaFX** (ví dụ: **[BellSoft Liberica JDK (bản Full)](https://bell-sw.com/pages/downloads/)** hoặc **[Azul Zulu JDK (bản FX package)](https://www.azul.com/downloads/?package=jdk-fx)**).
* Nếu dùng các JDK này, hệ thống sẽ tự động nhận diện JavaFX đồ họa và chạy trực tiếp file JAR mượt mà bằng double-click.
* **Lưu ý**: Nếu click đúp chuột mà không thấy giao diện hiện ra, điều đó có nghĩa là JDK mặc định của máy chưa hỗ trợ JavaFX trực tiếp. Hãy chuyển sang dùng **Cách 1** (chạy bằng dòng lệnh) để chương trình hoạt động bình thường và hiển thị đầy đủ log lỗi.

