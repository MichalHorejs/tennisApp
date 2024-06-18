package com.example.tennisapp.security.filters;

import com.example.tennisapp.security.service.JwtService;
import com.example.tennisapp.security.service.UserDetailsServiceImp;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This class represents a filter for JWT (JSON Web Token) authentication.
 * It intercepts every HTTP request and checks if it contains a valid JWT in the Authorization header.
 */
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImp userDetailsService;

    /**
     * This method intercepts every HTTP request and checks if it contains a valid
     * JWT in the Authorization header.
     * If the JWT is valid, it sets the authentication in the security context.
     * If the JWT is not valid or not present, it forwards the request to the next filter in the chain.
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @param filterChain The FilterChain object.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Get the Authorization header from the request
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String phoneNumber = jwtService.extractPhoneNumber(token);

        // Check if the phone number is not null and the user is not already authenticated
        if(phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNumber);

            // Check if the token is valid
            if(jwtService.isValid(token, userDetails)){

                // Set the authentication in the security context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
