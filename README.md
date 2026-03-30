# 🎓 BAITAP TUẦN 5 & 6 - QUẢN LÝ SẢN PHẨM VÀ GIỎ HÀNG

**Phiên bản:** 1.0.0  
**Ngôn ngữ:** Java 17  
**Framework:** Spring Boot 4.0.3  
**Database:** MySQL 8.0  

---

## 📋 MỤC LỤC

1. [Tính năng](#-tính-năng)
2. [Yêu cầu hệ thống](#-yêu-cầu-hệ-thống)
3. [Cài đặt](#-cài-đặt)
4. [Chạy ứng dụng](#-chạy-ứng-dụng)
5. [Cấu trúc dự án](#-cấu-trúc-dự-án)
6. [API Documentation](#-api-documentation)
7. [Troubleshooting](#-troubleshooting)

---

## ✨ TÍNH NĂNG

### **7 Tính năng chính:**

| # | Tính năng | Mô tả | Status |
|:---:|:---|:---|:---:|
| 1️⃣ | 🔍 Tìm kiếm | Tìm sản phẩm theo tên (keyword) | ✅ |
| 2️⃣ | 📄 Phân trang | Hiển thị 5 sản phẩm/trang + Previous/Next | ✅ |
| 3️⃣ | ⬆️⬇️ Sắp xếp | Theo giá tăng dần / giảm dần | ✅ |
| 4️⃣ | 🏷️ Lọc | Lọc sản phẩm theo category | ✅ |
| 5️⃣ | 🛒 Giỏ hàng | Thêm sản phẩm vào giỏ (session-based) | ✅ |
| 6️⃣ | 🛍️ Trang giỏ | Xem, cập nhật, xóa sản phẩm trong giỏ | ✅ |
| 7️⃣ | ✔️ Đặt hàng | Tạo Order & OrderDetail, tính tổng tiền | ✅ |

---

## 💻 YÊU CẦU HỆ THỐNG

### **Software:**
- ✅ **Java 17** hoặc cao hơn
- ✅ **MySQL 8.0** hoặc cao hơn
- ✅ **Maven 3.8+** (hoặc dùng Maven Wrapper)
- ✅ **Spring Boot 4.0.3**

### **Hardware:**
- ✅ RAM: 2GB tối thiểu
- ✅ Disk: 500MB trống

---

## 🚀 CÀI ĐẶT

### **1. Clone / Download dự án**
```bash
cd D:\VanVu\BAITAP6J2EE_LEVANVU
```

### **2. Cấu hình MySQL**

**Tạo database:**
```sql
CREATE DATABASE csdl_bai5_qlsp;
USE csdl_bai5_qlsp;
```

**Cập nhật `application.properties`:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/csdl_bai5_qlsp
spring.datasource.username=root
spring.datasource.password=  # Để trống nếu MySQL không có password

spring.jpa.hibernate.ddl-auto=update  # Tự tạo bảng
```

### **3. Build dự án**
```bash
.\mvnw clean package -DskipTests
```

---

## ▶️ CHẠY ỨNG DỤNG

### **Cách 1: Spring Boot Maven Plugin**
```bash
.\mvnw spring-boot:run
```

### **Cách 2: JAR File**
```bash
# Build JAR
.\mvnw clean package -DskipTests

# Chạy JAR
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### **Cách 3: IDE (IntelliJ IDEA)**
- Nhấp chuột phải vào `Baitap4Application.java`
- Chọn `Run 'Baitap4Application'`

### **URL truy cập:**
```
http://localhost:8080
```

**Các URL chính:**
- 📦 Sản phẩm: `http://localhost:8080/products`
- 🛒 Giỏ hàng: `http://localhost:8080/cart`
- 📋 Đơn hàng: `http://localhost:8080/orders`

---

## 📁 CẤU TRÚC DỰ ÁN

```
BAITAP6J2EE_LEVANVU/
│
├── src/main/java/com/example/demo/
│   ├── Baitap4Application.java           (Entry point)
│   │
│   ├── controller/
│   │   ├── ProductController.java        (✨ NEW: Tìm kiếm + Phân trang + Lọc + Sắp xếp + Add to Cart)
│   │   ├── CartController.java           (✨ NEW: Quản lý giỏ hàng)
│   │   ├── OrderController.java          (✨ NEW: Quản lý đơn hàng)
│   │   ├── HomeController.java
│   │   ├── ErrorPageController.java
│   │
│   ├── service/
│   │   ├── ProductService.java           (✨ UPDATED: +8 methods mới)
│   │   ├── OrderService.java             (✨ NEW: Tạo đơn hàng)
│   │   ├── AccountService.java           (✨ UPDATED: Constructor Injection)
│   │   ├── CategoryService.java
│   │
│   ├── repository/
│   │   ├── ProductRepository.java        (✨ UPDATED: +3 methods)
│   │   ├── OrderRepository.java          (✨ NEW)
│   │   ├── OrderDetailRepository.java    (✨ NEW)
│   │   ├── AccountRepository.java        (✨ UPDATED: +1 method)
│   │   ├── CategoryRepository.java
│   │
│   ├── model/
│   │   ├── Product.java
│   │   ├── Category.java
│   │   ├── Order.java                    (✨ NEW)
│   │   ├── OrderDetail.java              (✨ NEW)
│   │   ├── Account.java                  (✨ UPDATED: +2 fields)
│   │   ├── Role.java
│   │
│   ├── config/
│   │   ├── SecurityConfig.java
│   │   ├── WebConfig.java
│
├── src/main/resources/
│   ├── application.properties
│   │
│   ├── templates/
│   │   ├── product/
│   │   │   ├── products.html            (✨ UPDATED: Form + Pagination)
│   │   │   ├── create.html
│   │   │   ├── edit.html
│   │   │
│   │   ├── cart/
│   │   │   └── cart.html                (✨ NEW)
│   │   │
│   │   ├── order/
│   │   │   ├── checkout-success.html    (✨ NEW)
│   │   │   ├── list.html                (✨ NEW)
│   │   │   ├── detail.html              (✨ NEW)
│   │   │
│   │   ├── error/
│   │   │   └── 403.html
│
│
├── uploads/                              (📁 Thư mục upload ảnh)
│   └── (images...)
│
├── pom.xml                               (Maven Dependencies)
├── mvnw / mvnw.cmd                       (Maven Wrapper)
│
├── 📄 README.md                          (File này)
├── 📄 HUONG_DAN_SU_DUNG.md              (✨ Hướng dẫn chi tiết)
├── 📄 LỖI_ĐÃ_FIX.md                     (✨ Danh sách lỗi đã fix)
├── 📄 API_ENDPOINTS.md                   (✨ API Documentation)

```

---

## 🔗 API DOCUMENTATION

### **Xem chi tiết:** Mở file `API_ENDPOINTS.md`

**Quick Reference:**

| Endpoint | Method | Mô tả |
|:---|:---:|:---|
| `/products` | GET | Danh sách sản phẩm |
| `/products?keyword=...` | GET | Tìm kiếm |
| `/products?sortBy=price_asc` | GET | Sắp xếp |
| `/products?categoryId=1` | GET | Lọc category |
| `/products/add-to-cart/{id}` | GET | Thêm vào giỏ |
| `/cart` | GET | Xem giỏ hàng |
| `/cart/update-quantity/{id}` | POST | Cập nhật số lượng |
| `/cart/remove/{id}` | GET | Xóa sản phẩm |
| `/orders/checkout` | GET | Đặt hàng |
| `/orders` | GET | Danh sách đơn hàng |
| `/orders/{id}` | GET | Chi tiết đơn hàng |

---

## 🧪 TEST CASES

### **Test 1: Tìm kiếm**
```
URL: http://localhost:8080/products?keyword=Iphone
Expected: Hiển thị tất cả sản phẩm có tên chứa "Iphone"
```

### **Test 2: Phân trang**
```
URL: http://localhost:8080/products?page=0
Expected: Trang 1 (5 sản phẩm)

URL: http://localhost:8080/products?page=1
Expected: Trang 2 (5 sản phẩm)
```

### **Test 3: Sắp xếp**
```
URL: http://localhost:8080/products?sortBy=price_asc
Expected: Sắp xếp từ giá thấp → cao

URL: http://localhost:8080/products?sortBy=price_desc
Expected: Sắp xếp từ giá cao → thấp
```

### **Test 4: Lọc**
```
URL: http://localhost:8080/products?categoryId=1
Expected: Hiển thị sản phẩm của category 1
```

### **Test 5: Kết hợp (Advanced)**
```
URL: http://localhost:8080/products?keyword=phone&categoryId=1&sortBy=price_asc&page=0
Expected:
- Tìm "phone"
- Trong category 1
- Giá tăng dần
- Trang 1
```

### **Test 6: Thêm giỏ hàng**
```
URL: http://localhost:8080/products/add-to-cart/5?quantity=2
Expected:
- Thêm product 5, qty 2 vào session
- Redirect về /products
```

### **Test 7: Xem giỏ**
```
URL: http://localhost:8080/cart
Expected:
- Danh sách sản phẩm
- Tính toán tổng tiền
```

### **Test 8: Checkout**
```
URL: http://localhost:8080/orders/checkout
Expected:
- Tạo Order
- Tạo OrderDetails
- Xóa giỏ hàng
- Redirect /orders/checkout-success
```

---

## 📊 DATABASE SCHEMA

### **Table: products**
```sql
CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price BIGINT NOT NULL,
    image VARCHAR(200),
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);
```

### **Table: categories**
```sql
CREATE TABLE categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);
```

### **Table: orders** (✨ NEW)
```sql
CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    account_id INT,
    order_date DATETIME NOT NULL,
    total_price BIGINT NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    FOREIGN KEY (account_id) REFERENCES account(id)
);
```

### **Table: order_details** (✨ NEW)
```sql
CREATE TABLE order_details (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    product_id INT,
    quantity INT NOT NULL,
    price BIGINT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
```

---

## ⚙️ CẤU HÌNH QUAN TRỌNG

### **application.properties**
```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/csdl_bai5_qlsp
spring.datasource.username=root
spring.datasource.password=

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### **ProductService Constants**
```java
private static final int ITEMS_PER_PAGE = 5;  // Số item mỗi trang
```

---

## 🆘 TROUBLESHOOTING

### **❌ Lỗi: "Connection refused"**
```
Nguyên nhân: MySQL không chạy
Cách fix: Khởi động MySQL service
  - Windows: services.msc → MySQL80 → Start
  - Linux: sudo service mysql start
```

### **❌ Lỗi: "Unknown database 'csdl_bai5_qlsp'"**
```
Nguyên nhân: Database không tồn tại
Cách fix: Tạo database trong MySQL
  CREATE DATABASE csdl_bai5_qlsp;
```

### **❌ Lỗi: "Field injection is not recommended"**
```
Nguyên nhân: Dùng @Autowired trên biến
Cách fix: ✅ Đã fix sử dụng Constructor Injection
```

### **❌ Lỗi: "Class 'ProductService' is not abstract and does not override"**
```
Nguyên nhân: File bị trùng lặp method
Cách fix: ✅ Đã fix loại bỏ trùng lặp
```

### **❌ Lỗi: "Reached end of file while parsing"**
```
Nguyên nhân: Thiếu closing brace
Cách fix: ✅ Đã fix thêm } ở cuối file
```

### **❌ Lỗi: "Cannot resolve symbol 'ITEMS_PER_PAGE'"**
```
Nguyên nhân: Constant nằm ngoài class
Cách fix: ✅ Đã fix move vào class
```

---

## 📝 LỚP HỌC & NGƯỜI TẠO

**Lớp:** J2EE  
**Tuần:** 5 & 6  
**Bài tập:** Quản lý sản phẩm & Giỏ hàng  
**Người làm:** (Your Name)  
**Ngày hoàn thành:** 2026-03-30

---

## 📚 TÀI LIỆU THAM KHẢO

1. **Hướng dẫn chi tiết:** `HUONG_DAN_SU_DUNG.md`
2. **Lỗi đã fix:** `LỖI_ĐÃ_FIX.md`
3. **API Endpoints:** `API_ENDPOINTS.md`
4. **Spring Boot Docs:** https://spring.io/projects/spring-boot
5. **Thymeleaf:** https://www.thymeleaf.org/
6. **Bootstrap 5:** https://getbootstrap.com/

---

## 📞 HỖ TỢ

Nếu gặp vấn đề:
1. ✅ Kiểm tra MySQL đang chạy
2. ✅ Kiểm tra database tồn tại
3. ✅ Đọc file `HUONG_DAN_SU_DUNG.md`
4. ✅ Kiểm tra `LỖI_ĐÃ_FIX.md`
5. ✅ Xem logs trong console

---

## ✅ CHECKLIST HOÀN THÀNH

- ✅ Tìm kiếm sản phẩm (Keyword)
- ✅ Phân trang (5 sản phẩm/trang)
- ✅ Sắp xếp theo giá
- ✅ Lọc theo category
- ✅ Thêm vào giỏ hàng
- ✅ Trang giỏ hàng
- ✅ Đặt hàng (Checkout)
- ✅ Tất cả lỗi compile đã fix
- ✅ Build & Package thành công
- ✅ Documentation hoàn chỉnh

---

## 📄 LICENSE

Educational Project - For Learning Purposes Only

---

**🎉 Happy Coding!**

```
Build Status: ✅ SUCCESS
Compilation: ✅ PASSED
Tests: ✅ READY
Deployment: ✅ READY
Documentation: ✅ COMPLETE
```



