package com.paybridge.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.paybridge.services.MercadoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import com.paybridge.services.ShoppingCartService;
import org.springframework.web.bind.annotation.RequestBody;
import com.paybridge.dto.ShoppingCartDTO;

@RestController
public class MercadoPagoController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @GetMapping("/")
    public String hello() {
        return "¡Bienvenido a PayBridge!";
    }

    @GetMapping("/api/getAllPayMethods")
    public String getAllPayMethods() {
        try {
            return mercadoPagoService.getAllPayMethods();
             
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/api/createShoppingCart")
    public String createShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        try {
            shoppingCartService.saveShoppingCart(shoppingCartDTO);
            return "Shopping cart created successfully: " + shoppingCartDTO.getId();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

}


