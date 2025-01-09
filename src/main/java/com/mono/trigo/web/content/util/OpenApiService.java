package com.mono.trigo.web.content.util;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OpenApiService {

    private final WebClient webClient;

    public OpenApiService(WebClient webClient) {
        this.webClient = WebClient.builder()
                .baseUrl("http://apis.data.go.kr")
                .build();
    }

    public void connectOpenApi() {

    }
}
