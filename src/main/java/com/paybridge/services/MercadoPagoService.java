package com.paybridge.services;
import org.springframework.stereotype.Service;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.paymentmethod.PaymentMethodClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.MPResourceList;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class MercadoPagoService {

    private final Dotenv dotenv = Dotenv.load();

    public String getAllPayMethods() throws MPException, MPApiException {
    try {
        MercadoPagoConfig.setAccessToken(dotenv.get("ACCESS_TOKEN"));
        PaymentMethodClient client = new PaymentMethodClient();
    } catch (MPException e) {
        throw new RuntimeException(e);
    }
    }
    
}
