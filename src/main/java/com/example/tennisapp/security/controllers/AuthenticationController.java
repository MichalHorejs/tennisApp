package com.example.tennisapp.security.controllers;

import com.example.tennisapp.security.models.AuthenticationResponse;
import com.example.tennisapp.security.service.AuthenticationService;
import com.example.tennisapp.user.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register (@RequestBody User user){
        return ResponseEntity.ok(authService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login (@RequestBody User user){
        return ResponseEntity.ok(authService.authenticate(user));
    }
}
