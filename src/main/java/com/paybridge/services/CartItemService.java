package com.paybridge.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.paybridge.mappers.CartItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.paybridge.dto.ProductDTO;
import com.paybridge.entities.CartItemEntity;
import com.paybridge.entities.ProductsEntity;
import com.paybridge.entities.UsersEntity;
import com.paybridge.repository.CartItemRepository;
import org.springframework.stereotype.Service;
import com.paybridge.dto.CartItemDTO;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartItemMapper cartItemMapper;

    public void createCart(List<ProductDTO> allProducts, Long userId) {
        List<ProductDTO> filteredProducts = allProducts.stream().filter(
                p -> p.getProductId() != null && p.getQuantity() > 0 && p.getPrice() > 0)
                .collect(Collectors.toList());

        for (ProductDTO product : filteredProducts) {
            Optional<CartItemEntity> existing = cartItemRepository
                    .findByUserIdAndProductId(userId, product.getProductId());

            if (existing.isPresent()) {
                // El producto ya está en el carrito → sumar cantidad
                CartItemEntity item = existing.get();
                item.setQuantity(item.getQuantity() + product.getQuantity());
                item.setUpdatedAt(LocalDateTime.now());
                cartItemRepository.save(item);
            } else {
                // Producto nuevo → insertar
                CartItemEntity cartItemEntity = new CartItemEntity();
                cartItemEntity.setProduct(buildProductEntity(product.getProductId()));
                cartItemEntity.setUser(buildUserEntity(userId));
                cartItemEntity.setQuantity(product.getQuantity());
                cartItemEntity.setPrice(product.getPrice());
                cartItemEntity.setCreatedAt(LocalDateTime.now());
                cartItemEntity.setUpdatedAt(LocalDateTime.now());
                cartItemRepository.save(cartItemEntity);
            }
        }
    }

    private ProductsEntity buildProductEntity(Long productId) {
        ProductsEntity product = new ProductsEntity();
        product.setProductId(productId);
        return product;
    }

    public List<CartItemDTO> getCartItemsByUserId(Long userId) {
        return cartItemRepository.findByUserId(userId).stream()
                .map(cartItemMapper::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    private UsersEntity buildUserEntity(Long userId) {
        UsersEntity user = new UsersEntity();
        user.setUserId(userId);
        return user;
    }

}
