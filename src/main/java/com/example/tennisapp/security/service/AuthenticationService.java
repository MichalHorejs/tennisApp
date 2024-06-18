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

/**
 * This class represents the service for authentication operations.
 * It contains methods for user registration, authentication, and token refresh.
 */
@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenDao tokenDao;

    /**
     * This method handles user registration.
     * It creates a new User entity, saves it to the database,
     * and returns an AuthenticationResponse with the generated access and refresh tokens.
     * @param request The User object containing the registration details.
     * @return The AuthenticationResponse object containing the access and refresh tokens.
     */
    public AuthenticationResponse register(User request){

        // don't allow admin registration
        if (request.getRole().equals(Role.ADMIN)){
            throw new BadRequestException("Admin cannot be registered");
        }

        // create new User
        User user = new User();
        user.setPhoneNumber(request.getPhoneNumber());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        // save new User
        userDao.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // save tokens
        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    /**
     * This method handles user authentication.
     * It authenticates the user with the AuthenticationManager,
     * generates new access and refresh tokens, and returns an AuthenticationResponse with these tokens.
     * @param request The User object containing the authentication details.
     * @return The AuthenticationResponse object containing the access and refresh tokens.
     */
    public AuthenticationResponse authenticate(User request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getPhoneNumber(),
                        request.getPassword()
                )
        );

        User user = userDao.getUserByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new BadRequestException("User not found"));

        // generate tokens
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // revoke all tokens for user and save them
        revokeAllTokens(user);
        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    /**
     * This method revokes all tokens for a user.
     * It retrieves all valid tokens for the user from the database,
     * sets their logged out status to true, and saves them back to the database.
     * @param user The User object for which to revoke all tokens.
     */
    private void revokeAllTokens(User user) {
        List<Token> validTokenList = tokenDao.findAllAccessTokensByUser(user.getPhoneNumber());

        if(!validTokenList.isEmpty()){
            validTokenList.forEach(t -> t.setLoggedOut(true));
        }

        tokenDao.saveAll(validTokenList);
    }

    /**
     * This method saves a new token to the database.
     * It creates a new Token entity with the provided access and refresh tokens
     * and the user, and saves it to the database.
     * @param accessToken The access token string.
     * @param refreshToken The refresh token string.
     * @param user The User object associated with the tokens.
     */
    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenDao.save(token);
    }

    /**
     * This method handles a token refresh request.
     * It retrieves the refresh token from the Authorization header,
     * validates it, generates new access and refresh tokens,
     * and returns an AuthenticationResponse with these tokens.
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @return A ResponseEntity object containing the AuthenticationResponse with the new tokens, or an error status.
     */
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        // get token from header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        String phoneNumber = jwtService.extractPhoneNumber(token);

        User user = userDao.getUserByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new BadRequestException("User not found"));

        // check if refresh token is valid, revoke valid tokens and save new tokens
        if(jwtService.isValidRefreshToken(token, user)){
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllTokens(user);
            saveUserToken(accessToken, refreshToken, user);

            return new ResponseEntity<>(new AuthenticationResponse(accessToken, refreshToken), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
}
