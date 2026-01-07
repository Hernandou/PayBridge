package com.paybridge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.paybridge.entities.ProductsEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductsEntity, Long> {
    
    // Buscar por nombre (método automático de Spring Data JPA)
    Optional<ProductsEntity> findByName(String name);
    
    // Buscar productos por nombre que contenga un texto
    List<ProductsEntity> findByNameContainingIgnoreCase(String name);
    
    // Buscar productos por rango de precio
    @Query("SELECT p FROM ProductsEntity p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<ProductsEntity> findByPriceRange(Double minPrice, Double maxPrice);
    
    // Buscar productos con stock disponible
    @Query("SELECT p FROM ProductsEntity p WHERE p.stock > 0")
    List<ProductsEntity> findAvailableProducts();
    
    // Buscar productos con stock bajo (menos de cierto número)
    @Query("SELECT p FROM ProductsEntity p WHERE p.stock < :threshold")
    List<ProductsEntity> findLowStockProducts(Integer threshold);
    
    // Buscar productos ordenados por precio ascendente
    @Query("SELECT p FROM ProductsEntity p ORDER BY p.price ASC")
    List<ProductsEntity> findAllOrderByPriceAsc();
    
    // Buscar productos ordenados por precio descendente
    @Query("SELECT p FROM ProductsEntity p ORDER BY p.price DESC")
    List<ProductsEntity> findAllOrderByPriceDesc();
}

