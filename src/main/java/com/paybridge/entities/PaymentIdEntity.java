package com.paybridge.entities;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PaymentIdEntity implements Serializable {

    private Long userId;
    private Long shoppingCartId;
    private String preferenceId;

    public PaymentIdEntity() {
    }

    public PaymentIdEntity(Long userId, Long shoppingCartId, String preferenceId) {
        this.userId = userId;
        this.shoppingCartId = shoppingCartId;
        this.preferenceId = preferenceId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PaymentIdEntity))
            return false;
        PaymentIdEntity that = (PaymentIdEntity) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(shoppingCartId, that.shoppingCartId) &&
                Objects.equals(preferenceId, that.preferenceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, shoppingCartId, preferenceId);
    }
}
