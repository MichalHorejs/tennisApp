package com.example.tennisapp.security.service;

import com.example.tennisapp.daos.UserDao;
import com.example.tennisapp.enums.Role;
import com.example.tennisapp.exceptions.BadRequestException;
import com.example.tennisapp.models.User;
import com.example.tennisapp.security.daos.TokenDao;
import com.example.tennisapp.security.models.AuthenticationResponse;
import com.example.tennisapp.security.models.Token;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenDao tokenDao;

    public AuthenticationResponse register(User request){
        if (request.getRole().equals(Role.ADMIN)){
            throw new BadRequestException("Admin cannot be registered");
        }
        User user = new User();
        user.setPhoneNumber(request.getPhoneNumber());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        // save new User
        userDao.save(user);

        String jwt = jwtService.generateToken(user);

        saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt);
    }

    public AuthenticationResponse authenticate(User request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getPhoneNumber(),
                        request.getPassword()
                )
        );

        User user = userDao.getUserByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new BadRequestException("User not found"));
        String token = jwtService.generateToken(user);

        revokeAllTokens(user);


        saveUserToken(token, user);

        return new AuthenticationResponse(token);
    }

    private void revokeAllTokens(User user) {
        List<Token> validTokenList = tokenDao.findAllTokensByUser(user.getPhoneNumber());

        if(!validTokenList.isEmpty()){
            validTokenList.forEach(t -> t.setLoggedOut(true));
        }

        tokenDao.saveAll(validTokenList);
    }

    // save token to database
    private void saveUserToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenDao.save(token);
    }

}
