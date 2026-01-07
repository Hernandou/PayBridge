package com.paybridge.dto;

public class OrderItemDTO {
    
    private Long orderltemId;
    private Long productId;     // ID del producto (en lugar de objeto completo)
    private Integer quantity;
    private Double price;
    private Double subtotal;
    private String createdAt;

    // Constructor sin argumentos
    public OrderItemDTO() {
    }

    // Constructor completo
    public OrderItemDTO(Long orderltemId, Long productId, Integer quantity, Double price, Double subtotal, String createdAt) {
        this.orderltemId = orderltemId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
        this.createdAt = createdAt;
    }

    // Getters y Setters
    public Long getOrderltemId() {
        return orderltemId;
    }

    public void setOrderltemId(Long orderltemId) {
        this.orderltemId = orderltemId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "OrderItemDTO{" +
                "orderltemId=" + orderltemId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", price=" + price +
                ", subtotal=" + subtotal +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}

