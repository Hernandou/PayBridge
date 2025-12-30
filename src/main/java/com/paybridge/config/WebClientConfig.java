package com.paybridge.config;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebClientConfig {

    static String baseUrl = "https://api.mercadopago.com/v1";



}
