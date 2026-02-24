package com.paybridge.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.paybridge.services.CartItemService;
import com.paybridge.services.MercadoPagoService;
import com.paybridge.services.ShoppingCartService;
import com.paybridge.dto.ShoppingCartDTO;
import com.paybridge.dto.ProductDTO;
import java.util.List;

@RestController
public class MercadoPagoController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CartItemService cartItemService;

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

    @GetMapping("/api/getAllShoppingCart")
    public List<ShoppingCartDTO> getAllShoppingCart(@RequestParam Long userId) {
        try {
            return shoppingCartService.getShoppingCartByUserId(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @GetMapping("/api/getShoppingCart")
    public ShoppingCartDTO getShoppingCart(@RequestParam Long shoppingCartId) {
        try {
            return shoppingCartService.getShoppingCartById(shoppingCartId);
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @PostMapping("/api/createCart")
    public String createCart(@RequestBody List<ProductDTO> allProducts, @RequestParam Long userId) {
        try {
            cartItemService.createCart(allProducts, userId);
            return "Cart item created successfully";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/api/buyShoppingCart")
    public String buyShoppingCart(@RequestParam Long shoppingCartId) {

        try {
            mercadoPagoService.buyShoppingCart(shoppingCartId);
            return "Shopping cart bought successfully: " + shoppingCartId;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }

    }

}
