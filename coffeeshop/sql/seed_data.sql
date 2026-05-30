-- Seed Data cho Thong Ke
-- Sinh du lieu cho don hang va chi tiet don hang

-- Delete existing seed data to avoid primary key conflicts
DELETE FROM order_items WHERE order_id IN ('d1eebc99-9c0b-4ef8-bb6d-6bb9bd380d11', 'd2eebc99-9c0b-4ef8-bb6d-6bb9bd380d22', 'd3eebc99-9c0b-4ef8-bb6d-6bb9bd380d33', 'd4eebc99-9c0b-4ef8-bb6d-6bb9bd380d44', 'd5eebc99-9c0b-4ef8-bb6d-6bb9bd380d55', 'd6eebc99-9c0b-4ef8-bb6d-6bb9bd380d66');
DELETE FROM orders WHERE id IN ('d1eebc99-9c0b-4ef8-bb6d-6bb9bd380d11', 'd2eebc99-9c0b-4ef8-bb6d-6bb9bd380d22', 'd3eebc99-9c0b-4ef8-bb6d-6bb9bd380d33', 'd4eebc99-9c0b-4ef8-bb6d-6bb9bd380d44', 'd5eebc99-9c0b-4ef8-bb6d-6bb9bd380d55', 'd6eebc99-9c0b-4ef8-bb6d-6bb9bd380d66');

-- Insert Orders
-- sub_total = 50000, total_amount = 50000
INSERT INTO orders (id, user_id, customer_name, phone, address_text, status, sub_total, total_amount, tracking_code, created_at) VALUES 
('d1eebc99-9c0b-4ef8-bb6d-6bb9bd380d11', 'b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'Nguyen Van A', '0912345678', 'Ha Noi', 'DELIVERED', 50000, 50000, 'ORD-000001', '2023-10-01 10:00:00'),
('d2eebc99-9c0b-4ef8-bb6d-6bb9bd380d22', 'b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'Tran Thi B', '0987654321', 'HCM', 'DELIVERED', 125000, 125000, 'ORD-000002', '2023-10-05 14:30:00'),
('d3eebc99-9c0b-4ef8-bb6d-6bb9bd380d33', 'b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'Le Van C', '0909090909', 'Da Nang', 'DELIVERED', 77000, 77000, 'ORD-000003', '2023-10-10 09:15:00'),
('d4eebc99-9c0b-4ef8-bb6d-6bb9bd380d44', 'b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'Pham Thi D', '0933333333', 'Can Tho', 'DELIVERED', 115000, 115000, 'ORD-000004', '2023-10-15 16:45:00'),
('d5eebc99-9c0b-4ef8-bb6d-6bb9bd380d55', 'b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'Hoang Van E', '0944444444', 'Hai Phong', 'DELIVERED', 25000, 25000, 'ORD-000005', '2023-10-20 08:00:00'),
('d6eebc99-9c0b-4ef8-bb6d-6bb9bd380d66', 'b1eebc99-9c0b-4ef8-bb6d-6bb9bd380a22', 'Nguyen Van A', '0912345678', 'Ha Noi', 'DELIVERED', 90000, 90000, 'ORD-000006', '2023-10-25 11:20:00');

-- Insert Order Items
INSERT INTO order_items (order_id, product_id, quantity, snapshot_options, snapshot_product_name, snapshot_unit_price, sub_total) VALUES 
('d1eebc99-9c0b-4ef8-bb6d-6bb9bd380d11', 'f1eebc99-9c0b-4ef8-bb6d-6bb9bd380f11', 2, 'Da: 100%, Duong: 100%', 'Ca phe den da', 25000, 50000),

('d2eebc99-9c0b-4ef8-bb6d-6bb9bd380d22', 'f2eebc99-9c0b-4ef8-bb6d-6bb9bd380f22', 1, 'Da: 50%, Duong: 50%', 'Ca phe sua da', 29000, 29000),
('d2eebc99-9c0b-4ef8-bb6d-6bb9bd380d22', 'f3eebc99-9c0b-4ef8-bb6d-6bb9bd380f33', 3, 'Da: 100%, Duong: 100%', 'Bac xiu', 32000, 96000),

('d3eebc99-9c0b-4ef8-bb6d-6bb9bd380d33', 'f4eebc99-9c0b-4ef8-bb6d-6bb9bd380f44', 1, 'Da: Nong, Duong: It', 'Espresso', 35000, 35000),
('d3eebc99-9c0b-4ef8-bb6d-6bb9bd380d33', 'f7eebc99-9c0b-4ef8-bb6d-6bb9bd380f77', 1, 'Da: 100%, Duong: 100%', 'Tra vai nhiet doi', 42000, 42000),

('d4eebc99-9c0b-4ef8-bb6d-6bb9bd380d44', 'f5eebc99-9c0b-4ef8-bb6d-6bb9bd380f55', 2, 'Da: 100%, Duong: 50%', 'Latte Da', 45000, 90000),
('d4eebc99-9c0b-4ef8-bb6d-6bb9bd380d44', 'f1eebc99-9c0b-4ef8-bb6d-6bb9bd380f11', 1, 'Da: 100%, Duong: Khong', 'Ca phe den da', 25000, 25000),

('d5eebc99-9c0b-4ef8-bb6d-6bb9bd380d55', 'f1eebc99-9c0b-4ef8-bb6d-6bb9bd380f11', 1, 'Da: It, Duong: 100%', 'Ca phe den da', 25000, 25000),

('d6eebc99-9c0b-4ef8-bb6d-6bb9bd380d66', 'f2eebc99-9c0b-4ef8-bb6d-6bb9bd380f22', 2, 'Da: 100%, Duong: 100%', 'Ca phe sua da', 29000, 58000),
('d6eebc99-9c0b-4ef8-bb6d-6bb9bd380d66', 'f3eebc99-9c0b-4ef8-bb6d-6bb9bd380f33', 1, 'Da: 100%, Duong: 50%', 'Bac xiu', 32000, 32000);
