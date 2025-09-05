package com.bank.account_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${client.service.url}")
    private String clientServiceUrl;

    @Bean
    public WebClient clientServiceWebClient() {
        return WebClient.builder()
                .baseUrl(clientServiceUrl)
                .build();
    }
}
