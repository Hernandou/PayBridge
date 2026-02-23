package com.paybridge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.paybridge.entities.CartItemEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    // Buscar todos los items del carrito de un usuario
    @Query("SELECT c FROM CartItemEntity c WHERE c.user.id = :userId")
    List<CartItemEntity> findByUserId(@Param("userId") Long userId);

    // Buscar un item específico del carrito de un usuario
    @Query("SELECT c FROM CartItemEntity c WHERE c.user.id = :userId AND c.product.productId = :productId")
    Optional<CartItemEntity> findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    // Verificar si un producto ya está en el carrito de un usuario
    @Query("SELECT COUNT(c) > 0 FROM CartItemEntity c WHERE c.user.id = :userId AND c.product.productId = :productId")
    boolean existsByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    // Contar items en el carrito de un usuario
    @Query("SELECT COUNT(c) FROM CartItemEntity c WHERE c.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);

    // Calcular el total del carrito de un usuario
    @Query("SELECT SUM(c.price * c.quantity) FROM CartItemEntity c WHERE c.user.id = :userId")
    Double calculateTotalByUserId(@Param("userId") Long userId);

    // Eliminar todos los items del carrito de un usuario
    @Modifying
    @Transactional
    @Query("DELETE FROM CartItemEntity c WHERE c.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    // Eliminar un item específico del carrito
    @Modifying
    @Transactional
    @Query("DELETE FROM CartItemEntity c WHERE c.user.id = :userId AND c.product.productId = :productId")
    void deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
}
