package com.paybridge.mappers;
import com.paybridge.entities.ShoppingCartEntity;
import com.paybridge.dto.ShoppingCartDTO;
public class ShoppingCartMapper {
    
    public ShoppingCartDTO mapEntityToDTO(ShoppingCartEntity shoppingCartEntity){
        return new ShoppingCartDTO(shoppingCartEntity.getId().toString(), shoppingCartEntity.getName(), shoppingCartEntity.getDescription(), shoppingCartEntity.getPrice().toString(), shoppingCartEntity.getQuantity().toString(), shoppingCartEntity.getTotal().toString(), shoppingCartEntity.getCreatedAt().toString(), shoppingCartEntity.getUpdatedAt().toString(), shoppingCartEntity.getStatus());
    }


    
}
