package com.icloud.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icloud.security.student.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MultiChainProxyTest {

    @LocalServerPort
    int port;

    RestTemplate restTemplate = new RestTemplate();

    @DisplayName("1. 학생조사")
    @Test
    void test_1() throws Exception {
        String url = String.format("http://localhost:%d/api/teacher/students", port);
        System.out.println(url);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString("choi:1".getBytes()));
        HttpEntity<Object> httpEntity = new HttpEntity<>("", httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(url, GET, httpEntity, String.class);

        List<Student> list = new ObjectMapper().readValue(response.getBody(), new TypeReference<List<Student>>() {});

        Assertions.assertEquals(list.size(), 3);
        System.out.println("list = " + list);
    }



}
