package com.paybridge.dto;

public class OrderDTO {
    
    private Long userId;        // Parte de la clave compuesta
    private Long orderltemId;   // Parte de la clave compuesta
    private Double total;
    private String status;
    private String paymentMethod;
    private String shippingAddress;
    private String createdAt;
    private String updatedAt;

    // Constructor sin argumentos
    public OrderDTO() {
    }

    // Constructor completo
    public OrderDTO(Long userId, Long orderltemId, Double total, String status, String paymentMethod, String shippingAddress, String createdAt, String updatedAt) {
        this.userId = userId;
        this.orderltemId = orderltemId;
        this.total = total;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "userId=" + userId +
                ", orderltemId=" + orderltemId +
                ", total=" + total +
                ", status='" + status + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}

