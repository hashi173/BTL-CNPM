# ☕ Hướng dẫn cài đặt và chạy ứng dụng Quản lý Quán Cà Phê

Ứng dụng này được viết bằng Java (Swing) và sử dụng cơ sở dữ liệu PostgreSQL. Để ứng dụng hoạt động trên một máy tính mới, bạn cần cài đặt môi trường Java và thiết lập Database theo các bước dưới đây.

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
3. Khi được hỏi đặt mật khẩu cho tài khoản siêu quản trị `postgres`, bạn hãy thiết lập mật khẩu mặc định của ứng dụng (trong mã nguồn thiết lập là gì thì bạn nhập như thế, mặc định thường là `postgres` hoặc để rỗng tuỳ máy).

### Bước 3: Thiết lập Cơ sở dữ liệu (Database)
Để ứng dụng có dữ liệu hoạt động, bạn cần tạo Database và nạp dữ liệu mẫu:
1. Mở công cụ **pgAdmin 4** (được cài đặt kèm PostgreSQL) hoặc dùng dòng lệnh Terminal.
2. Tạo một Database mới với tên là: `coffeeshop`.
3. Tải 2 file `schema.sql` và `seed_data.sql` (kèm theo trong release này) về máy.
4. Mở Terminal / PowerShell tại thư mục chứa 2 file vừa tải và chạy lần lượt 2 lệnh sau để nạp dữ liệu (bạn sẽ được yêu cầu nhập mật khẩu database):
   ```bash
   # Nạp cấu trúc bảng
   psql -U postgres -d coffeeshop -f schema.sql
   
   # Nạp dữ liệu mẫu
   psql -U postgres -d coffeeshop -f seed_data.sql
   ```

---

## ☕ Chạy Ứng dụng

Sau khi đã hoàn tất cài đặt môi trường và Database, bạn có thể chạy ứng dụng rất dễ dàng!

1. Tải file thực thi `CoffeeShop.jar` về máy.
2. Mở cửa sổ Terminal (macOS) hoặc PowerShell (Windows) tại thư mục chứa file tải về.
3. Chạy lệnh:
   ```bash
   java -jar CoffeeShop.jar
   ```
4. Ứng dụng sẽ hiển thị lên màn hình. Bạn có thể sử dụng các tài khoản có sẵn trong hệ thống (từ file seed) hoặc đăng ký tài khoản mới để trải nghiệm!

### 💡 Lưu ý nhỏ
- Nếu bạn click đúp chuột trực tiếp vào file `CoffeeShop.jar`, ứng dụng vẫn có thể mở lên được (đối với Windows đã liên kết đuôi .jar với Java Runtime). Tuy nhiên, nếu gặp lỗi không mở được, hãy luôn ưu tiên dùng dòng lệnh `java -jar CoffeeShop.jar` để thấy rõ lỗi nếu có.
