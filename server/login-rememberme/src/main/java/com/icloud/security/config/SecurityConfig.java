package com.icloud.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.servlet.http.HttpSessionEvent;
import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.EventListener;


@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()

            .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/", false)
                .failureUrl("/login-error")

            .and()
                .logout()
                .logoutSuccessUrl("/")

            .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied")

            .and()
                .rememberMe()
                .rememberMeParameter("remember-me")
                .rememberMeServices(rememberMeService())
                ;
    }

    @Bean
    public RememberMeServices rememberMeService() {
        return new PersistentTokenBasedRememberMeServices("hello", userDetailsService, tokenRepository());
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations(), PathRequest.toH2Console());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
        return roleHierarchy;
    }


    /**
     * {@link ServletListenerRegistrationBean} 은 {@link EventListener} 를 등록하기 위한 클래스이다.
     * {@link ServletListenerRegistrationBean} 을 새로 생성해서 반환 할 시에 T에 {@link EventListener} 를 상속받은 클래스를 넣는다.
     */
    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisherServletListenerRegistration() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher() {
            @Override
            public void sessionCreated(HttpSessionEvent event) {
                super.sessionCreated(event);
                log.info("===>> [{}] 세션 생성됨 {}\n", LocalDateTime.now(), event.getSession().getId());
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent event) {
                super.sessionDestroyed(event);
                log.info("===>> [{}] 세션 만료됨 {}\n", LocalDateTime.now(), event.getSession().getId());
            }

            @Override
            public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
                super.sessionIdChanged(event, oldSessionId);
                log.info("===>> [{}] 세션 아이디 변경 {} : {}\n", LocalDateTime.now(), oldSessionId, event.getSession().getId());
            }
        });
    }
}
