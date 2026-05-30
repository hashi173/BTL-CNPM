# Hướng Dẫn Triển Khai (Deployment Guide)

Tài liệu này cung cấp hướng dẫn chi tiết từ A-Z về cách cài đặt môi trường và chạy ứng dụng quản lý quán cà phê (Coffee Shop Management System - Java Swing) hoàn toàn bằng **Terminal / Command Line** cho cả macOS và Windows.

---

## 1. Cài Đặt Môi Trường (Install Prerequisites)

Bạn cần cài đặt Java (JDK 17) và PostgreSQL.

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

Chúng ta sẽ tạo user `cafe_admin`, database `coffeeshop` và import dữ liệu mẫu.

### Trên macOS
Mở Terminal, di chuyển vào thư mục `coffeeshop` và chạy:
```bash
# Đăng nhập vào PostgreSQL (tài khoản mặc định của máy)
psql postgres

# Trong giao diện psql, chạy lần lượt các lệnh sau:
CREATE USER coffee_admin WITH PASSWORD '123';
CREATE DATABASE coffeeshop OWNER coffee_admin;
\q

# Import file schema.sql vào database vừa tạo
psql -d coffeeshop -f sql/schema.sql
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

# Import file schema.sql vào database (thêm cờ -U postgres nếu cần)
psql -U postgres -d coffeeshop -f sql\schema.sql
```

---

## 3. Biên Dịch Mã Nguồn (Compilation)

Ứng dụng cần được biên dịch từ file `.java` sang file `.class` và lưu vào thư mục `bin`. 
Di chuyển vào thư mục `coffeeshop`:

### Trên macOS
```bash
cd coffeeshop
mkdir -p bin
javac -encoding UTF-8 -d bin $(find src -name "*.java")
```

### Trên Windows (PowerShell)
```powershell
cd coffeeshop
mkdir -Force bin
javac -encoding UTF-8 -d bin (Get-ChildItem -Path src -Recurse -Filter *.java | Select-Object -ExpandProperty FullName)
```

---

## 4. Khởi Chạy Ứng Dụng (Run)

Sau khi biên dịch thành công, vẫn ở trong thư mục `coffeeshop`, chạy lệnh sau để khởi động phần mềm:

### Trên macOS
```bash
java -cp "bin:lib/postgresql-42.7.5.jar" com.coffeeshop.Main
```

### Trên Windows (PowerShell)
```powershell
java -cp "bin;lib/postgresql-42.7.5.jar" com.coffeeshop.Main
```

---

## 5. Tài Khoản Đăng Nhập Mặc Định

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
