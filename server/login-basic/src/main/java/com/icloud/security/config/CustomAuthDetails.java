package com.icloud.security.config;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * {@link HttpServletRequest}를 파라미터로 받아서 {@link RequestInfo} 로 만들어 반환한다.
 */
@Component
public class CustomAuthDetails implements AuthenticationDetailsSource<HttpServletRequest, RequestInfo> {

    @Override
    public RequestInfo buildDetails(HttpServletRequest request) {

        return RequestInfo.builder()
                .remoteIp(request.getRemoteAddr())
                .sessionId(request.getSession().getId())
                .loginTime(LocalDateTime.now())
                .locale(request.getLocale())
                .build();
    }
}
