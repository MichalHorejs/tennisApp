package com.example.tennisapp.security.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * This class represents a custom handler for access denied exceptions.
 * It implements the AccessDeniedHandler interface from Spring Security.
 * When an AccessDeniedException is thrown, this handler sets the HTTP response status to 403 (Forbidden).
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        response.setStatus(403);
    }
}
