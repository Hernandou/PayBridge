package com.paybridge.mappers;

import java.math.BigDecimal;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.paybridge.dto.CartItemDTO;
import org.springframework.stereotype.Component;

@Component
public class PreferenceItemsRequestMapper {

    public PreferenceItemRequest mapToPreferenceItemRequest(CartItemDTO cartItem) {
        return PreferenceItemRequest.builder()
                .title(cartItem.getProductName())
                .quantity(cartItem.getQuantity())
                .unitPrice(new BigDecimal(cartItem.getPrice()))
                .build();
    }

}
