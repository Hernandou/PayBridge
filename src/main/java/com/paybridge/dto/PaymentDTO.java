package com.paybridge.dto;

public class PaymentDTO {

    private Long userId;
    private Long shoppingCartId;
    private String preferenceId;
    private String paymentId; // nullable hasta que llegue el webhook
    private String createdAt;
    private String updatedAt;

    public PaymentDTO() {
    }

    public PaymentDTO(Long userId, Long shoppingCartId, String preferenceId, String paymentId,
            String createdAt, String updatedAt) {
        this.userId = userId;
        this.shoppingCartId = shoppingCartId;
        this.preferenceId = preferenceId;
        this.paymentId = paymentId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(Long shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public String getPreferenceId() {
        return preferenceId;
    }

    public void setPreferenceId(String preferenceId) {
        this.preferenceId = preferenceId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
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
        return "PaymentDTO{" +
                "userId=" + userId +
                ", shoppingCartId=" + shoppingCartId +
                ", preferenceId='" + preferenceId + '\'' +
                ", paymentId='" + paymentId + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
