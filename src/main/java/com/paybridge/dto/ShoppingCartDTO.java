package com.paybridge.dto;

public class ShoppingCartDTO {
    
    private String id;
    private String name;
    private String description;
    private String price;
    private String quantity;
    private String total;
    private String createdAt;
    private String updatedAt;
    private String status;


    public ShoppingCartDTO(String id, String name, String description, String price, String quantity, String total, String createdAt, String updatedAt, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getTotal() {
        return total;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ShoppingCartDTO [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price + ", quantity=" + quantity + ", total=" + total + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", status=" + status + "]";
    }

}
