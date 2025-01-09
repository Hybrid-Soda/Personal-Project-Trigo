package com.mono.trigo.openApi;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OpenApiService {

    private final String serviceKey;
    private final WebClient webClient;

    public OpenApiService(@Value("#{environment['spring.open-api-token']}") String serviceKey) {
        this.serviceKey = serviceKey;
        this.webClient = WebClient.builder()
                .baseUrl("http://apis.data.go.kr/B551011/KorService1")
                .build();
    }

    public String connectOpenApi(String endpoint) {

        String uri = UriComponentsBuilder.fromPath(endpoint)
                .queryParam("serviceKey", serviceKey)
                .queryParam("numOfRows", 10)
                .queryParam("pageNo", 1)
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "testApp")
                .queryParam("_type", "json")
                .build()
                .toString();

        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
