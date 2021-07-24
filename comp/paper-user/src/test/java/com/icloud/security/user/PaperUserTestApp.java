package com.icloud.security.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PaperUserTestApp {


    public static void main(String[] args) {
        SpringApplication.run(PaperUserTestApp.class, args);
    }


}
