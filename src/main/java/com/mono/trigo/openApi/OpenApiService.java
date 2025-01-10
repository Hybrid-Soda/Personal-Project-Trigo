package com.mono.trigo.openApi;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

@Component
public class OpenApiService {

    private final String serviceKey;
    private final WebClient webClient;

    public OpenApiService(@Value("#{environment['spring.open-api-token']}") String serviceKey) {
        this.serviceKey = serviceKey;
        this.webClient = WebClient.builder().build();
    }

    public String connectOpenApi(String endpoint) {

        String fullUri = UriComponentsBuilder.fromUriString(endpoint)
                .queryParam("serviceKey", serviceKey)
                .queryParam("numOfRows", 20)
                .queryParam("pageNo", 1)
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "testApp")
                .queryParam("_type", "json")
                .build()
                .toString();

        URI uri = URI.create(fullUri);

        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
