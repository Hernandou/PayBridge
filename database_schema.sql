-- 1. Tabla: users
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- 2. Tabla: products
CREATE TABLE products (
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    price DOUBLE,
    stock INT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- 3. Tabla: shopping_cart
CREATE TABLE shopping_cart (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(255),
    description TEXT,
    price DOUBLE,
    quantity INT,
    total DOUBLE,
    status VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_shoppingcart_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 4. Tabla: cart_items
CREATE TABLE cart_items (
    cart_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT,
    price DOUBLE,
    product_name VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_cartitems_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_cartitems_product FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- 5. Tabla: order_item
CREATE TABLE order_item (
    orderltem_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    quantity INT,
    price DOUBLE,
    subtotal DOUBLE,
    created_at TIMESTAMP,
    CONSTRAINT fk_orderitem_product FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- 6. Tabla: orders (Tiene PK Compuesta: user_id + orderltem_id)
CREATE TABLE orders (
    user_id BIGINT NOT NULL,
    orderltem_id BIGINT NOT NULL,
    total DOUBLE,
    status VARCHAR(50),
    payment_method VARCHAR(50),
    shipping_address TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY (user_id, orderltem_id),
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_orders_orderitem FOREIGN KEY (orderltem_id) REFERENCES order_item(orderltem_id) ON DELETE CASCADE
);

-- 7. Tabla: payments (Tiene PK Compuesta/EmbeddedId: user_id + shopping_cart_id)
CREATE TABLE payments (
    user_id BIGINT NOT NULL,
    shopping_cart_id BIGINT NOT NULL,
    payment_id VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    PRIMARY KEY (user_id, shopping_cart_id),
    CONSTRAINT fk_payments_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_payments_shoppingcart FOREIGN KEY (shopping_cart_id) REFERENCES shopping_cart(id) ON DELETE CASCADE
);


-- 1. Insertar el Usuario
INSERT INTO users (name, email, password, created_at, updated_at) 
VALUES 
('Hernán Martín', 'hernan@test.com', 'password123', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 2. Insertar algunos Productos
INSERT INTO products (name, description, price, stock, created_at, updated_at) 
VALUES 
('Monitor 24 pulgadas', 'Monitor IPS Full HD a 75Hz', 150000.00, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Teclado Mecánico', 'Teclado RGB switch blue', 45000.00, 35, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Mouse Inalámbrico', 'Mouse gamer 10000 DPI', 25000.00, 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 3. Insertar los dos Carritos de Compras para el usuario (Asumiendo que el usuario insertado tiene ID 1)
-- Carrito 1: Pendiente de pago
INSERT INTO shopping_cart (user_id, name, description, price, quantity, total, status, created_at, updated_at) 
VALUES 
(1, 'Compra SetUp', 'Monitor y Teclado', 195000.00, 2, 195000.00, 'pending', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Carrito 2: Éste carrito podría ser uno abandonado o en proceso
INSERT INTO shopping_cart (user_id, name, description, price, quantity, total, status, created_at, updated_at) 
VALUES 
(1, 'Compra Accesorios', 'Solo el mouse', 25000.00, 1, 25000.00, 'abandoned', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- 4. Insertar los ítems de cada carrito (Vinculando los productos a los carritos)

-- Ítems para el Carrito 1 (ID 1)
-- Producto 1 (Monitor) en Carrito 1
INSERT INTO cart_items (user_id, product_id, shopping_cart_id, quantity, price, product_name, created_at, updated_at) 
VALUES 
(1, 1, 1, 1, 150000.00, 'Monitor 24 pulgadas', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Producto 2 (Teclado) en Carrito 1
INSERT INTO cart_items (user_id, product_id, shopping_cart_id, quantity, price, product_name, created_at, updated_at) 
VALUES 
(1, 2, 1, 1, 45000.00, 'Teclado Mecánico', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Ítems para el Carrito 2 (ID 2)
-- Producto 3 (Mouse) en Carrito 2
INSERT INTO cart_items (user_id, product_id, shopping_cart_id, quantity, price, product_name, created_at, updated_at) 
VALUES 
(1, 3, 2, 1, 25000.00, 'Mouse Inalámbrico', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);



