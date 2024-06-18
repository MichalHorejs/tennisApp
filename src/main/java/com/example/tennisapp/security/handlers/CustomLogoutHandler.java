package com.example.tennisapp.security.handlers;

import com.example.tennisapp.security.daos.TokenDao;
import com.example.tennisapp.security.models.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

/**
 * This class represents a custom handler for logout operations.
 * It implements the LogoutHandler interface from Spring Security.
 * When a logout request is received, this handler invalidates the active token.
 */
@RequiredArgsConstructor
@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenDao tokenDao;

    /**
     * This method handles a logout request.
     * It retrieves the active token from the Authorization header and invalidates it.
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @param authentication The Authentication object.
     */
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String token = authHeader.substring(7);

        // Invalidate active token
        Token activeToken = tokenDao.findByAccessToken(token)
                .orElse(null);

        if (activeToken != null) {
            activeToken.setLoggedOut(true);
            tokenDao.update(activeToken);
        }

    }
}
