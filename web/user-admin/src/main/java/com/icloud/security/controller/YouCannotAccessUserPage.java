package com.icloud.security.controller;

import org.springframework.security.access.AccessDeniedException;

public class YouCannotAccessUserPage extends AccessDeniedException {

    public YouCannotAccessUserPage(String msg) {
        super(msg);
    }
}
