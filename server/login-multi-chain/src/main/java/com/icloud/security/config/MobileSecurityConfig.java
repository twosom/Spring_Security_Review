package com.icloud.security.config;


import com.icloud.security.student.StudentAuthenticationProvider;
import com.icloud.security.teacher.TeacherAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * {@link MobileSecurityConfig} 에서는 {@link EnableWebSecurity} 와
 * {@link EnableGlobalMethodSecurity} 가 중복 선언 되므로,
 * {@link Configuration} 어노테이션만 붙여주도록 한다.
 */
@Order(1)
@Configuration
@RequiredArgsConstructor
public class MobileSecurityConfig extends WebSecurityConfigurerAdapter {

    private final StudentAuthenticationProvider studentAuthenticationProvider;
    private final TeacherAuthenticationProvider teacherAuthenticationProvider;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(teacherAuthenticationProvider);
        auth.authenticationProvider(studentAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.antMatcher("/api/**")
                .authorizeRequests()
                .anyRequest().authenticated()
        .and()
            .httpBasic()
        ;
    }
}
