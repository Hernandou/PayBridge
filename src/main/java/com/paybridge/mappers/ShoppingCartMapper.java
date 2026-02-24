package com.paybridge.mappers;

import org.springframework.stereotype.Component;
import com.paybridge.entities.ShoppingCartEntity;
import com.paybridge.dto.ShoppingCartDTO;

@Component
public class ShoppingCartMapper {

    public ShoppingCartDTO mapEntityToDTO(ShoppingCartEntity shoppingCartEntity) {
        return new ShoppingCartDTO(
                shoppingCartEntity.getId() != null ? shoppingCartEntity.getId().toString() : null,
                shoppingCartEntity.getUser() != null ? shoppingCartEntity.getUser().getUserId().toString() : null,
                shoppingCartEntity.getName(),
                shoppingCartEntity.getDescription(),
                shoppingCartEntity.getPrice() != null ? shoppingCartEntity.getPrice().toString() : null,
                shoppingCartEntity.getQuantity() != null ? shoppingCartEntity.getQuantity().toString() : null,
                shoppingCartEntity.getTotal() != null ? shoppingCartEntity.getTotal().toString() : null,
                shoppingCartEntity.getCreatedAt() != null ? shoppingCartEntity.getCreatedAt().toString() : null,
                shoppingCartEntity.getUpdatedAt() != null ? shoppingCartEntity.getUpdatedAt().toString() : null,
                shoppingCartEntity.getStatus());
    }

}
