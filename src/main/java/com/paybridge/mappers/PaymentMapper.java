package com.paybridge.mappers;


public class PaymentMapper {
    String paymentMethodName;
    String bankingEntityName;
    String secureLogoURL;

    public PaymentMapper(String paymentMethodName, String bankingEntityName, String secureLogoURL) {
        this.paymentMethodName = paymentMethodName;
        this.bankingEntityName = bankingEntityName;
        this.secureLogoURL = secureLogoURL;
    }

    // Getters and Setters
    public String getPaymentMethodName() {
        return paymentMethodName;
    }

    public String getBankingEntityName() {
        return bankingEntityName;
    }

    public String getSecureLogoURL() {
        return secureLogoURL;
    }

    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    public void setBankingEntityName(String bankingEntityName) {
        this.bankingEntityName = bankingEntityName;
    }

    public void setSecureLogoURL(String secureLogoURL) {
        this.secureLogoURL = secureLogoURL;
    }
}


