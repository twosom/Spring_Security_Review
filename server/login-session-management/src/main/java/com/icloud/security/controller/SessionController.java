package com.icloud.security.controller;

import com.icloud.security.user.domain.SpUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class SessionController {

    private final SessionRegistry sessionRegistry;


    @GetMapping("/sessions")
    public String sessions(Model model) {
        List<UserSession> sessionList = sessionRegistry.getAllPrincipals()
                .stream()
                .map(this::convertToUserSession)
                .collect(Collectors.toList());

        model.addAttribute("sessionList", sessionList);
        return "sessionList";
    }


    @PostMapping("/session/expire")
    public String expireSession(@RequestParam String sessionId) {
        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionId);
        if (!sessionInformation.isExpired()) sessionInformation.expireNow();

        return "redirect:/sessions";
    }


    @GetMapping("/session-expired")
    public String sessionExpired() {
        return "sessionExpired";
    }


    private UserSession convertToUserSession(Object principal) {
        return UserSession.builder()
                .username(((SpUser) principal).getUsername())
                .sessions(sessionRegistry.getAllSessions(principal, false)
                        .stream()
                        .map(this::convertToSessionInfo)
                        .collect(Collectors.toList())
                ).build();
    }

    private SessionInfo convertToSessionInfo(SessionInformation s) {
        return SessionInfo.builder()
                .sessionId(s.getSessionId())
                .time(s.getLastRequest())
                .build();
    }

}
