package com.paybridge.mappers;
import com.paybridge.entities.ShoppingCartEntity;
import com.paybridge.dto.ShoppingCartDTO;
public class ShoppingCartMapper {
    
    public ShoppingCartDTO mapEntityToDTO(ShoppingCartEntity shoppingCartEntity){
        return new ShoppingCartDTO(
            shoppingCartEntity.getId() != null ? shoppingCartEntity.getId().toString() : null,
            shoppingCartEntity.getName(),
            shoppingCartEntity.getDescription(),
            shoppingCartEntity.getPrice() != null ? shoppingCartEntity.getPrice().toString() : null,
            shoppingCartEntity.getQuantity() != null ? shoppingCartEntity.getQuantity().toString() : null,
            shoppingCartEntity.getTotal() != null ? shoppingCartEntity.getTotal().toString() : null,
            shoppingCartEntity.getCreatedAt() != null ? shoppingCartEntity.getCreatedAt().toString() : null,
            shoppingCartEntity.getUpdatedAt() != null ? shoppingCartEntity.getUpdatedAt().toString() : null,
            shoppingCartEntity.getStatus()
        );
    }
    
}
