package com.paybridge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.paybridge.entities.UsersEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UsersEntity, Long> {
    
    // Buscar por email (método automático de Spring Data JPA)
    Optional<UsersEntity> findByEmail(String email);
    
    // Verificar si existe un usuario con ese email
    boolean existsByEmail(String email);
    
    // Buscar por nombre
    @Query("SELECT u FROM UsersEntity u WHERE u.name = :name")
    Optional<UsersEntity> findByName(String name);
    
    // Buscar usuarios por nombre que contenga un texto
    @Query("SELECT u FROM UsersEntity u WHERE u.name LIKE %:name%")
    java.util.List<UsersEntity> findByNameContaining(String name);
}

