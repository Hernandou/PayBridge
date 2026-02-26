package com.paybridge.mappers;

import com.paybridge.dto.CartItemDTO;
import com.paybridge.entities.CartItemEntity;
import com.paybridge.entities.ProductsEntity;
import com.paybridge.entities.UsersEntity;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class CartItemMapper {

    public CartItemEntity mapDTOToEntity(CartItemDTO cartItemDTO) {
        CartItemEntity cartItemEntity = new CartItemEntity();
        cartItemEntity.setProduct(this.buildProductEntity(cartItemDTO.getProductId()));
        cartItemEntity.setUser(this.buildUserEntity(cartItemDTO.getUserId()));
        cartItemEntity.setQuantity(cartItemDTO.getQuantity());
        cartItemEntity.setPrice(cartItemDTO.getPrice());
        cartItemEntity.setProductName(cartItemDTO.getProductName());
        cartItemEntity.setCreatedAt(LocalDateTime.now());
        cartItemEntity.setUpdatedAt(LocalDateTime.now());
        return cartItemEntity;
    }

    public CartItemDTO mapEntityToDTO(CartItemEntity cartItemEntity) {
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setCartItemId(cartItemEntity.getCartItemId());
        cartItemDTO.setUserId(cartItemEntity.getUser().getUserId());
        cartItemDTO.setProductId(cartItemEntity.getProduct().getProductId());
        cartItemDTO.setProductName(cartItemEntity.getProductName());
        cartItemDTO.setQuantity(cartItemEntity.getQuantity());
        cartItemDTO.setPrice(cartItemEntity.getPrice());
        cartItemDTO
                .setCreatedAt(cartItemEntity.getCreatedAt() != null ? cartItemEntity.getCreatedAt().toString() : null);
        cartItemDTO
                .setUpdatedAt(cartItemEntity.getUpdatedAt() != null ? cartItemEntity.getUpdatedAt().toString() : null);
        return cartItemDTO;
    }

    private ProductsEntity buildProductEntity(Long productId) {
        ProductsEntity product = new ProductsEntity();
        product.setProductId(productId);
        return product;
    }

    private UsersEntity buildUserEntity(Long userId) {
        UsersEntity user = new UsersEntity();
        user.setUserId(userId);
        return user;
    }
}
