package com.example.tennisapp.security.handlers;

import com.example.tennisapp.security.daos.TokenDao;
import com.example.tennisapp.security.models.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenDao tokenDao;

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
        Token activeToken = tokenDao.findByToken(token)
                .orElse(null);

        if (activeToken != null) {
            activeToken.setLoggedOut(true);
            tokenDao.update(activeToken);
        }

    }
}
