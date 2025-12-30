package com.paybridge.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.paybridge.services.MercadoPagoService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class HelloController {

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

}


