package com.icloud.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.servlet.http.HttpServletRequest;
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
    private final PasswordEncoder passwordEncoder;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
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
                .accessDeniedHandler(customAccessDeniedHandler)
                .authenticationEntryPoint(customAuthenticationEntryPoint)

            .and()
                .rememberMe()
                .rememberMeParameter("remember-me")
                .rememberMeServices(rememberMeService())

            .and()
                .sessionManagement()
                .sessionFixation().changeSessionId()
                .maximumSessions(2)
                .maxSessionsPreventsLogin(true)
                .expiredUrl("/session-expired")
        ;
    }

    @Bean
    public RememberMeServices rememberMeService() {
        PersistentTokenBasedRememberMeServices rememberMeServices =
                new PersistentTokenBasedRememberMeServices("hello", userDetailsService, tokenRepository()){
                    @Override
                    protected Authentication createSuccessfulAuthentication(HttpServletRequest request, UserDetails user) {
                        //== RememberMeServices 에서 createSuccessfulAuthentication 은 RememberMeAuthenticationToken 을 발급해준다.
                        //== 그러나 RememberMeAuthenticationToken 은 ExceptionTranslationFilter 의 handleAccessDeniedException 에서
                        //== this.authenticationTrustResolver.isRememberMe() 메소드에 의해 if 조건에 걸리게 되어, sendStartAuthentication 메소드가
                        //== 최종적으로 실행되게 되어 로그인을 요구하게 된다.==//
                        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), null);
                    }
                };
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/sessions", "/session/expire", "/session-expired")
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations(),
                        PathRequest.toH2Console()
                );
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

    @Bean
    SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
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
