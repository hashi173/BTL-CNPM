-- Database: coffeeshop
SET client_encoding = 'UTF8';

DROP TABLE IF EXISTS cart_items CASCADE;
DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Table structure for Users
CREATE TABLE users (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    active BOOLEAN DEFAULT TRUE,
    email VARCHAR(100) UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    role VARCHAR(255) DEFAULT 'client',
    username VARCHAR(50) UNIQUE NOT NULL
);

-- Table structure for Categories
CREATE TABLE categories (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(255),
    name VARCHAR(50) NOT NULL
);

-- Table structure for Products
CREATE TABLE products (
    id UUID PRIMARY KEY,
    category_id UUID REFERENCES categories(id) ON DELETE SET NULL,
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    base_price NUMERIC(10, 2) NOT NULL,
    description TEXT,
    is_available BOOLEAN DEFAULT TRUE,
    name VARCHAR(100) NOT NULL,
    image_path VARCHAR(255) DEFAULT NULL
);

-- Table structure for Orders
CREATE TABLE orders (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    address_text TEXT,
    customer_name VARCHAR(255) NOT NULL,
    note TEXT,
    order_type VARCHAR(255),
    phone VARCHAR(255),
    status VARCHAR(255) DEFAULT 'PENDING',
    sub_total NUMERIC(12, 2) NOT NULL,
    total_amount FLOAT8 NOT NULL,
    tracking_code VARCHAR(255) UNIQUE
);

-- Table structure for Order Items
CREATE TABLE order_items (
    id SERIAL PRIMARY KEY,
    order_id UUID REFERENCES orders(id) ON DELETE CASCADE,
    product_id UUID REFERENCES products(id) ON DELETE SET NULL,
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    quantity INT4 NOT NULL,
    snapshot_options TEXT,
    snapshot_product_name VARCHAR(255) NOT NULL,
    snapshot_unit_price NUMERIC(12, 2) NOT NULL,
    sub_total NUMERIC(12, 2) NOT NULL
);

-- Table structure for Cart Items
CREATE TABLE cart_items (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    product_id UUID REFERENCES products(id) ON DELETE CASCADE,
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    options TEXT,
    quantity INT4 NOT NULL DEFAULT 1
);

-- Insert Default Admin & Test User
INSERT INTO users (id, username, password, full_name, email, phone, role) VALUES 
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'admin', '123456', 'System Admin', 'admin@cafe.com', '0123456789', 'ADMIN'),
('b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'client', '123456', 'Nguyễn Văn Khách', 'client@cafe.com', '0987654321', 'CLIENT');

-- Insert Dummy Categories
INSERT INTO categories (id, name, description) VALUES
('c1eebc99-9c0b-4ef8-bb6d-6bb9bd380c11', 'Cà phê truyền thống', 'Các loại cà phê pha phin truyền thống Việt Nam'),
('c2eebc99-9c0b-4ef8-bb6d-6bb9bd380c22', 'Cà phê máy', 'Cà phê pha máy Espresso, Cappuccino, Latte...'),
('c3eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'Trà & Nước trái cây', 'Các loại trà và nước trái cây giải nhiệt'),
('c4eebc99-9c0b-4ef8-bb6d-6bb9bd380c44', 'Sinh tố & Nước ép', 'Sinh tố và nước ép trái cây tươi');

-- Insert Dummy Products with image_path
INSERT INTO products (id, category_id, name, base_price, description, is_available, image_path) VALUES
-- Cà phê truyền thống
('f1eebc99-9c0b-4ef8-bb6d-6bb9bd380f11', 'c1eebc99-9c0b-4ef8-bb6d-6bb9bd380c11', 'Cà phê đen đá', 25000, 'Cà phê rang xay nguyên chất pha phin', TRUE, 'images/IcedCoffee.png'),
('f2eebc99-9c0b-4ef8-bb6d-6bb9bd380f22', 'c1eebc99-9c0b-4ef8-bb6d-6bb9bd380c11', 'Cà phê sữa đá', 29000, 'Cà phê phin kết hợp sữa đặc chuẩn vị truyền thống', TRUE, 'images/VietnameseCoffee.png'),
('f3eebc99-9c0b-4ef8-bb6d-6bb9bd380f33', 'c1eebc99-9c0b-4ef8-bb6d-6bb9bd380c11', 'Bạc xỉu', 32000, 'Bạc xỉu 3 tầng thơm ngon', TRUE, 'images/CoconutCoffee.png'),
-- Cà phê máy
('f4eebc99-9c0b-4ef8-bb6d-6bb9bd380f44', 'c2eebc99-9c0b-4ef8-bb6d-6bb9bd380c22', 'Espresso', 35000, 'Espresso nguyên chất đậm vị', TRUE, 'images/Espresso.png'),
('f5eebc99-9c0b-4ef8-bb6d-6bb9bd380f55', 'c2eebc99-9c0b-4ef8-bb6d-6bb9bd380c22', 'Cappuccino', 42000, 'Cappuccino bọt sữa mịn màng', TRUE, 'images/Cappuccino.png'),
('f5aeebc99-9c0b-4ef8-bb6d-6bb9bd380f5a', 'c2eebc99-9c0b-4ef8-bb6d-6bb9bd380c22', 'Latte Đá', 45000, 'Cà phê pha máy với sữa tươi thanh trùng', TRUE, 'images/CaffeLatte.png'),
('f5beebc99-9c0b-4ef8-bb6d-6bb9bd380f5b', 'c2eebc99-9c0b-4ef8-bb6d-6bb9bd380c22', 'Mocha', 48000, 'Cà phê Mocha kết hợp socola', TRUE, 'images/Mocha.png'),
('f5ceebc99-9c0b-4ef8-bb6d-6bb9bd380f5c', 'c2eebc99-9c0b-4ef8-bb6d-6bb9bd380c22', 'Americano', 38000, 'Americano đậm đà', TRUE, 'images/Americano.png'),
('f5deebc99-9c0b-4ef8-bb6d-6bb9bd380f5d', 'c2eebc99-9c0b-4ef8-bb6d-6bb9bd380c22', 'Flat White', 44000, 'Flat White sữa mịn', TRUE, 'images/FlatWhite.png'),
('f5eeebc99-9c0b-4ef8-bb6d-6bb9bd380f5e', 'c2eebc99-9c0b-4ef8-bb6d-6bb9bd380c22', 'Cold Brew', 40000, 'Cold Brew ủ lạnh 12 tiếng', TRUE, 'images/ColdBrew.png'),
('f5feebc99-9c0b-4ef8-bb6d-6bb9bd380f5f', 'c2eebc99-9c0b-4ef8-bb6d-6bb9bd380c22', 'Hazelnut Latte', 47000, 'Latte vị hạt phỉ', TRUE, 'images/HazelnutLatte.png'),
('f51eebc99-9c0b-4ef8-bb6d-6bb9bd380f51', 'c2eebc99-9c0b-4ef8-bb6d-6bb9bd380c22', 'Caramel Macchiato', 49000, 'Caramel Macchiato ngọt ngào', TRUE, 'images/CaramelMacchiato.png'),
('f52eebc99-9c0b-4ef8-bb6d-6bb9bd380f52', 'c2eebc99-9c0b-4ef8-bb6d-6bb9bd380c22', 'Affogato', 43000, 'Affogato espresso + kem', TRUE, 'images/Affogato.png'),
('f53eebc99-9c0b-4ef8-bb6d-6bb9bd380f53', 'c2eebc99-9c0b-4ef8-bb6d-6bb9bd380c22', 'Salted Caramel Coffee', 46000, 'Cà phê muối caramel', TRUE, 'images/SaltedCaramelCoffee.png'),
('f54eebc99-9c0b-4ef8-bb6d-6bb9bd380f54', 'c2eebc99-9c0b-4ef8-bb6d-6bb9bd380c22', 'Tiramisu', 52000, 'Cà phê vị Tiramisu', TRUE, 'images/Tiramisu.png'),
-- Trà & Nước trái cây
('f6eebc99-9c0b-4ef8-bb6d-6bb9bd380f66', 'c3eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'Trà đào cam sả', 39000, 'Trà đào thanh mát với cam và sả', TRUE, 'images/MangoTea.png'),
('f6aeebc99-9c0b-4ef8-bb6d-6bb9bd380f6a', 'c3eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'Trà vải nhiệt đới', 42000, 'Trà vải tươi mát lành', TRUE, 'images/PeachTea.png'),
('f6beebc99-9c0b-4ef8-bb6d-6bb9bd380f6b', 'c3eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'Trà chanh tươi', 28000, 'Trà chanh tươi mát lạnh', TRUE, 'images/LemonTea.png'),
('f6ceebc99-9c0b-4ef8-bb6d-6bb9bd380f6c', 'c3eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'Trà Matcha', 42000, 'Trà Matcha Nhật Bản', TRUE, 'images/MatchaLatte.png'),
('f6deebc99-9c0b-4ef8-bb6d-6bb9bd380f6d', 'c3eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'Trà Thái', 35000, 'Trà Thái đỏ đậm đà', TRUE, 'images/ThaiTea.png'),
('f6eeebc99-9c0b-4ef8-bb6d-6bb9bd380f6e', 'c3eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'Trà hoa cúc', 30000, 'Trà hoa cúc thảo mộc', TRUE, 'images/ChamomileTea.png'),
('f6feebc99-9c0b-4ef8-bb6d-6bb9bd380f6f', 'c3eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'Trà nhài', 28000, 'Trà nhài thơm ngát', TRUE, 'images/JasmineTea.png'),
('f61eebc99-9c0b-4ef8-bb6d-6bb9bd380f61', 'c3eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'Trà Ô long', 32000, 'Trà Ô long thượng hạng', TRUE, 'images/OolongTea.png'),
('f62eebc99-9c0b-4ef8-bb6d-6bb9bd380f62', 'c3eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'Trà Atisô', 30000, 'Trà Atisô tốt cho sức khỏe', TRUE, 'images/HoneyGingerTea.png'),
('f63eebc99-9c0b-4ef8-bb6d-6bb9bd380f63', 'c3eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'Trà Mãng cầu', 38000, 'Trà mãng cầu nhiệt đới', TRUE, 'images/PassionFruitTea.png'),
('f64eebc99-9c0b-4ef8-bb6d-6bb9bd380f64', 'c3eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'Trà Sakura', 40000, 'Trà hoa anh đào Nhật Bản', TRUE, 'images/SakuraBlossomTea.png'),
('f65eebc99-9c0b-4ef8-bb6d-6bb9bd380f65', 'c3eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'Trà Matcha Espresso', 48000, 'Matcha kết hợp Espresso', TRUE, 'images/MatchaEspresso.png'),
('f66eebc99-9c0b-4ef8-bb6d-6bb9bd380f66', 'c3eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'Trà dâu tây', 36000, 'Trà dâu tây tươi', TRUE, 'images/StrawberryTea.png'),
('f67eebc99-9c0b-4ef8-bb6d-6bb9bd380f67', 'c3eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'Trà xoài', 34000, 'Trà xoài nhiệt đới', TRUE, 'images/LycheeTea.png'),
('f68eebc99-9c0b-4ef8-bb6d-6bb9bd380f68', 'c3eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'Trà bưởi', 33000, 'Trà bưởi thanh mát', TRUE, 'images/PeachTea.png'),
('f69eebc99-9c0b-4ef8-bb6d-6bb9bd380f69', 'c3eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'Soda Việt quất', 35000, 'Soda việt quất sủi bọt', TRUE, 'images/LimeSoda.png'),
-- Sinh tố & Nước ép
('f7eebc99-9c0b-4ef8-bb6d-6bb9bd380f77', 'c4eebc99-9c0b-4ef8-bb6d-6bb9bd380c44', 'Sinh tố bơ', 38000, 'Sinh tố bơ béo ngậy', TRUE, 'images/AvocadoSmoothie.png'),
('f7aeebc99-9c0b-4ef8-bb6d-6bb9bd380f7a', 'c4eebc99-9c0b-4ef8-bb6d-6bb9bd380c44', 'Sinh tố chuối', 35000, 'Sinh tố chuối bổ dưỡng', TRUE, 'images/BananaSmoothie.png'),
('f7beebc99-9c0b-4ef8-bb6d-6bb9bd380f7b', 'c4eebc99-9c0b-4ef8-bb6d-6bb9bd380c44', 'Sinh tố dâu', 37000, 'Sinh tố dâu tây tươi', TRUE, 'images/StrawberrySmoothie.png'),
('f7ceebc99-9c0b-4ef8-bb6d-6bb9bd380f7c', 'c4eebc99-9c0b-4ef8-bb6d-6bb9bd380c44', 'Sinh tố xoài', 36000, 'Sinh tố xoài tropical', TRUE, 'images/MangoSmoothie.png'),
('f7deebc99-9c0b-4ef8-bb6d-6bb9bd380f7d', 'c4eebc99-9c0b-4ef8-bb6d-6bb9bd380c44', 'Nước ép táo', 32000, 'Nước ép táo tươi nguyên chất', TRUE, 'images/AppleJuice.png'),
('f7eeebc99-9c0b-4ef8-bb6d-6bb9bd380f7e', 'c4eebc99-9c0b-4ef8-bb6d-6bb9bd380c44', 'Nước ép cam', 30000, 'Nước ép cam vắt tươi', TRUE, 'images/OrangeJuice.png'),
('f7feebc99-9c0b-4ef8-bb6d-6bb9bd380f7f', 'c4eebc99-9c0b-4ef8-bb6d-6bb9bd380c44', 'Nước ép dưa hấu', 28000, 'Nước ép dưa hấu giải khát', TRUE, 'images/WatermelonJuice.png'),
('f71eebc99-9c0b-4ef8-bb6d-6bb9bd380f71', 'c4eebc99-9c0b-4ef8-bb6d-6bb9bd380c44', 'Nước ép cà rốt', 30000, 'Nước ép cà rốt tốt cho mắt', TRUE, 'images/CarrotJuice.png'),
('f72eebc99-9c0b-4ef8-bb6d-6bb9bd380f72', 'c4eebc99-9c0b-4ef8-bb6d-6bb9bd380c44', 'Sinh tố dừa', 38000, 'Sinh tố dừa béo thơm', TRUE, 'images/CoconutJuice.png'),
('f73eebc99-9c0b-4ef8-bb6d-6bb9bd380f73', 'c4eebc99-9c0b-4ef8-bb6d-6bb9bd380c44', 'Sinh tố berry', 40000, 'Sinh tố hỗn hợp berry', TRUE, 'images/BlueberrySmoothie.png'),
('f74eebc99-9c0b-4ef8-bb6d-6bb9bd380f74', 'c4eebc99-9c0b-4ef8-bb6d-6bb9bd380c44', 'Sinh tố mầm đậu', 35000, 'Sinh tố mầm đậu bổ dưỡng', TRUE, 'images/PeanutSmoothie.png'),
('f75eebc99-9c0b-4ef8-bb6d-6bb9bd380f75', 'c4eebc99-9c0b-4ef8-bb6d-6bb9bd380c44', 'Nước mía', 25000, 'Nước mía tươi nguyên chất', TRUE, 'images/SugarcaneJuice.png'),
('f76eebc99-9c0b-4ef8-bb6d-6bb9bd380f76', 'c4eebc99-9c0b-4ef8-bb6d-6bb9bd380c44', 'Sinh tố thập cẩm', 42000, 'Sinh tố mix trái cây nhiệt đới', TRUE, 'images/MixedBerrySmoothie.png'),
('f77eebc99-9c0b-4ef8-bb6d-6bb9bd380f78', 'c4eebc99-9c0b-4ef8-bb6d-6bb9bd380c44', 'Sinh tố dứa', 34000, 'Sinh tố dứa chua ngọt', TRUE, 'images/PineappleJuice.png'),
('f78eebc99-9c0b-4ef8-bb6d-6bb9bd380f79', 'c4eebc99-9c0b-4ef8-bb6d-6bb9bd380c44', 'Trà sữa hoàng hôn', 45000, 'Trà sữa vị taro', TRUE, 'images/TaroMilkTea.png');

-- Cấp quyền cho user coffee_admin
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO coffee_admin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO coffee_admin;
