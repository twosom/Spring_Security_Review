package com.icloud.security.config;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;

@EnableCaching
@EnableAutoConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class AclGlobalMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    private final DataSource dataSource;




    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(aclPermissionEvaluator());

        return expressionHandler;
    }

    @Bean
    public PermissionEvaluator aclPermissionEvaluator() {
        return new AclPermissionEvaluator(aclService());
    }

    @Bean
    public AclService aclService() {
        return new JdbcMutableAclService(dataSource, lookupStrategy(), aclCache());
    }

    @Bean
    public LookupStrategy lookupStrategy() {
        return new BasicLookupStrategy(
                dataSource,
                aclCache(),
                aclAuthorizationStrategy(),
                auditLogger()
        );
    }

    @Bean
    public AclCache aclCache() {
        return new EhCacheBasedAclCache(
                aclEhCacheFactoryBean().getObject(),
                permissionGrantedStrategy(),
                aclAuthorizationStrategy()
        );
    }

    @Bean
    public EhCacheCacheManager cacheManager() {
        EhCacheCacheManager cacheManager = new EhCacheCacheManager();
        cacheManager.setCacheManager(ehCacheManagerFactoryBean().getObject());
        return cacheManager;
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        return new EhCacheManagerFactoryBean();
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantedStrategy() {
        return new DefaultPermissionGrantingStrategy(auditLogger());
    }

    @Bean
    public EhCacheFactoryBean aclEhCacheFactoryBean() {
        EhCacheFactoryBean ehCacheFactoryBean = new EhCacheFactoryBean();
        ehCacheFactoryBean.setCacheManager(ehCacheManagerFactoryBean().getObject());
        ehCacheFactoryBean.setCacheName("aclCache");
        return ehCacheFactoryBean;
    }

    @Bean
    public ConsoleAuditLogger auditLogger() {
        return new ConsoleAuditLogger();
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

}
