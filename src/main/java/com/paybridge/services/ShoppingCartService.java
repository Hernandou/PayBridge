package com.paybridge.services;
import org.springframework.stereotype.Service;
import com.paybridge.dto.ShoppingCartDTO;
import com.paybridge.repository.ShoppingCartRepository;
import com.paybridge.entities.ShoppingCartEntity;
import java.time.LocalDateTime;

@Service
public class ShoppingCartService{

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public void saveShoppingCart(ShoppingCartDTO shoppingCartDTO){
        //save the shopping cart in the database
        ShoppingCartEntity shoppingCartEntity = new ShoppingCartEntity();
        shoppingCartEntity.setName(shoppingCartDTO.getName());
        shoppingCartEntity.setDescription(shoppingCartDTO.getDescription());
        shoppingCartEntity.setPrice(Double.parseDouble(shoppingCartDTO.getPrice()));
        shoppingCartEntity.setQuantity(Integer.parseInt(shoppingCartDTO.getQuantity()));
        shoppingCartEntity.setTotal(Double.parseDouble(shoppingCartDTO.getTotal()));
        shoppingCartEntity.setStatus(shoppingCartDTO.getStatus());
        shoppingCartEntity.setCreatedAt(LocalDateTime.now());
        shoppingCartEntity.setUpdatedAt(LocalDateTime.now());
        System.out.println(shoppingCartEntity.toString());
        shoppingCartRepository.save(shoppingCartEntity);
    }

    
}
