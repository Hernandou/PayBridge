-- ============================================
-- PAYBRIDGE DATABASE SCHEMA - PostgreSQL
-- ============================================

-- Tabla: User
-- Entidad principal de usuarios
CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para users
CREATE INDEX idx_email ON users(email);

-- Tabla: Product
-- Entidad principal de productos
CREATE TABLE products (
    product_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    stock INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para products
CREATE INDEX idx_name ON products(name);

-- Tabla: CartItem
-- Relación asociativa entre User y Product (carrito de compras)
CREATE TABLE shopping_cart (
    id          BIGSERIAL        PRIMARY KEY,
    user_id     BIGINT           NOT NULL,
    name        VARCHAR(255),
    description TEXT,
    price       DOUBLE PRECISION,
    quantity    INTEGER,
    total       DOUBLE PRECISION,
    status      VARCHAR(50)      NOT NULL DEFAULT 'PENDING',
    created_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_shopping_cart_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE INDEX idx_shopping_cart_user_id ON shopping_cart(user_id);

-- Índices para cart_items
CREATE INDEX idx_cart_user_id ON cart_items(user_id);
CREATE INDEX idx_cart_product_id ON cart_items(product_id);

-- Tabla: ShoppingCart
-- Snapshot del carrito al momento del checkout
CREATE TABLE shopping_cart (
    id          BIGSERIAL        PRIMARY KEY,
    user_id     BIGINT           NOT NULL REFERENCES users(user_id),
    name        VARCHAR(255),
    description TEXT,
    price       DOUBLE PRECISION,
    quantity    INTEGER,
    total       DOUBLE PRECISION,
    status      VARCHAR(50)      NOT NULL DEFAULT 'PENDING',
    created_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

-- Índice para shopping_cart
CREATE INDEX idx_shopping_cart_user_id ON shopping_cart(user_id);



-- ============================================
-- NOTA IMPORTANTE SOBRE EL MODELO:
-- Este esquema refleja el modelo ERD donde:
-- 1. Order tiene clave compuesta (user_id [PK], orderltem_id [PK])
-- 2. Order representa la relación "Order" (diamante) entre User y Order_Item
-- 3. Order_Item tiene relación "Compone" con Product
-- ============================================

-- Tabla: Order_Item
-- Entidad Order_Item según el modelo (tiene Orderltem_id como PK)
-- Relación "Compone" con Product
CREATE TABLE order_item (
    Orderltem_id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL, -- Para la relación "Compone" con Product
    quantity INTEGER NOT NULL,
    price DECIMAL(10, 2) NOT NULL, -- Precio histórico al momento de la compra
    subtotal DECIMAL(10, 2) NOT NULL, -- quantity * price (calculado)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE RESTRICT
);

-- Índices para order_item
CREATE INDEX idx_order_item_product_id ON order_item(product_id);

-- Tabla: Order
-- Entidad Order según el modelo (clave compuesta: user_id [PK] y orderltem_id [PK])
-- Representa la relación "Order" (diamante) entre User y Order_Item
-- Cada fila vincula un User con un Order_Item específico
CREATE TABLE orders (
    user_id BIGINT NOT NULL,
    orderltem_id BIGINT NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING', -- PENDING, PAID, SHIPPED, DELIVERED, CANCELLED
    payment_method VARCHAR(100),
    shipping_address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, orderltem_id), -- Clave primaria compuesta según el modelo
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE RESTRICT,
    FOREIGN KEY (orderltem_id) REFERENCES order_item(Orderltem_id) ON DELETE CASCADE
);

-- Índices para orders
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_orderltem_id ON orders(orderltem_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at);

-- ============================================
-- FUNCIÓN PARA ACTUALIZAR updated_at AUTOMÁTICAMENTE
-- ============================================

-- Función que actualiza el campo updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Triggers para actualizar updated_at automáticamente
CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_products_updated_at
    BEFORE UPDATE ON products
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_cart_items_updated_at
    BEFORE UPDATE ON cart_items
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_orders_updated_at
    BEFORE UPDATE ON orders
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- ============================================
-- TRIGGERS PARA CALCULAR SUBTOTAL AUTOMÁTICAMENTE
-- ============================================

-- Función para calcular subtotal en order_item
CREATE OR REPLACE FUNCTION calculate_order_item_subtotal()
RETURNS TRIGGER AS $$
BEGIN
    NEW.subtotal = NEW.quantity * NEW.price;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para calcular subtotal antes de insertar o actualizar
CREATE TRIGGER calculate_order_item_subtotal_trigger
    BEFORE INSERT OR UPDATE ON order_item
    FOR EACH ROW
    EXECUTE FUNCTION calculate_order_item_subtotal();

-- ============================================
-- VISTAS ÚTILES
-- ============================================

-- Vista: Carrito completo del usuario con información del producto
CREATE OR REPLACE VIEW v_user_cart AS
SELECT 
    ci.cart_item_id,
    ci.user_id,
    u.name AS user_name,
    ci.product_id,
    p.name AS product_name,
    p.description AS product_description,
    ci.quantity,
    ci.price AS cart_price,
    p.price AS current_price,
    (ci.quantity * ci.price) AS subtotal
FROM cart_items ci
INNER JOIN users u ON ci.user_id = u.user_id
INNER JOIN products p ON ci.product_id = p.product_id;

-- Vista: Detalles completos de una orden
-- Refleja la relación "Order" (diamante) entre User y Order_Item
CREATE OR REPLACE VIEW v_order_details AS
SELECT 
    o.user_id,
    o.orderltem_id,
    u.name AS user_name,
    u.email AS user_email,
    o.total AS order_total,
    o.status AS order_status,
    o.created_at AS order_date,
    oi.Orderltem_id AS order_item_id,
    oi.product_id,
    p.name AS product_name,
    oi.quantity,
    oi.price AS item_price,
    oi.subtotal AS item_subtotal
FROM orders o
INNER JOIN users u ON o.user_id = u.user_id
INNER JOIN order_item oi ON o.orderltem_id = oi.Orderltem_id
INNER JOIN products p ON oi.product_id = p.product_id
ORDER BY o.user_id, o.orderltem_id;

-- ============================================
-- DATOS DE PRUEBA - Llenar todas las tablas
-- ============================================

-- Limpiar datos existentes (opcional - descomentar si quieres empezar desde cero)
-- TRUNCATE TABLE orders CASCADE;
-- TRUNCATE TABLE order_item CASCADE;
-- TRUNCATE TABLE cart_items CASCADE;
-- TRUNCATE TABLE products CASCADE;
-- TRUNCATE TABLE users CASCADE;

-- Alternativa: Eliminar solo datos de prueba (más seguro)
-- DELETE FROM orders;
-- DELETE FROM order_item;
-- DELETE FROM cart_items;
-- DELETE FROM products WHERE product_id <= 12;
-- DELETE FROM users WHERE user_id <= 5;

-- ==========================================
-- BORRAR TODOS LOS DATOS DE LAS TABLAS
-- ==========================================
DELETE FROM orders;
DELETE FROM order_item;
DELETE FROM cart_items;
DELETE FROM products;
DELETE FROM users;

-- ============================================
-- 1. USUARIOS DE PRUEBA
-- ============================================
INSERT INTO users (email, name, password) VALUES
('juan@email.com', 'Juan Pérez', '$2a$10$hashed_password_123'),
('maria@email.com', 'María García', '$2a$10$hashed_password_456'),
('carlos@email.com', 'Carlos Rodríguez', '$2a$10$hashed_password_789'),
('ana@email.com', 'Ana Martínez', '$2a$10$hashed_password_012'),
('pedro@email.com', 'Pedro López', '$2a$10$hashed_password_345')
ON CONFLICT (email) DO NOTHING;

-- ============================================
-- 2. PRODUCTOS DE PRUEBA
-- ============================================
INSERT INTO products (name, description, price, stock) VALUES
-- Laptops
('Laptop HP Pavilion', 'Laptop HP 15.6 pulgadas, Intel Core i5, 8GB RAM, 256GB SSD', 1000.00, 15),
('Laptop Dell Inspiron', 'Laptop Dell 14 pulgadas, AMD Ryzen 5, 16GB RAM, 512GB SSD', 1200.00, 10),
('Laptop Lenovo ThinkPad', 'Laptop Lenovo 15.6 pulgadas, Intel Core i7, 16GB RAM, 512GB SSD', 1500.00, 8),

-- Periféricos
('Mouse Logitech MX Master', 'Mouse inalámbrico Logitech MX Master 3, ergonómico', 50.00, 50),
('Mouse Razer DeathAdder', 'Mouse gaming Razer DeathAdder V3, 20000 DPI', 80.00, 30),
('Teclado Mecánico RGB', 'Teclado mecánico RGB, switches Cherry MX Red, retroiluminado', 120.00, 25),
('Teclado Logitech K380', 'Teclado inalámbrico Logitech K380, compacto', 40.00, 40),

-- Monitores
('Monitor Samsung 27"', 'Monitor Samsung 27 pulgadas, 4K UHD, IPS', 350.00, 12),
('Monitor LG UltraWide', 'Monitor LG 34 pulgadas, UltraWide, Curvo, 144Hz', 450.00, 8),

-- Audio
('Auriculares Sony WH-1000XM4', 'Auriculares inalámbricos Sony, cancelación de ruido', 280.00, 20),
('Auriculares HyperX Cloud II', 'Auriculares gaming HyperX Cloud II, 7.1 surround', 100.00, 35),

-- Almacenamiento
('Disco SSD Samsung 1TB', 'Disco SSD Samsung 980 PRO, NVMe, 1TB', 150.00, 30),
('Disco HDD Seagate 2TB', 'Disco duro Seagate BarraCuda, 2TB, 7200 RPM', 60.00, 25);

-- ============================================
-- 3. ITEMS EN CARRITO DE PRUEBA
-- ============================================
-- Usa ON CONFLICT para actualizar cantidad si el producto ya está en el carrito
INSERT INTO cart_items (user_id, product_id, quantity, price) VALUES
-- Carrito de Juan (user_id = 1)
(1, 1, 2, 1000.00),  -- 2 Laptops HP
(1, 4, 1, 50.00),    -- 1 Mouse Logitech
(1, 7, 1, 120.00),   -- 1 Teclado Mecánico

-- Carrito de María (user_id = 2)
(2, 2, 1, 1200.00),  -- 1 Laptop Dell
(2, 8, 1, 350.00),   -- 1 Monitor Samsung
(2, 10, 1, 280.00),  -- 1 Auriculares Sony

-- Carrito de Carlos (user_id = 3)
(3, 3, 1, 1500.00),  -- 1 Laptop Lenovo
(3, 5, 1, 80.00),    -- 1 Mouse Razer
(3, 9, 1, 450.00),   -- 1 Monitor LG
(3, 11, 2, 150.00),  -- 2 Discos SSD Samsung

-- Carrito de Ana (user_id = 4)
(4, 6, 1, 40.00),    -- 1 Teclado Logitech
(4, 11, 1, 150.00),  -- 1 Disco SSD
(4, 12, 1, 60.00)    -- 1 Disco HDD
ON CONFLICT (user_id, product_id) 
DO UPDATE SET 
    quantity = EXCLUDED.quantity,
    price = EXCLUDED.price,
    updated_at = CURRENT_TIMESTAMP;

-- ============================================
-- 4. ORDER_ITEMS DE PRUEBA (Historial de compras)
-- ============================================
-- Limpiar order_items existentes antes de insertar (para evitar IDs duplicados)
DELETE FROM order_item WHERE Orderltem_id <= 15;

INSERT INTO order_item (product_id, quantity, price, subtotal) VALUES
-- OrderItems para Juan
(1, 1, 1000.00, 1000.00),   -- OrderItem #1: 1 Laptop HP
(4, 2, 50.00, 100.00),      -- OrderItem #2: 2 Mouse Logitech
(7, 1, 120.00, 120.00),    -- OrderItem #3: 1 Teclado Mecánico

-- OrderItems para María
(2, 1, 1200.00, 1200.00),  -- OrderItem #4: 1 Laptop Dell
(8, 1, 350.00, 350.00),    -- OrderItem #5: 1 Monitor Samsung
(10, 1, 280.00, 280.00),   -- OrderItem #6: 1 Auriculares Sony

-- OrderItems para Carlos
(3, 1, 1500.00, 1500.00),  -- OrderItem #7: 1 Laptop Lenovo
(5, 1, 80.00, 80.00),      -- OrderItem #8: 1 Mouse Razer
(9, 1, 450.00, 450.00),    -- OrderItem #9: 1 Monitor LG
(11, 1, 150.00, 150.00),   -- OrderItem #10: 1 Disco SSD

-- OrderItems para Ana
(6, 1, 40.00, 40.00),      -- OrderItem #11: 1 Teclado Logitech
(12, 2, 60.00, 120.00),    -- OrderItem #12: 2 Discos HDD

-- OrderItems para Pedro
(1, 1, 1000.00, 1000.00),  -- OrderItem #13: 1 Laptop HP
(4, 1, 50.00, 50.00),     -- OrderItem #14: 1 Mouse Logitech
(7, 1, 120.00, 120.00);   -- OrderItem #15: 1 Teclado Mecánico

-- ============================================
-- 5. ORDERS DE PRUEBA (Relación User-OrderItem)
-- ============================================
-- Limpiar orders existentes antes de insertar
DELETE FROM orders WHERE user_id <= 5;

INSERT INTO orders (user_id, orderltem_id, total, status, payment_method, shipping_address) VALUES
-- Órdenes de Juan (user_id = 1)
(1, 1, 1000.00, 'PAID', 'CREDIT_CARD', 'Calle Principal 123, Ciudad, CP 12345'),
(1, 2, 100.00, 'PAID', 'CREDIT_CARD', 'Calle Principal 123, Ciudad, CP 12345'),
(1, 3, 120.00, 'SHIPPED', 'DEBIT_CARD', 'Calle Principal 123, Ciudad, CP 12345'),

-- Órdenes de María (user_id = 2)
(2, 4, 1200.00, 'PAID', 'PAYPAL', 'Avenida Central 456, Ciudad, CP 67890'),
(2, 5, 350.00, 'DELIVERED', 'CREDIT_CARD', 'Avenida Central 456, Ciudad, CP 67890'),
(2, 6, 280.00, 'PAID', 'CREDIT_CARD', 'Avenida Central 456, Ciudad, CP 67890'),

-- Órdenes de Carlos (user_id = 3)
(3, 7, 1500.00, 'PAID', 'CREDIT_CARD', 'Boulevard Norte 789, Ciudad, CP 11111'),
(3, 8, 80.00, 'SHIPPED', 'CREDIT_CARD', 'Boulevard Norte 789, Ciudad, CP 11111'),
(3, 9, 450.00, 'PAID', 'PAYPAL', 'Boulevard Norte 789, Ciudad, CP 11111'),
(3, 10, 150.00, 'PENDING', 'CREDIT_CARD', 'Boulevard Norte 789, Ciudad, CP 11111'),

-- Órdenes de Ana (user_id = 4)
(4, 11, 40.00, 'PAID', 'DEBIT_CARD', 'Calle Sur 321, Ciudad, CP 22222'),
(4, 12, 120.00, 'CANCELLED', 'CREDIT_CARD', 'Calle Sur 321, Ciudad, CP 22222'),

-- Órdenes de Pedro (user_id = 5)
(5, 13, 1000.00, 'PAID', 'CREDIT_CARD', 'Plaza Mayor 654, Ciudad, CP 33333'),
(5, 14, 50.00, 'PAID', 'CREDIT_CARD', 'Plaza Mayor 654, Ciudad, CP 33333'),
(5, 15, 120.00, 'PENDING', 'PAYPAL', 'Plaza Mayor 654, Ciudad, CP 33333');


INSERT INTO cart_items (user_id, product_id, product_name, quantity, price, created_at, updated_at) VALUES
(1, 1, 'Notebook Lenovo IdeaPad',  2, 850.00, NOW(), NOW()),
(1, 2, 'Mouse Logitech MX Master', 1, 75.50,  NOW(), NOW()),
(1, 3, 'Teclado Mecánico Redragon',1, 45.00,  NOW(), NOW()),
(1, 4, 'Monitor Samsung 24"',      1, 220.00, NOW(), NOW()),
(1, 5, 'Auriculares Sony WH-1000', 1, 180.00, NOW(), NOW())

-- ============================================
-- VERIFICACIÓN DE DATOS INSERTADOS
-- ============================================

-- Verificar cantidad de registros insertados
-- SELECT 'users' AS tabla, COUNT(*) AS cantidad FROM users
-- UNION ALL
-- SELECT 'products', COUNT(*) FROM products
-- UNION ALL
-- SELECT 'cart_items', COUNT(*) FROM cart_items
-- UNION ALL
-- SELECT 'order_item', COUNT(*) FROM order_item
-- UNION ALL
-- SELECT 'orders', COUNT(*) FROM orders;

-- ============================================
-- QUERIES ÚTILES DE CONSULTA
-- ============================================

-- Obtener carrito de un usuario
-- SELECT * FROM v_user_cart WHERE user_id = 1;

-- Obtener total del carrito de un usuario
-- SELECT SUM(subtotal) AS total_cart FROM v_user_cart WHERE user_id = 1;

-- Obtener detalles de una orden específica (por user_id y orderltem_id)
-- SELECT * FROM v_order_details WHERE user_id = 1 AND orderltem_id = 1;

-- Obtener historial de compras de un usuario
-- SELECT * FROM v_order_details WHERE user_id = 1 ORDER BY order_date DESC;

-- Obtener todas las órdenes de un usuario (agrupadas)
-- SELECT user_id, COUNT(*) as total_orders, SUM(total) as total_spent 
-- FROM orders WHERE user_id = 1 GROUP BY user_id;

-- Ver todos los productos disponibles
-- SELECT product_id, name, price, stock FROM products ORDER BY name;

-- Ver carritos de todos los usuarios
-- SELECT * FROM v_user_cart ORDER BY user_id;

-- Ver todas las órdenes con detalles
-- SELECT * FROM v_order_details ORDER BY user_id, order_date DESC;

-- Estadísticas de ventas por usuario
-- SELECT 
--     u.name AS usuario,
--     COUNT(DISTINCT o.orderltem_id) AS total_items_comprados,
--     SUM(o.total) AS total_gastado,
--     COUNT(DISTINCT CASE WHEN o.status = 'PAID' THEN o.orderltem_id END) AS items_pagados
-- FROM users u
-- LEFT JOIN orders o ON u.user_id = o.user_id
-- GROUP BY u.user_id, u.name
-- ORDER BY total_gastado DESC;
