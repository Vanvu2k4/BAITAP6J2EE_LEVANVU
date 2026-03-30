# 📚 HƯỚNG DẪN SỬ DỤNG - BAITAP TUẦN 5 & 6

## ✅ TÍNH NĂNG ĐÃ HOÀN THÀNH

### 📝 **Câu 1: Tìm kiếm sản phẩm theo tên (Keyword Search)**
- ✅ Nhập keyword từ form
- ✅ Hiển thị kết quả đúng
- **URL:** `GET /products?keyword=tên`
- **Ví dụ:** `/products?keyword=Iphone`

### 📄 **Câu 2: Phân trang (Pagination)**
- ✅ Mỗi trang hiển thị **5 sản phẩm**
- ✅ Nút **Next / Previous**
- **URL:** `GET /products?page=0`
- **Ví dụ:** `/products?page=1` (trang 2)

### ⬆️⬇️ **Câu 3: Sắp xếp (Sorting)**
- ✅ Theo giá **tăng dần** (price_asc)
- ✅ Theo giá **giảm dần** (price_desc)
- **URL:** `GET /products?sortBy=price_asc`
- **Ví dụ:**
  - `/products?sortBy=price_asc` (giá từ thấp → cao)
  - `/products?sortBy=price_desc` (giá từ cao → thấp)

### 🏷️ **Câu 4: Lọc theo Category**
- ✅ Dropdown chọn category
- ✅ Hiển thị sản phẩm theo category
- **URL:** `GET /products?categoryId=1`

### 🛒 **Câu 5: Thêm vào Giỏ hàng (Add to Cart)**
- ✅ Lưu vào **session**
- ✅ Có **quantity** (số lượng)
- **URL:** `GET /products/add-to-cart/{productId}`
- **Ví dụ:** `/products/add-to-cart/5?quantity=2`

### 🛍️ **Câu 6: Trang Giỏ hàng (Shopping Cart)**
- ✅ Hiển thị danh sách sản phẩm đã chọn
- ✅ Hiển thị: **tên, giá, số lượng, tổng tiền**
- ✅ Có nút: **Update quantity, Remove, Clear cart**
- **URL:** `GET /cart`

### ✔️ **Câu 7: Đặt hàng (Checkout)**
- ✅ Tạo **Order**
- ✅ Lưu **Order Detail**
- ✅ Tính **tổng tiền** (bao gồm thuế 10%)
- **URL:** `GET /orders/checkout`

---

## 🎯 ĐIỀU HƯỚNG (Navigation)

### **Trang danh sách sản phẩm**
```
/products
```

**Tính năng:**
- Form tìm kiếm, lọc, sắp xếp
- Danh sách sản phẩm
- Nút "Add to Cart" cho mỗi sản phẩm
- Phân trang (Next/Previous)

### **Trang giỏ hàng**
```
/cart
```

**Tính năng:**
- Danh sách sản phẩm trong giỏ
- Cập nhật số lượng
- Xóa sản phẩm
- Xóa toàn bộ giỏ
- Tính tổng tiền
- Nút "Proceed to Checkout"

### **Trang đặt hàng thành công**
```
/orders/checkout
```

**Tính năng:**
- Tạo đơn hàng từ giỏ hàng
- Hiển thị thông tin đơn hàng
- Xóa giỏ hàng sau khi đặt
- Redirect đến trang thành công

### **Trang quản lý đơn hàng**
```
/orders
```

**Tính năng:**
- Hiển thị danh sách tất cả đơn hàng
- Chi tiết đơn hàng (Order Details)
- Hủy đơn hàng (nếu chưa hoàn thành)

---

## 📊 CẤU TRÚC DATABASE

### **Bảng: products**
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

### **Bảng: categories**
```sql
CREATE TABLE categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);
```

### **Bảng: orders**
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

### **Bảng: order_details**
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

## 🔧 CẤU HÌNH APPLICATION

### **application.properties**
```properties
spring.application.name=baitap4
spring.datasource.url=jdbc:mysql://localhost:3306/csdl_bai5_qlsp
spring.datasource.username=root
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

---

## 🚀 CHẠY ỨNG DỤNG

### **Cách 1: Dùng Spring Boot**
```bash
./mvnw spring-boot:run
```

### **Cách 2: Dùng Maven Package**
```bash
./mvnw clean package -DskipTests
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### **URL mặc định**
```
http://localhost:8080
```

---

## 📁 CẤU TRÚC FILE

```
src/main/java/com/example/demo/
├── controller/
│   ├── ProductController.java      (Quản lý sản phẩm + giỏ hàng)
│   ├── CartController.java         (Quản lý giỏ hàng)
│   ├── OrderController.java        (Quản lý đơn hàng)
│   └── ...
├── service/
│   ├── ProductService.java         (Logic tìm kiếm, lọc, sắp xếp)
│   ├── OrderService.java           (Logic tạo đơn hàng)
│   └── ...
├── model/
│   ├── Product.java
│   ├── Order.java
│   ├── OrderDetail.java
│   └── ...
├── repository/
│   ├── ProductRepository.java
│   ├── OrderRepository.java
│   └── ...

src/main/resources/
├── templates/
│   ├── product/
│   │   ├── products.html    (Danh sách + tìm kiếm + lọc + sắp xếp)
│   │   ├── create.html
│   │   └── edit.html
│   ├── cart/
│   │   └── cart.html        (Giỏ hàng)
│   ├── order/
│   │   ├── checkout-success.html
│   │   ├── list.html        (Danh sách đơn hàng)
│   │   └── detail.html      (Chi tiết đơn hàng)
│   └── ...
```

---

## 📱 TEST CASE

### **Test 1: Tìm kiếm sản phẩm**
```
URL: /products?keyword=Iphone
Expected: Hiển thị tất cả sản phẩm có tên chứa "Iphone"
```

### **Test 2: Phân trang**
```
URL: /products?page=0
Expected: Hiển thị trang 1 (5 sản phẩm)

URL: /products?page=1
Expected: Hiển thị trang 2 (5 sản phẩm)
```

### **Test 3: Sắp xếp**
```
URL: /products?sortBy=price_asc
Expected: Sắp xếp từ giá thấp → cao

URL: /products?sortBy=price_desc
Expected: Sắp xếp từ giá cao → thấp
```

### **Test 4: Lọc theo category**
```
URL: /products?categoryId=1
Expected: Hiển thị sản phẩm của category có id=1
```

### **Test 5: Kết hợp tất cả**
```
URL: /products?keyword=phone&categoryId=1&sortBy=price_asc&page=0
Expected: 
- Tìm kiếm "phone"
- Lọc theo category 1
- Sắp xếp giá tăng dần
- Hiển thị trang 1
```

### **Test 6: Thêm vào giỏ hàng**
```
URL: /products/add-to-cart/5?quantity=2
Expected: 
- Thêm sản phẩm ID=5 với số lượng=2 vào session
- Redirect về /products
```

### **Test 7: Xem giỏ hàng**
```
URL: /cart
Expected:
- Hiển thị tất cả sản phẩm trong giỏ
- Hiển thị tổng tiền
- Có nút update, remove, clear
```

### **Test 8: Đặt hàng**
```
URL: /orders/checkout
Expected:
- Tạo Order mới
- Tạo OrderDetail cho mỗi sản phẩm
- Xóa giỏ hàng
- Redirect đến /orders/checkout-success
```

---

## 🎨 GIAO DIỆN BOOTSTRAP

Toàn bộ ứng dụng sử dụng **Bootstrap 5.3.3**:
```html
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
```

---

## ⚠️ LƯU Ý QUAN TRỌNG

1. **Session-based Cart:**
   - Giỏ hàng được lưu trong **HTTP Session**
   - Sẽ mất khi người dùng tắt trình duyệt hoặc hết timeout session

2. **Database:**
   - Đảm bảo MySQL đang chạy
   - Database `csdl_bai5_qlsp` phải tồn tại
   - Sử dụng `spring.jpa.hibernate.ddl-auto=update` để tự tạo bảng

3. **File Upload:**
   - Hình ảnh được lưu trong thư mục `/uploads`
   - Thư mục được tạo tự động

4. **Đơn vị tiền:**
   - Giá sản phẩm: **VND (Đồng Việt Nam)**
   - Hiển thị: `#numbers.formatDecimal(price, 0, 'COMMA', 0, 'POINT')`

---

## 📞 LIÊN HỆ HỖ TRỢ

Nếu gặp lỗi, kiểm tra:
1. ✅ MySQL đang chạy
2. ✅ Database tồn tại
3. ✅ application.properties cấu hình đúng
4. ✅ Các model có @Entity annotation
5. ✅ Repository extend JpaRepository

---

**Hoàn thành ngày: 2026-03-30** ✅

