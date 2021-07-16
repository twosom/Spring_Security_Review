package com.icloud.security.test;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.expression.AccessException;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import static org.junit.jupiter.api.Assertions.*;

public class SpELTest {

    public static final String NAME = "홍길동";
    public static final String CHANGED_NAME = "호나우드";

    ExpressionParser parser = new SpelExpressionParser();

    Person person = Person.builder()
            .name(NAME)
            .height(178)
            .build();

    Horse nancy = Horse
            .builder()
            .name("nancy")
            .height(160)
            .build();


    @DisplayName("1. 기본 테스트")
    @Test
    void test_1() {

        assertEquals(NAME, parser.parseExpression("name").getValue(person, String.class));
    }

    @DisplayName("2. 값 변경")
    @Test
    void test_2() {
        parser.parseExpression("name").setValue(person, CHANGED_NAME);
        assertEquals(CHANGED_NAME, parser.parseExpression("name").getValue(person, String.class));

    }


    @DisplayName("3. 메소드 호출")
    @Test
    void test_3() throws Exception {
        assertTrue(parser.parseExpression("over(170)").getValue(person, Boolean.class));
        assertFalse(parser.parseExpression("over(170)").getValue(nancy, Boolean.class));
    }

    @DisplayName("4. Context 테스트")
    @Test
    void test_4() throws Exception {

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanResolver() {
            @Override
            public Object resolve(EvaluationContext context, String beanName) throws AccessException {
                return beanName.equals("person") ? person : nancy;
            }

        });

        ///== EvaluationContext 에서 Root, Variables(#), BeanResolver(@) 등으로 가져올 수 있다. ==//
        context.setRootObject(person);
        context.setVariable("horse", nancy);

        assertTrue(parser.parseExpression("over(170)").getValue(context, Boolean.class));
        assertFalse(parser.parseExpression("#horse.over(170)").getValue(context, Boolean.class));

        assertTrue(parser.parseExpression("@person.over(170)").getValue(context, Boolean.class));
        assertFalse(parser.parseExpression("@nancy.over(170)").getValue(context, Boolean.class));


    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class Person {
        private String name;
        private int height;

        public boolean over(int pivot) {
            return height >= pivot;
        }
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    static class Horse {
        private String name;
        private int height;

        public boolean over(int pivot) {
            return height >= pivot;
        }
    }
}



