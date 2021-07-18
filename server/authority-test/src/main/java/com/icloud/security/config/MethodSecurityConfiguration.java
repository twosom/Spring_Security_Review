package com.icloud.security.config;

import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    private final CustomPermissionEvaluator permissionEvaluator;


    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler() {
            @Override
            protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication,
                                                                                      MethodInvocation invocation) {
                CustomMethodSecurityExpressionRoot methodSecurityExpressionRoot = new CustomMethodSecurityExpressionRoot(authentication, invocation);
                methodSecurityExpressionRoot.setPermissionEvaluator(getPermissionEvaluator());
                return methodSecurityExpressionRoot;
            }

        };

        handler.setPermissionEvaluator(permissionEvaluator);

        return handler;
    }

    @Override
    protected AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
        ExpressionBasedPreInvocationAdvice expressionAdvice = new ExpressionBasedPreInvocationAdvice();
        expressionAdvice.setExpressionHandler(getExpressionHandler());

        decisionVoters.add(new PreInvocationAuthorizationAdviceVoter(expressionAdvice));
        decisionVoters.add(new RoleVoter());
        decisionVoters.add(new AuthenticatedVoter());
//        decisionVoters.add(new CustomVoter());

        return new AffirmativeBased(decisionVoters);
//        ConsensusBased consensusBased = new ConsensusBased(decisionVoters);
//        consensusBased.setAllowIfEqualGrantedDeniedDecisions(false);
//        return consensusBased;
    }
}
