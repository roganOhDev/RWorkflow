package com.source.rworkflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ClientFactory {
    private static final String BASE_URL = "http://localhost:8080";

    public static WebClient client(final String token) {
        return WebClient.builder()
                .filter(ExchangeFilterFunction.ofResponseProcessor((clientResponse -> {
                    if (clientResponse.statusCode().isError()) {
                        Assertions.fail(clientResponse.bodyToMono(String.class).toString());
                    }
                    return Mono.just(clientResponse);
                })))
                .baseUrl(BASE_URL)
                .defaultHeaders(headers -> {
                    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    headers.set("token", token);
                })
                .build();
    }

    public static JSONObject get(final String uri, final MultiValueMap<String, String> params, final String token) {
        final var client = client(token);

        final var response = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(uri)
                        .queryParams(params)
                        .build())
                .exchangeToMono(rawResponse -> {
                    handleException(rawResponse, uri, HttpMethod.GET.name());

                    return rawResponse.bodyToMono(Object.class);
                })
                .block();

        return getJsonObject(response);
    }

    public static JSONObject post(final String uri, final JSONObject body, final String token) {
        final var client = client(token);

        final var response = client.post()
                .uri(uri)
                .body(BodyInserters.fromValue(body.toString()))
                .exchangeToMono(rawResponse -> {
                    handleException(rawResponse, uri, HttpMethod.POST.name());

                    return rawResponse.bodyToMono(Object.class);
                })
                .block();

        return getJsonObject(response);
    }

    public static JSONObject put(final String uri, final JSONObject body, final String token) {
        final var client = client(token);

        final var response = client.put()
                .uri(uri)
                .body(BodyInserters.fromValue(body.toString()))
                .exchangeToMono(rawResponse -> {
                    handleException(rawResponse, uri, HttpMethod.PUT.name());

                    return rawResponse.bodyToMono(Object.class);
                })
                .block();

        return getJsonObject(response);
    }

    public static JSONObject delete(final String uri, final String token) {
        final var client = client(token);

        final var clientSpec = client.delete()
                .uri(uri);

        final var response = clientSpec
                .exchangeToMono(rawResponse -> {
                    handleException(rawResponse, uri, HttpMethod.DELETE.name());

                    return rawResponse.bodyToMono(Object.class);
                })
                .block();

        return getJsonObject(response);
    }

    private static JSONObject getJsonObject(final Object response) {
        if (response == null) {
            return null;
        }

        try {
            final var mapper = new ObjectMapper();
            final var jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
            System.out.println(jsonInString);

            return new JSONObject(jsonInString);

        } catch (JsonProcessingException | JSONException e) {
            Assertions.fail(e.getMessage() + " (Exception Occur While Change To JsonObject)");
            return null;
        }
    }

    private static void handleException(final ClientResponse rawResponse, final String uri, final String httpMethod) {
        if (rawResponse.statusCode().is4xxClientError()) {
            Assertions.fail(httpMethod + " " + BASE_URL + uri + " failed (api server error)");
        } else if (rawResponse.statusCode().is5xxServerError()) {
            Assertions.fail(httpMethod + " " + BASE_URL + uri + " " + rawResponse.statusCode().value() + " error : " + rawResponse.statusCode().getReasonPhrase());
        }
    }
}
