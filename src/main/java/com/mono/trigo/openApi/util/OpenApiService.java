package com.mono.trigo.openApi.util;

import com.mono.trigo.openApi.baseDto.WrapperDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.List;

@Component
public class OpenApiService {

    private final String serviceKey;
    private final WebClient webClient;

    public OpenApiService(@Value("#{environment['spring.open-api-token']}") String serviceKey) {
        this.serviceKey = serviceKey;
        this.webClient = WebClient.builder().build();
    }

    public <T> List<T> connectOpenApi(String endpoint, Class<T> objectType) {

        ParameterizedTypeReference<WrapperDto<T>> typeReference =
                new ParameterizedTypeReference<>() {};

        String baseUri = "http://apis.data.go.kr/B551011/KorService1/";
        String fullUri = UriComponentsBuilder.fromUriString(baseUri + endpoint)
                .queryParam("serviceKey", serviceKey)
                .queryParam("numOfRows", 20)
                .queryParam("pageNo", 1)
                .queryParam("MobileOS", "ETC")
                .queryParam("MobileApp", "testApp")
                .queryParam("_type", "json")
                .build()
                .toString();

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
