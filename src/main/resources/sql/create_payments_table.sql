-- Entidad débil payments
-- Clave primaria compuesta: user_id + shopping_cart_id + preference_id
-- payment_id es nullable hasta que el usuario complete el pago (llega por webhook)

CREATE TABLE payments (
    user_id           BIGINT       NOT NULL,
    shopping_cart_id  BIGINT       NOT NULL,
    preference_id     VARCHAR(255) NOT NULL,
    payment_id        VARCHAR(255) NULL,
    created_at        DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_at        DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (user_id, shopping_cart_id, preference_id),

    CONSTRAINT fk_payments_user
        FOREIGN KEY (user_id) REFERENCES users(id),

    CONSTRAINT fk_payments_shopping_cart
        FOREIGN KEY (shopping_cart_id) REFERENCES shopping_cart(id)
);
