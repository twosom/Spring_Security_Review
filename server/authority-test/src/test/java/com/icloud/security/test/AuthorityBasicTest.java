package com.icloud.security.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

public class AuthorityBasicTest extends WebIntegrationTest {

    TestRestTemplate client;


    @DisplayName("1. greeting 메시지를 불러온다.")
    @Test
    void test_1() {
        client = new TestRestTemplate("user1", "1111");
        ResponseEntity<String> response = client.getForEntity(uri("/greeting/twosom"), String.class);

        Assertions.assertEquals("hello twosom", response.getBody());
    }

}
