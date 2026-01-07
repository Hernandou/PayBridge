package com.paybridge.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@IdClass(OrderIdEntity.class) // Clase auxiliar para clave primaria compuesta
public class OrdersEntity {

    // Clave primaria compuesta: user_id + orderltem_id
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "orderltem_id")
    private Long orderltemId;

    // Relación ManyToOne con User (opcional, si quieres la relación JPA)
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UsersEntity user;

    // Relación ManyToOne con OrderItem (opcional, si quieres la relación JPA)
    @ManyToOne
    @JoinColumn(name = "orderltem_id", insertable = false, updatable = false)
    private OrderItemEntity orderItem;

    @Column(name = "total")
    private Double total;
    
    @Column(name = "status")
    private String status;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
