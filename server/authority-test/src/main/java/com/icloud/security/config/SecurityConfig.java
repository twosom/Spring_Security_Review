package com.icloud.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final NameCheck nameCheck;

    @Bean
    AccessDecisionManager filterAccessDecisionManager() {
        return new AccessDecisionManager() {
            @Override
            public void decide(Authentication authentication,
                               Object object,
                               Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
                throw new AccessDeniedException("접근 금지");
//                return;
            }

            @Override
            public boolean supports(ConfigAttribute attribute) {
                return true;
            }

            @Override
            public boolean supports(Class<?> clazz) {
                return FilterInvocation.class.isAssignableFrom(clazz);
            }
        };
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.httpBasic();

        http.authorizeRequests()
                .mvcMatchers("/greeting/{name}").access("@nameCheck.check(#name)")
                .anyRequest().authenticated()
//                .accessDecisionManager(filterAccessDecisionManager())
                ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(
                        User.withDefaultPasswordEncoder()
                                .username("user1")
                                .password("1111")
                                .roles("USER", "STUDENT"))
                .withUser(
                        User.withDefaultPasswordEncoder()
                                .username("user2")
                                .password("1111")
                                .roles("USER", "STUDENT"))
                .withUser(
                        User.withDefaultPasswordEncoder()
                                .username("tutor1")
                                .password("1111")
                                .roles("USER", "TUTOR"));

    }
}
