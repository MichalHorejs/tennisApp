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

@RestController
@RequestMapping(path = "api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;


    @PostMapping("register")
    public ResponseEntity<AuthenticationResponse> register (@RequestBody User user){
        return ResponseEntity.ok(authService.register(user));
    }

    @PostMapping("login")
    public ResponseEntity<AuthenticationResponse> login (@RequestBody User user){
        return ResponseEntity.ok(authService.authenticate(user));
    }

    @PostMapping("refresh_token")
    public ResponseEntity refreshToken (
            HttpServletRequest request,
            HttpServletResponse response
    ){
        return authService.refreshToken(request, response);
    }
}
