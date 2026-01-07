package com.paybridge.entities;
import java.io.Serializable;
import java.util.Objects;

/**
 * Clase auxiliar para la clave primaria compuesta de OrdersEntity
 * 
 * ¿Por qué se necesita esta clase?
 * - JPA necesita un objeto que represente la clave compuesta (user_id + orderltem_id)
 * - Esta clase debe implementar Serializable
 * - Debe tener equals() y hashCode() para que JPA pueda comparar claves
 * - Los campos deben coincidir EXACTAMENTE con los campos @Id en OrdersEntity
 */
public class OrderIdEntity implements Serializable {
    
    private Long userId;        // Debe coincidir con el campo @Id en OrdersEntity
    private Long orderltemId;   // Debe coincidir con el campo @Id en OrdersEntity

    // Constructor sin argumentos (requerido por JPA)
    public OrderIdEntity() {
    }

    // Constructor con argumentos
    public OrderIdEntity(Long userId, Long orderltemId) {
        this.userId = userId;
        this.orderltemId = orderltemId;
    }

    // Getters y Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderltemId() {
        return orderltemId;
    }

    public void setOrderltemId(Long orderltemId) {
        this.orderltemId = orderltemId;
    }

    // equals() y hashCode() son OBLIGATORIOS para claves compuestas
    // JPA los usa para comparar y buscar entidades
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderIdEntity that = (OrderIdEntity) o;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(orderltemId, that.orderltemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, orderltemId);
    }

    @Override
    public String toString() {
        return "OrderIdEntity{" +
                "userId=" + userId +
                ", orderltemId=" + orderltemId +
                '}';
    }
}

