package com.paybridge.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return "¡Bienvenido a PayBridge!";
    }

    @GetMapping("/api/getAllPayMethods")
    public String apiHello() {
        return null;
    }


}


