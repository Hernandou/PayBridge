package com.paybridge.mappers;

import com.paybridge.dto.ProductDTO;
import com.paybridge.entities.ProductsEntity;
import java.time.LocalDateTime;

public class ProductMapper {

    public ProductsEntity mapDTOToEntity(ProductDTO productDTO) {
        ProductsEntity productEntity = new ProductsEntity();
        productEntity.setProductId(productDTO.getProductId());
        productEntity.setName(productDTO.getName());
        productEntity.setDescription(productDTO.getDescription());
        productEntity.setPrice(productDTO.getPrice());
        productEntity.setStock(productDTO.getStock());
        productEntity.setCreatedAt(productDTO.getCreatedAt() != null ? LocalDateTime.parse(productDTO.getCreatedAt())
                : LocalDateTime.now());
        productEntity.setUpdatedAt(productDTO.getUpdatedAt() != null ? LocalDateTime.parse(productDTO.getUpdatedAt())
                : LocalDateTime.now());
        return productEntity;
    }

    public ProductDTO mapEntityToDTO(ProductsEntity productEntity) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(productEntity.getProductId());
        productDTO.setName(productEntity.getName());
        productDTO.setDescription(productEntity.getDescription());
        productDTO.setPrice(productEntity.getPrice());
        productDTO.setStock(productEntity.getStock());
        productDTO.setCreatedAt(productEntity.getCreatedAt() != null ? productEntity.getCreatedAt().toString() : null);
        productDTO.setUpdatedAt(productEntity.getUpdatedAt() != null ? productEntity.getUpdatedAt().toString() : null);
        return productDTO;
    }
}
