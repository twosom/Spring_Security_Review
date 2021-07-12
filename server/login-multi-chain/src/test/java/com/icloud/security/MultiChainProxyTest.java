package com.icloud.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icloud.security.student.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MultiChainProxyTest {

    @LocalServerPort
    int port;


    RestTemplate client = new RestTemplate();

    @DisplayName("1. choi:1 로 로그인 해서 학생 리스트 내려받기")
    @Test
    void test_1() throws Exception {

        String url = String.format("http://localhost:%d/api/teacher/students", port);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString("choi:1".getBytes()));
        HttpEntity<Object> httpEntity = new HttpEntity<>("", httpHeaders);

        ResponseEntity<List<Student>> response = client.exchange(url, GET, httpEntity, new ParameterizedTypeReference<List<Student>>() {});
        assertNotNull(response.getBody());
        assertEquals(response.getBody().size(), 3);
    }

}
