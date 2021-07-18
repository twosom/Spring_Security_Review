package com.icloud.security.config;

import org.springframework.stereotype.Component;

/**
 * "@Component 로 등록되있으면 필드에 정의하지 SecurityExpression으로 Bean으로 접근 가능."
 */
@Component
public class NameCheck {

    public boolean check(String name) {
        return name.equals("twosom");
    }

}
