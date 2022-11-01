package com.source.rworkflow;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class RestTestFactory {
    private static final String BASE_URL = "http://localhost:8080";

    public static WebClient client(final String token) {
        return WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeaders(headers -> {
                    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    headers.set("token", token);
                })
                .build();
    }
}
