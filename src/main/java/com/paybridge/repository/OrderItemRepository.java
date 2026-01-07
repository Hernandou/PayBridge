package com.paybridge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.paybridge.entities.OrderItemEntity;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    
    // Buscar todos los order items de un producto
    @Query("SELECT oi FROM OrderItemEntity oi WHERE oi.product.productId = :productId")
    List<OrderItemEntity> findByProductId(@Param("productId") Long productId);
    
    // Contar cuántas veces se ha comprado un producto
    @Query("SELECT COUNT(oi) FROM OrderItemEntity oi WHERE oi.product.productId = :productId")
    Long countByProductId(@Param("productId") Long productId);
    
    // Calcular el total vendido de un producto
    @Query("SELECT SUM(oi.subtotal) FROM OrderItemEntity oi WHERE oi.product.productId = :productId")
    Double calculateTotalSalesByProductId(@Param("productId") Long productId);
    
    // Buscar order items ordenados por fecha de creación
    @Query("SELECT oi FROM OrderItemEntity oi ORDER BY oi.createdAt DESC")
    List<OrderItemEntity> findAllOrderByCreatedAtDesc();
}

