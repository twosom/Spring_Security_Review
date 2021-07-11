package com.icloud.security.teacher;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class TeacherAuthenticationProvider implements AuthenticationProvider, InitializingBean {

    private Map<String, Teacher> studentDB = new HashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        Teacher teacher = null;
        if (authentication instanceof TeacherAuthenticationToken) {
            TeacherAuthenticationToken token = (TeacherAuthenticationToken) authentication;
            if (studentDB.containsKey(token.getCredentials())) {
                teacher = studentDB.get(token.getCredentials());
            }
        } else {
            String key = String.valueOf(authentication.getPrincipal());
            if (studentDB.containsKey(key)) {
                teacher = studentDB.get(key);
            }
        }
        return TeacherAuthenticationToken.builder()
                .principal(teacher)
                .details(teacher.getUsername())
                .authenticated(true)
                .authorities(teacher.getRole())
                .build();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TeacherAuthenticationToken.class.isAssignableFrom(authentication)
                || UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set.of(
                new Teacher("choi", "최선생", Set.of(new SimpleGrantedAuthority("ROLE_TEACHER")), null)
        ).forEach(s -> {
            studentDB.put(s.getId(), s);
        });
    }
}
