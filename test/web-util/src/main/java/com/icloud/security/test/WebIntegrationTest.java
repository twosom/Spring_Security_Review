package com.icloud.security.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebIntegrationTest {

    @LocalServerPort
    int port;

    public URI uri(String path) {
        try {
            return new URI(String.format("http://localhost:%d%s", port, path));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
