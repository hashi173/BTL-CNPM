# Hướng Dẫn Triển Khai (Deployment Guide)

Tài liệu này cung cấp hướng dẫn chi tiết từ A-Z về cách cài đặt môi trường và chạy ứng dụng quản lý quán cà phê (Coffee Shop Management System - JavaFX) hoàn toàn bằng **Terminal / Command Line** cho cả macOS và Windows.

---

## 1. Cài Đặt Môi Trường (Install Prerequisites)

Bạn cần cài đặt Java (JDK 17) và PostgreSQL. **Lưu ý:** Vì dự án sử dụng giao diện **JavaFX** và **AtlantaFX**, nếu chạy bằng dòng lệnh (không dùng IDE như IntelliJ/Eclipse), bạn cần cấu hình JavaFX SDK hoặc tải đủ thư viện JavaFX vào thư mục `lib`.

### Trên macOS (Sử dụng Homebrew)
Mở ứng dụng **Terminal** và chạy các lệnh sau:
```bash
# Cài đặt Homebrew (nếu chưa có)
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Cài đặt Java 17
brew install openjdk@17
sudo ln -sfn /opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk

# Cài đặt PostgreSQL 14 (hoặc bản mới nhất)
brew install postgresql@14

# Khởi động service PostgreSQL
brew services start postgresql@14
```

### Trên Windows (Sử dụng Winget / PowerShell)
Mở **PowerShell dưới quyền Admin (Run as Administrator)** và chạy:
```powershell
# Cài đặt Java 17
winget install -e --id Oracle.Java.JDK.17

# Cài đặt PostgreSQL (Sẽ hiện cửa sổ cài đặt, hãy nhớ mật khẩu mặc định là 'postgres' hoặc tự đặt)
winget install -e --id PostgreSQL.PostgreSQL
```
*Lưu ý: Sau khi cài đặt Java trên Windows, bạn có thể cần khởi động lại Terminal để hệ thống nhận diện lệnh `java` và `javac`.*

---

## 2. Thiết Lập Cơ Sở Dữ Liệu (Database Setup)

Chúng ta sẽ tạo user `coffee_admin`, database `coffeeshop` và import dữ liệu mẫu.

### Trên macOS
Mở Terminal, di chuyển vào thư mục `coffeeshop` và chạy:
```bash
# Đăng nhập vào PostgreSQL (tài khoản mặc định của máy)
psql postgres

# Trong giao diện psql, chạy lần lượt các lệnh sau:
CREATE USER coffee_admin WITH PASSWORD '123';
CREATE DATABASE coffeeshop OWNER coffee_admin;
\q

# Import file schema.sql và seed_full.sql vào database vừa tạo
psql -d coffeeshop -f sql/schema.sql
psql -d coffeeshop -f sql/seed_full.sql
```

### Trên Windows
Mở PowerShell, di chuyển vào thư mục `coffeeshop` và chạy:
```powershell
# Đăng nhập vào PostgreSQL (nhập mật khẩu của user postgres mà bạn đã đặt lúc cài)
# Chú ý: Đảm bảo đường dẫn tới psql đã được thêm vào biến môi trường (Environment Variables)
psql -U postgres

# Trong giao diện psql, chạy lần lượt:
CREATE USER coffee_admin WITH PASSWORD '123';
CREATE DATABASE coffeeshop OWNER coffee_admin;
\q

# Import file schema.sql và seed_full.sql vào database (thêm cờ -U postgres nếu cần)
psql -U postgres -d coffeeshop -f sql\schema.sql
psql -U postgres -d coffeeshop -f sql\seed_full.sql
```

---

## 3. Biên Dịch Mã Nguồn (Compilation)

Để đơn giản hóa quá trình biên dịch, dự án đã cung cấp sẵn các script biên dịch tự động. Bạn chỉ cần di chuyển vào thư mục `coffeeshop` và chạy script tương ứng với hệ điều hành:

### Trên Windows
Mở **Command Prompt (cmd)** hoặc **PowerShell**, di chuyển vào thư mục `coffeeshop` và chạy:
```cmd
build.bat
```

### Trên macOS / Linux
Mở **Terminal**, di chuyển vào thư mục `coffeeshop` và chạy:
```bash
# Cấp quyền thực thi cho file script (chỉ cần chạy một lần duy nhất)
chmod +x build.sh run.sh

# Biên dịch mã nguồn
./build.sh
```

---

## 4. Khởi Chạy Ứng Dụng (Run)

Sau khi biên dịch thành công, để khởi chạy ứng dụng:

### Trên Windows
Vẫn ở trong thư mục `coffeeshop`, chạy file script khởi chạy:
```cmd
run.bat
```

### Trên macOS / Linux
Vẫn ở trong thư mục `coffeeshop`, chạy file script khởi chạy:
```bash
./run.sh
```

---

## 5. Gói Ứng Dụng Thành File JAR (Packaging as JAR)

Script biên dịch tự động (`build.bat` và `build.sh`) đã được cấu hình để **tự động đóng gói** ứng dụng thành file thực thi `CoffeeShop.jar` ngay trong thư mục `coffeeshop` sau khi biên dịch thành công.

Để chạy trực tiếp file JAR này:

### Cách 1: Chạy bằng dòng lệnh (Khuyên dùng)
Mở terminal/cmd tại thư mục `coffeeshop` và chạy lệnh sau:
- **Trên Windows**:
  ```cmd
  java --module-path "lib/javafx-sdk-17.0.12/lib" --add-modules javafx.controls -jar CoffeeShop.jar
  ```
- **Trên macOS / Linux**:
  ```bash
  java --module-path "lib/javafx-sdk-17.0.12/lib" --add-modules javafx.controls -jar CoffeeShop.jar
  ```

*(Lưu ý: Bạn không cần khai báo thêm các thư viện PostgreSQL hay AtlantaFX ở dòng lệnh vì file JAR đã được cấu hình tự động tìm chúng trong thư mục `lib` qua Manifest).*

### Cách 2: Double-click để chạy trực tiếp (Không cần dòng lệnh)
Để có thể kích đúp chuột (Double-click) chạy trực tiếp file `CoffeeShop.jar` mà không cần thông qua terminal, máy tính của bạn (hoặc giáo viên chấm bài) cần cài đặt **JDK đã tích hợp sẵn JavaFX** (ví dụ: **BellSoft Liberica JDK (Full)** hoặc **Azul Zulu JDK (Fx package)**). 
Nếu dùng các phiên bản JDK này, hệ thống sẽ tự động nhận diện JavaFX và chạy file JAR mượt mà bằng double-click.

---

## 6. Tài Khoản Đăng Nhập Mặc Định

Sử dụng các tài khoản sau để đăng nhập vào hệ thống (đã được tự động tạo qua file `schema.sql`):

| Vai Trò | Tên Đăng Nhập | Mật Khẩu |
| :--- | :--- | :--- |
| **Quản trị viên (Admin)** | `admin` | `123456` |
| **Khách hàng (Client)** | `client` | `123456` |

---

## Xử Lý Sự Cố (Troubleshooting)

1. **Lỗi `psql: command not found` (Windows)**:
   - Bạn cần thêm đường dẫn của thư mục `bin` trong PostgreSQL (ví dụ: `C:\Program Files\PostgreSQL\14\bin`) vào **Environment Variables (Path)** của Windows. Khởi động lại terminal sau khi thêm.
2. **Lỗi `ClassFormatError` hoặc `UnsupportedClassVersionError`**:
   - Máy bạn đang có nhiều phiên bản Java và bản mặc định thấp hơn Java 11. Chạy lệnh `java -version` và `javac -version` để kiểm tra.
3. **Lỗi `org.postgresql.util.PSQLException: FATAL: password authentication failed`**:
   - Dịch vụ PostgreSQL chưa chạy, hoặc bạn quên chưa tạo user `coffee_admin` với mật khẩu `123`.
4. **Lỗi hiển thị font tiếng Việt**:
   - Đảm bảo terminal của bạn sử dụng mã hóa UTF-8 và bạn không quên cờ `-encoding UTF-8` ở bước compile (`javac`).
