package com.example.tennisapp.security.controllers;

import com.example.tennisapp.security.models.AuthenticationResponse;
import com.example.tennisapp.security.service.AuthenticationService;
import com.example.tennisapp.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class represents the controller for authentication-related requests.
 * It contains methods for registering a new user, logging in, and refreshing the authentication token.
 */
@RestController
@RequestMapping(path = "api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;


    /**
     * This method handles the registration of a new user.
     * It receives a User object as a request body and passes it
     * to the AuthenticationService for registration.
     * @param user The User object to register.
     * @return A ResponseEntity containing an AuthenticationResponse object.
     */
    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> register (@RequestBody User user){
        return ResponseEntity.ok(authService.register(user));
    }

    /**
     * This method handles the login of a user.
     * It receives a User object as a request body and passes it to the AuthenticationService for authentication.
     * @param user The User object to authenticate.
     * @return A ResponseEntity containing an AuthenticationResponse object.
     */
    @PostMapping("login")
    public ResponseEntity<AuthenticationResponse> login (@RequestBody User user){
        return ResponseEntity.ok(authService.authenticate(user));
    }

    /**
     * This method handles the refreshing of an authentication token.
     * It receives the HttpServletRequest and HttpServletResponse objects and passes
     * them to the AuthenticationService for token refreshing.
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return A ResponseEntity object.
     */
    @PostMapping("refresh_token")
    public ResponseEntity<?> refreshToken (
            HttpServletRequest request,
            HttpServletResponse response
    ){
        return authService.refreshToken(request, response);
    }
}
