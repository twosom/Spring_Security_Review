package com.icloud.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicApplication.class, args);

        Person person = Person.builder()
                .name("TESTNAME")
                .build();

        System.out.println("person = " + person);
    }
}
