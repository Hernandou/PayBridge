package com.paybridge.mappers;

import com.paybridge.dto.CartItemDTO;
import com.paybridge.entities.CartItemEntity;
import com.paybridge.entities.ProductsEntity;
import com.paybridge.entities.UsersEntity;
import java.time.LocalDateTime;

public class CartItemMapper {

    public CartItemEntity mapDTOToEntity(CartItemDTO cartItemDTO) {
        CartItemEntity cartItemEntity = new CartItemEntity();
        cartItemEntity.setProduct(this.buildProductEntity(cartItemDTO.getProductId()));
        cartItemEntity.setUser(this.buildUserEntity(cartItemDTO.getUserId()));
        cartItemEntity.setQuantity(cartItemDTO.getQuantity());
        cartItemEntity.setPrice(cartItemDTO.getPrice());
        cartItemEntity.setCreatedAt(LocalDateTime.now());
        cartItemEntity.setUpdatedAt(LocalDateTime.now());
        return cartItemEntity;
    }

    private ProductsEntity buildProductEntity(Long productId) {
        ProductsEntity product = new ProductsEntity();
        product.setProductId(productId);
        return product;
    }

    private UsersEntity buildUserEntity(Long userId) {
        UsersEntity user = new UsersEntity();
        user.setId(userId);
        return user;
    }
}
