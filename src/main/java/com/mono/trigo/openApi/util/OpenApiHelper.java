package com.mono.trigo.openApi.util;

import com.mono.trigo.openApi.baseDto.WrapperDto;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jakarta.annotation.Nullable;

@Component
public class OpenApiHelper {

    private final String serviceKey;
    private final WebClient webClient;

    public OpenApiHelper(@Value("#{environment['spring.open-api-token']}") String serviceKey) {
        this.serviceKey = serviceKey;
        this.webClient = WebClient.builder().build();
    }

    public <T> List<T> connectOpenApi(
            String endpoint,
            ParameterizedTypeReference<WrapperDto<T>> typeReference,
            @Nullable Map<String, String> parameters) {

        String baseUri = "http://apis.data.go.kr/B551011/KorService1/";
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(baseUri + endpoint)
                .queryParam("serviceKey", serviceKey)
                .queryParam("numOfRows", 100)
                .queryParam("pageNo", 1)
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "testApp")
                .queryParam("_type", "json");

        if (parameters != null) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                uriBuilder.queryParam(entry.getKey(), entry.getValue());
            }
        }

        String fullUri = uriBuilder.build().toString();
        URI uri = URI.create(fullUri);

        WrapperDto<T> response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(typeReference)
                .block();

        if (response != null) {
            return response.getResponse().getBody().getItems().getItem();
        } else {
            throw new RuntimeException("Response is null");
        }
    }
}
