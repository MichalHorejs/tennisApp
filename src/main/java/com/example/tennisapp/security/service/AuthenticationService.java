package com.example.tennisapp.security.service;

import com.example.tennisapp.daos.UserDao;
import com.example.tennisapp.enums.Role;
import com.example.tennisapp.exceptions.BadRequestException;
import com.example.tennisapp.models.User;
import com.example.tennisapp.security.daos.TokenDao;
import com.example.tennisapp.security.models.AuthenticationResponse;
import com.example.tennisapp.security.models.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken);
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

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllTokens(user);
        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    // revoke all tokens for a user
    private void revokeAllTokens(User user) {
        List<Token> validTokenList = tokenDao.findAllAccessTokensByUser(user.getPhoneNumber());

        if(!validTokenList.isEmpty()){
            validTokenList.forEach(t -> t.setLoggedOut(true));
        }

        tokenDao.saveAll(validTokenList);
    }

    // save new token to database
    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenDao.save(token);
    }

    public ResponseEntity refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        String phoneNumber = jwtService.extractPhoneNumber(token);

        User user = userDao.getUserByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new BadRequestException("User not found"));

        if(jwtService.isValidRefreshToken(token, user)){
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllTokens(user);
            saveUserToken(accessToken, refreshToken, user);

            return new ResponseEntity(new AuthenticationResponse(accessToken, refreshToken), HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.UNAUTHORIZED);

    }
}
