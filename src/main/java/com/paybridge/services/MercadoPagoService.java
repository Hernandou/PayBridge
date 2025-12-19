package com.paybridge.services;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.paybridge.config.WebClientConfig;
import io.github.cdimascio.dotenv.Dotenv;
import com.mercadopago.sdk.MercadoPagoConfig;

@Service
public class MercadoPagoService {

    private final Dotenv dotenv = Dotenv.load();
    private final WebClient webClient = WebClientConfig.webClient();

    public String getAllPayMethods() {
        MercadoPagoConfig.setAccessToken(dotenv.get("ACCESS_TOKEN"));

        
        return webClient.get()
            .uri("/payment_methods")
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
    
}
