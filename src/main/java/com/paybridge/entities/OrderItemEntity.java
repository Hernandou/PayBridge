package com.paybridge.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_item")
public class OrderItemEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Orderltem_id")
    private Long orderltemId;

    // Clave foránea a Product usando @ManyToOne y @JoinColumn
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductsEntity product;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Double price;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
