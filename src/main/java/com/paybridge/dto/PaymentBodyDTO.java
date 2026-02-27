package com.paybridge.dto;

public class PaymentBodyDTO {

    private Long shoppingCartId;
    private String paymentToken;
    private String cardType;

    public PaymentBodyDTO() {
    }

    public PaymentBodyDTO(Long shoppingCartId, String paymentToken, String cardType) {
        this.shoppingCartId = shoppingCartId;
        this.paymentToken = paymentToken;
        this.cardType = cardType;
    }

    public Long getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(Long shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public String getPaymentToken() {
        return paymentToken;
    }

    public void setPaymentToken(String paymentToken) {
        this.paymentToken = paymentToken;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    @Override
    public String toString() {
        return "PaymentBodyDTO{" +
                "shoppingCartId=" + shoppingCartId +
                ", paymentToken=" + paymentToken +
                ", cardType='" + cardType + '\'' +
                '}';
    }

}
