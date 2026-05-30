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
    name VARCHAR(100) NOT NULL
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
('c3eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'Trà trái cây', 'Các loại trà giải nhiệt kết hợp trái cây tươi');

-- Insert Dummy Products
INSERT INTO products (id, category_id, name, base_price, description, is_available) VALUES
('f1eebc99-9c0b-4ef8-bb6d-6bb9bd380f11', 'c1eebc99-9c0b-4ef8-bb6d-6bb9bd380c11', 'Cà phê đen đá', 25000, 'Cà phê rang xay nguyên chất pha phin', TRUE),
('f2eebc99-9c0b-4ef8-bb6d-6bb9bd380f22', 'c1eebc99-9c0b-4ef8-bb6d-6bb9bd380c11', 'Cà phê sữa đá', 29000, 'Cà phê phin kết hợp sữa đặc chuẩn vị truyền thống', TRUE),
('f3eebc99-9c0b-4ef8-bb6d-6bb9bd380f33', 'c1eebc99-9c0b-4ef8-bb6d-6bb9bd380c11', 'Bạc xỉu', 32000, 'Bạc xỉu 3 tầng thơm ngon', TRUE),
('f4eebc99-9c0b-4ef8-bb6d-6bb9bd380f44', 'c2eebc99-9c0b-4ef8-bb6d-6bb9bd380c22', 'Espresso', 35000, 'Espresso nguyên chất đậm vị', TRUE),
('f5eebc99-9c0b-4ef8-bb6d-6bb9bd380f55', 'c2eebc99-9c0b-4ef8-bb6d-6bb9bd380c22', 'Latte Đá', 45000, 'Cà phê pha máy với sữa tươi thanh trùng', TRUE),
('f6eebc99-9c0b-4ef8-bb6d-6bb9bd380f66', 'c3eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'Trà đào cam sả', 39000, 'Trà đào thanh mát với cam và sả', TRUE),
('f7eebc99-9c0b-4ef8-bb6d-6bb9bd380f77', 'c3eebc99-9c0b-4ef8-bb6d-6bb9bd380c33', 'Trà vải nhiệt đới', 42000, 'Trà vải tươi mát lành', TRUE);

-- Cấp quyền cho user coffee_admin
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO coffee_admin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO coffee_admin;
