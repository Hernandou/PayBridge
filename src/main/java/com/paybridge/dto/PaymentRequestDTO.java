package com.paybridge.dto;

import java.math.BigDecimal;

public class PaymentRequestDTO {
    private Long shoppingCartId;
    private String token; // Este es el Token seguro de la tarjeta
    private BigDecimal transactionAmount; // El total a cobrar exacto
    private Integer installments; // Cantidad de cuotas
    private String paymentMethodId; // "visa", "master", etc.
    private String payerEmail; // Email del cliente
    
    public PaymentRequestDTO() {
    }

    public PaymentRequestDTO(Long shoppingCartId, String token, BigDecimal transactionAmount, Integer installments, String paymentMethodId, String payerEmail) {
        this.shoppingCartId = shoppingCartId;
        this.token = token;
        this.transactionAmount = transactionAmount;
        this.installments = installments;
        this.paymentMethodId = paymentMethodId;
        this.payerEmail = payerEmail;
    }

    public Long getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(Long shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Integer getInstallments() {
        return installments;
    }

    public void setInstallments(Integer installments) {
        this.installments = installments;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    @Override
    public String toString() {
        return "PaymentRequestDTO{" +
                "shoppingCartId=" + shoppingCartId +
                ", token='" + token + '\'' +
                ", transactionAmount=" + transactionAmount +
                ", installments=" + installments +
                ", paymentMethodId='" + paymentMethodId + '\'' +
                ", payerEmail='" + payerEmail + '\'' +
                '}';
    }
}
