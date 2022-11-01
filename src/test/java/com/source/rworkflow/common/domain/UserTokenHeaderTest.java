package com.source.rworkflow.common.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.source.rworkflow.Const;
import com.source.rworkflow.RestTestFactory;
import com.source.rworkflow.TestCase;
import jdk.jfr.Description;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;

class UserTokenHeaderTest extends TestCase {
    private final static String BASE_URL = "http://localhost:8080";
//    private final static String CLASS_URL = "/"

    @Disabled
    @Description("유저 토큰 테스트")
    @TestFactory
    Collection<DynamicNode> token_test(){
        return group(
                single("a", () -> {
                    final var webClient = RestTestFactory.client(Const.User.Admin.token);

                    final var body = new JSONObject();

                    final var response = webClient.post()
                            .uri("/workflow-rule")
                            .body(BodyInserters.fromValue(body.toString()))
                            .exchangeToMono(r -> {
                                handleException(r, "/workflow-rule", HttpMethod.PUT.name());

                                return r.bodyToMono(Object.class);
                            })
                            .block();

                    final var rr = getJsonObject(response);
                    System.out.println("fin");
                })
        );
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