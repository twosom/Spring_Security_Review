package com.icloud.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BasicAuthenticationTest {

    @LocalServerPort
    int port;

    RestTemplate client = new RestTemplate();

    private String greetingUrl() {
        return "http://localhost:" + port + "/greeting";
    }

    @DisplayName("1. 인증 실패")
    @Test
    void test_1() throws Exception {

        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
            client.getForObject(greetingUrl(), String.class);
        });
        assertEquals(401, exception.getRawStatusCode());

    }

    @DisplayName("2. 인증 성공")
    @Test
    void test_2() throws Exception {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString("user1:1111".getBytes()));

        HttpEntity<Object> httpEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> response = client.exchange(greetingUrl(), GET, httpEntity, String.class);

        assertEquals("hello", response.getBody());

    }

    @DisplayName("3. 인증성공2")
    @Test
    void test_3() throws Exception {
        //== TestRestClient 는 기본적으로 Basic 토큰을 지원한다. ==//
        TestRestTemplate testClient = new TestRestTemplate("user1", "1111");
        String response = testClient.getForObject(greetingUrl(), String.class);
        assertEquals("hello", response);

    }

    @DisplayName("4. POST 인증")
    @Test
    void test_4() throws Exception {

        TestRestTemplate testClient = new TestRestTemplate("user1", "1111");
        ResponseEntity<String> response = testClient.postForEntity(greetingUrl(), "twosom", String.class);
        assertEquals("hello twosom", response.getBody());

    }




}
