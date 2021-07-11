package com.icloud.security.student;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StudentAuthenticationProvider implements AuthenticationProvider, InitializingBean {

    private Map<String, Student> studentDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        StudentAuthenticationToken token = (StudentAuthenticationToken) authentication;
        if (studentDB.containsKey(token.getCredentials())) {
            Student student = studentDB.get(token.getCredentials());
            return StudentAuthenticationToken.builder()
                    .principal(student)
                    .details(student.getUsername())
                    .authenticated(true)
                    .authorities(student.getRole())
                    .build();
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return StudentAuthenticationToken.class.isAssignableFrom(authentication)
                || UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public List<Student> myStudentList(String teacherId) {
        return this.studentDB
                .values()
                .stream()
                .filter(s -> s.getTeacherId().equals(teacherId))
                .collect(Collectors.toList());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set.of(
                new Student("hong", "홍길동", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")), "choi"),
                new Student("kang", "강아지", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")), "choi"),
                new Student("rang", "호랑이", Set.of(new SimpleGrantedAuthority("ROLE_STUDENT")), "choi")
        ).forEach(s -> {
            studentDB.put(s.getId(), s);
        });
    }
}
