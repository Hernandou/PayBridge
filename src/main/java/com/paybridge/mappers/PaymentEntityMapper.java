package com.paybridge.mappers;

import com.paybridge.dto.PaymentDTO;
import com.paybridge.entities.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentEntityMapper {

    public PaymentDTO mapEntityToDTO(PaymentEntity entity) {
        PaymentDTO dto = new PaymentDTO();
        dto.setUserId(entity.getId().getUserId());
        dto.setShoppingCartId(entity.getId().getShoppingCartId());
        dto.setPreferenceId(entity.getId().getPreferenceId());
        dto.setPaymentId(entity.getPaymentId());
        dto.setCreatedAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null);
        dto.setUpdatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null);
        return dto;
    }
}
