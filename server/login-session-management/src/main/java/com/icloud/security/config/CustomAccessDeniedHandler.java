package com.icloud.security.config;

import com.icloud.security.controller.YouCannotAccessUserPage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        System.out.println("CustomAccessDeniedHandler.handle");
        if (accessDeniedException instanceof YouCannotAccessUserPage) {
            request.getRequestDispatcher("/access-denied").forward(request, response);
        }
        else request.getRequestDispatcher("/access-denied2").forward(request, response);
    }
}
