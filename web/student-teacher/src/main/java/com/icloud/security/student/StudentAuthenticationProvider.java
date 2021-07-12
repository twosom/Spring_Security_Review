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

        //== *중요) 내가 핸들할 수 없는 토큰은 반드시 null 로 넘겨야 한다.
        //    그래야 그 다음 AuthenticationProvider 에게 authenticate 시도를 할 수 있게 한다. ==//
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            if (studentDB.containsKey(token.getName())) {
                return getAuthenticationToken(token.getName());
            }
        } else {
            StudentAuthenticationToken token = (StudentAuthenticationToken) authentication;
            if (studentDB.containsKey(token.getCredentials())) {
                return getAuthenticationToken(token.getCredentials());
            }
        }
        return null;
    }

    private StudentAuthenticationToken getAuthenticationToken(String name) {
        Student student = studentDB.get(name);
        return StudentAuthenticationToken.builder()
                .principal(student)
                .details(student.getUsername())
                .authenticated(true)
                .authorities(student.getRole())
                .build();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return StudentAuthenticationToken.class.isAssignableFrom(authentication) ||
                UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }


    public List<Student> myStudents(String teacherId) {
        return studentDB.values()
                .stream().filter(s -> s.getTeacherId().equals(teacherId))
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
