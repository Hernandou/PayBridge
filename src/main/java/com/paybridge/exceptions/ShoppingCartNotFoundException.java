package com.paybridge.exceptions;

public class ShoppingCartNotFoundException extends RuntimeException {

    public ShoppingCartNotFoundException() {
        super("Shopping cart not found");
    }

    public ShoppingCartNotFoundException(String message) {
        super(message);
    }

}
