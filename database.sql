CREATE DATABASE shopapp;
GO;
USE shopapp;
GO;
--Khách hàng khi muốn mua hàng -> phải đăng ký tài khoản -> bảng users
--Field password dùng để lưu mật khẩu đã mã hóa
--TINYINT(1) chỉ lấy 1 chữ số từ 0-9
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(10) NOT NULL,
    address VARCHAR(200) DEFAULT '',
    password VARCHAR(100) NOT NULL DEFAULT '',
    created_at DATETIME,
    updated_at DATETIME,
    is_active TINYINT(1) DEFAULT 1,
    date_of_birth DATE,
    facebook_account_id INT DEFAULT 0,
    google_account_id INT DEFAULT 0
);

--user là phải có vai trò -> bảng roles

CREATE TABLE roles (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

--Thêm property (column) role_id vào bảng users và liên kết khóa ngoại với bảng roles

ALTER TABLE users ADD COLUMN role_id INT;
ALTER TABLE users ADD FOREIGN KEY (role_id) REFERENCES roles(id); 

--Khi khách hàng đăng nhập -> được cấp token để không cần nhập lại mk -> bảng tokens
--expiration_date là ngày hết hạn của token
--user_id là foreign key (trường khóa ngoại) liên kết đến id của bảng users

CREATE TABLE tokens (
    id INT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(255) UNIQUE NOT NULL,
    token_type VARCHAR(50) NOT NULL,
    expiration_date DATETIME,
    revoked TINYINT(1) NOT NULL,
    expired TINYINT(1) NOT NULL,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

--Hỗ trợ đăng nhập từ FB và GG -> bảng social_accounts
--`email` cũng là email không khác nhau

CREATE TABLE social_accounts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    `provider` VARCHAR(20) NOT NULL COMMENT 'Tên nhà social network',
    provider_id VARCHAR(50) NOT NULL,
    email VARCHAR(150) NOT NULL COMMENT 'Email tài khoản',
    name VARCHAR(100) NOT NULL COMMENT 'Tên người dùng',
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

--Bảng danh mục sản phẩm -> bảng categories

CREATE TABLE categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL DEFAULT '' COMMENT 'Tên danh mục, vd: đồ điện tử'
);

--Bảng sản phẩm -> bảng products
--thumbnail là đường dẫn đến ảnh
CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(350) COMMENT 'Tên sản phẩm',
    price FLOAT NOT NULL CHECK (price >= 0),
    thumbnail VARCHAR(300) DEFAULT '',
    description LONGTEXT DEFAULT '',
    created_at DATETIME,
    updated_at DATETIME,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES categories (id)
);

--Bảng ảnh sản phẩm -> bảng product_images
--Trường thumbnail trên bảng product để lưu ảnh đại diện, tạo bảng product_images lưu nhiều ảnh của product

CREATE TABLE product_images (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT fk_product_images_product_id
        FOREIGN KEY (product_id)
        REFERENCES products (id) ON DELETE CASCADE,
    image_url VARCHAR(300)
);

--Đặt hàng -> bảng orders

CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    fullname VARCHAR(100) DEFAULT '',
    email VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(20) NOT NULL,
    address VARCHAR(200) NOT NULL,
    note VARCHAR(100) DEFAULT '',
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20),
    total_money FLOAT CHECK(total_money >= 0)
);

--Thêm một số thuộc tính hay còn gọi là cột cho bảng orders
ALTER TABLE orders ADD COLUMN `shipping_method` VARCHAR(100);
ALTER TABLE orders ADD COLUMN `shipping_address` VARCHAR(200);
ALTER TABLE orders ADD COLUMN `shipping_date` DATE;
ALTER TABLE orders ADD COLUMN `tracking_number` VARCHAR(100);
ALTER TABLE orders ADD COLUMN `payment_method` VARCHAR(100);

--Xóa đơn hàng, xóa mềm -> thêm trường active

ALTER TABLE orders ADD COLUMN active TINYINT(1);

--Sửa trường active thành trường is_active

ALTER TABLE orders CHANGE active is_active TINYINT(1);

--Trạng thái đơn hàng chỉ được phép nhận một số giá trị cụ thể

ALTER TABLE orders 
MODIFY COLUMN status ENUM('pending', 'processing','shipped', 'delivered', 'cancelled', 'completed')
COMMENT 'Trạng thái đơn hàng';

--Bảng orders có nhiều bảng order_details (Chi tiết đơn hàng) -> bảng order_details

CREATE TABLE order_details (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    FOREIGN KEY (order_id) REFERENCES orders (id),
    product_id INT,
    FOREIGN KEY (product_id) REFERENCES products (id),
    price FLOAT CHECK(price >= 0),
    number_of_products INT CHECK(number_of_products > 0),
    total_money FLOAT CHECK(total_money >= 0),
    color VARCHAR(20) DEFAULT ''
);