package com.icloud.security.config;

import com.icloud.security.student.StudentAuthenticationToken;
import com.icloud.security.teacher.TeacherAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    public static final String TEACHER = "teacher";
    public static final String STUDENT = "student";

    public CustomLoginFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        String username = obtainUsername(request);
        username = (username != null) ? username : "";
        username = username.trim();
        String password = obtainPassword(request);
        password = (password != null) ? password : "";
        String type = request.getParameter("type");
        UsernamePasswordAuthenticationToken authRequest;

        switch (type) {
            case TEACHER:
                TeacherAuthenticationToken teacherToken = TeacherAuthenticationToken
                        .builder()
                        .credentials(username)
                        .build();
                return this.getAuthenticationManager().authenticate(teacherToken);
            case STUDENT:
                StudentAuthenticationToken studentToken = StudentAuthenticationToken.builder()
                        .credentials(username)
                        .build();
                return this.getAuthenticationManager().authenticate(studentToken);
            default:
                return null;
        }
    }

    
}
