package com.icloud.security.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class SecurityMessageService {

    @PreAuthorize("hasRole('ADMIN')")
    public String message(String name) {
        return name;
    }
}

