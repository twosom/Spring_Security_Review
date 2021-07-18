package com.icloud.security.config;

import com.icloud.security.service.Paper;
import com.icloud.security.service.PaperService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final PaperService paperService;

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Object targetDomainObject,
                                 Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {
        Paper paper = paperService.getPaper((Long) targetId);
        if (paper == null) throw new AccessDeniedException("시험지가 존재하지 않습니다.");
        if (paper.getState() == Paper.State.PREPARE) return false;

        String username = authentication.getName();

        return paper.getStudentIds()
                .stream()
                .anyMatch(userId -> userId.equals(username));
    }
}
