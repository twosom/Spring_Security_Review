package com.icloud.security.config;


import com.icloud.security.student.StudentAuthenticationProvider;
import com.icloud.security.teacher.TeacherAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Order(2)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final StudentAuthenticationProvider studentAuthenticationProvider;
    private final TeacherAuthenticationProvider teacherAuthenticationProvider;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(studentAuthenticationProvider);
        auth.authenticationProvider(teacherAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CustomLoginFilter customLoginFilter = new CustomLoginFilter(authenticationManager());
        customLoginFilter.setAuthenticationFailureHandler((request, response, exception) -> {
            response.sendRedirect("/login-error");
        });

        http.authorizeRequests()
                .antMatchers("/", "/login", "/login-error").permitAll()
                .anyRequest().authenticated()

//            .and()
//                .formLogin()
//                .loginPage("/login")
//                .defaultSuccessUrl("/", false)
//                .failureUrl("/login-error")
//                .permitAll()

        .and()
                .logout()
                .logoutSuccessUrl("/")

        .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied")
        .and()
                .addFilterAt(customLoginFilter, UsernamePasswordAuthenticationFilter.class)
        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_TEACHER > ROLE_STUDENT");
        return roleHierarchy;
    }
}
