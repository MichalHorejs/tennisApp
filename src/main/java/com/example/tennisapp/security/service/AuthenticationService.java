package com.example.tennisapp.security.service;

import com.example.tennisapp.security.models.AuthenticationResponse;
import com.example.tennisapp.models.User;
import com.example.tennisapp.daos.UserDao;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(User request){
        User user = new User();
        user.setPhoneNumber(request.getPhoneNumber());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userDao.save(user);

        return new AuthenticationResponse(jwtService.generateToken(user));
    }

    public AuthenticationResponse authenticate(User request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getPhoneNumber(),
                        request.getPassword()
                )
        );

        User user = userDao.getUserByPhoneNumber(request.getPhoneNumber());
        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }



}
