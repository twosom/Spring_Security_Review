package com.icloud.security.config;

import org.springframework.stereotype.Component;

@Component
public class NameCheck {

    public boolean check(String name) {
        return name.equals("twosom");
    }
}
