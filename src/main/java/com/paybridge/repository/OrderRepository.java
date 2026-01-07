package com.paybridge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.paybridge.entities.OrdersEntity;
import com.paybridge.entities.OrderIdEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrdersEntity, OrderIdEntity> {
    
    // Buscar todas las órdenes de un usuario
    @Query("SELECT o FROM OrdersEntity o WHERE o.userId = :userId")
    List<OrdersEntity> findByUserId(@Param("userId") Long userId);
    
    // Buscar órdenes por estado
    @Query("SELECT o FROM OrdersEntity o WHERE o.status = :status")
    List<OrdersEntity> findByStatus(@Param("status") String status);
    
    // Buscar órdenes de un usuario por estado
    @Query("SELECT o FROM OrdersEntity o WHERE o.userId = :userId AND o.status = :status")
    List<OrdersEntity> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
    
    // Buscar una orden específica por user_id y orderltem_id
    @Query("SELECT o FROM OrdersEntity o WHERE o.userId = :userId AND o.orderltemId = :orderltemId")
    Optional<OrdersEntity> findByUserIdAndOrderltemId(@Param("userId") Long userId, @Param("orderltemId") Long orderltemId);
    
    // Contar órdenes de un usuario
    @Query("SELECT COUNT(o) FROM OrdersEntity o WHERE o.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    // Calcular el total gastado por un usuario
    @Query("SELECT SUM(o.total) FROM OrdersEntity o WHERE o.userId = :userId")
    Double calculateTotalSpentByUserId(@Param("userId") Long userId);
    
    // Buscar órdenes ordenadas por fecha de creación (más recientes primero)
    @Query("SELECT o FROM OrdersEntity o ORDER BY o.createdAt DESC")
    List<OrdersEntity> findAllOrderByCreatedAtDesc();
    
    // Buscar órdenes de un usuario ordenadas por fecha
    @Query("SELECT o FROM OrdersEntity o WHERE o.userId = :userId ORDER BY o.createdAt DESC")
    List<OrdersEntity> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
    
    // Buscar órdenes por método de pago
    @Query("SELECT o FROM OrdersEntity o WHERE o.paymentMethod = :paymentMethod")
    List<OrdersEntity> findByPaymentMethod(@Param("paymentMethod") String paymentMethod);
}

