package com.paybridge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.paybridge.entities.ShoppingCartEntity;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCartEntity, Long> {

    @Query("SELECT s FROM ShoppingCartEntity s WHERE s.id = :id")
    ShoppingCartEntity getShoppingCartById(Long id);

    @Query("SELECT s FROM ShoppingCartEntity s WHERE s.user.userId = :userId ORDER BY s.createdAt DESC")
    List<ShoppingCartEntity> findByUserId(Long userId);
}
