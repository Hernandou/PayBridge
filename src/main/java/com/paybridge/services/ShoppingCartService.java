package com.paybridge.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.paybridge.dto.ShoppingCartDTO;
import com.paybridge.entities.ShoppingCartEntity;
import com.paybridge.entities.UsersEntity;
import com.paybridge.mappers.ShoppingCartMapper;
import com.paybridge.repository.ShoppingCartRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public void saveShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCartEntity shoppingCartEntity = new ShoppingCartEntity();
        shoppingCartEntity.setUser(buildUserEntity(Long.parseLong(shoppingCartDTO.getUserId())));
        shoppingCartEntity.setName(shoppingCartDTO.getName());
        shoppingCartEntity.setDescription(shoppingCartDTO.getDescription());
        shoppingCartEntity.setPrice(Double.parseDouble(shoppingCartDTO.getPrice()));
        shoppingCartEntity.setQuantity(Integer.parseInt(shoppingCartDTO.getQuantity()));
        shoppingCartEntity.setTotal(Double.parseDouble(shoppingCartDTO.getTotal()));
        shoppingCartEntity.setStatus(shoppingCartDTO.getStatus());
        shoppingCartEntity.setCreatedAt(LocalDateTime.now());
        shoppingCartEntity.setUpdatedAt(LocalDateTime.now());
        shoppingCartRepository.save(shoppingCartEntity);
    }

    private UsersEntity buildUserEntity(Long userId) {
        UsersEntity user = new UsersEntity();
        user.setUserId(userId);
        return user;
    }

    public List<ShoppingCartDTO> getShoppingCartByUserId(Long userId) throws Exception {
        List<ShoppingCartEntity> entities = shoppingCartRepository.findByUserId(userId);
        if (entities.isEmpty()) {
            throw new Exception("No shopping cart history found for user: " + userId);
        }
        return entities.stream()
                .map(shoppingCartMapper::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    public ShoppingCartDTO getShoppingCartById(Long shoppingCartId) throws Exception {
        Optional<ShoppingCartEntity> shoppingCart = shoppingCartRepository.findById(shoppingCartId);
        if (shoppingCart.isEmpty()) {
            throw new Exception("Shopping cart not found");
        }
        return shoppingCartMapper.mapEntityToDTO(shoppingCart.get());
    }

}
