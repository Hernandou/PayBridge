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
CREATE TABLE cart_items (
    cart_item_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    price DECIMAL(10, 2) NOT NULL, -- Precio al momento de agregar al carrito
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    UNIQUE (user_id, product_id) -- Un usuario no puede tener el mismo producto duplicado en el carrito
);

-- Índices para cart_items
CREATE INDEX idx_cart_user_id ON cart_items(user_id);
CREATE INDEX idx_cart_product_id ON cart_items(product_id);

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
-- DATOS DE EJEMPLO (Opcional - para testing)
-- ============================================

-- Insertar usuarios de ejemplo
INSERT INTO users (email, name, password) VALUES
('juan@email.com', 'Juan Pérez', 'hashed_password_123'),
('maria@email.com', 'María García', 'hashed_password_456');

-- Insertar productos de ejemplo
INSERT INTO products (name, description, price, stock) VALUES
('Laptop HP', 'Laptop HP 15.6 pulgadas, 8GB RAM, 256GB SSD', 1000.00, 10),
('Mouse Logitech', 'Mouse inalámbrico Logitech MX Master', 50.00, 50),
('Teclado Mecánico', 'Teclado mecánico RGB, switches Cherry MX', 120.00, 30);

-- Insertar items al carrito de ejemplo
INSERT INTO cart_items (user_id, product_id, quantity, price) VALUES
(1, 1, 2, 1000.00), -- Juan tiene 2 laptops en el carrito
(1, 2, 1, 50.00);   -- Juan tiene 1 mouse en el carrito

-- Insertar Order_Items de ejemplo
INSERT INTO order_item (product_id, quantity, price, subtotal) VALUES
(1, 2, 1000.00, 2000.00), -- OrderItem #1: 2 laptops
(2, 1, 50.00, 50.00);     -- OrderItem #2: 1 mouse

-- Insertar Orders de ejemplo (relación entre User y Order_Item)
-- Nota: En este modelo, cada Order vincula un User con un Order_Item específico
INSERT INTO orders (user_id, orderltem_id, total, status) VALUES
(1, 1, 2000.00, 'PAID'),  -- Juan compró OrderItem #1 (2 laptops)
(1, 2, 50.00, 'PAID');    -- Juan compró OrderItem #2 (1 mouse)

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
