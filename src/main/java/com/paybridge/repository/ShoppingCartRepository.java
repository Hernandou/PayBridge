package com.paybridge.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.paybridge.entities.ShoppingCartEntity;
import org.springframework.data.jpa.repository.Query;
@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCartEntity, Long> {

    // El método save() ya viene de JpaRepository, no necesita @Query
    
    @Query("SELECT s FROM ShoppingCartEntity s WHERE s.id = :id")
    ShoppingCartEntity getShoppingCartById(Long id);

    @Query("SELECT s FROM ShoppingCartEntity s WHERE s.name = :name")
    ShoppingCartEntity getShoppingCartByName(String name);

    @Query("SELECT s FROM ShoppingCartEntity s WHERE s.description = :description")
    ShoppingCartEntity getShoppingCartByDescription(String description);
}