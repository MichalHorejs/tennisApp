package com.example.tennisapp.security.service;

import com.example.tennisapp.models.User;
import com.example.tennisapp.security.daos.TokenDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * This class represents the service for JWT (JSON Web Token) operations.
 * It contains methods for token generation, validation, and extraction of claims.
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration.accessToken}")
    private long expirationAccess;

    @Value("${jwt.expiration.refreshToken}")
    private long expirationRefresh;

    private final TokenDao tokenDao;

    /**
     * This method extracts the phone number (used as the subject) from the token.
     * @param token The JWT string.
     * @return The phone number string.
     */
    public String extractPhoneNumber(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    /**
     * This method checks if the token is valid and not expired.
     * @param token The JWT string.
     * @param user The UserDetails object.
     * @return A boolean indicating whether the token is valid.
     */
    public boolean isValid(String token, UserDetails user) {
        String phoneNumber = extractPhoneNumber(token);

        boolean isValidToken = tokenDao.findByAccessToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return (phoneNumber.equals(user.getUsername())) && isTokenExpired(token) && isValidToken;
    }

    /**
     * This method checks if the refresh token is valid.
     * @param token The JWT string.
     * @param user The User object.
     * @return A boolean indicating whether the refresh token is valid.
     */
    public boolean isValidRefreshToken(String token, User user) {
        String phoneNumber = extractPhoneNumber(token);

        boolean isValidRefreshToken = tokenDao.findByRefreshToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return (phoneNumber.equals(user.getUsername())) && isTokenExpired(token) && isValidRefreshToken;
    }

    /**
     * This method checks if the token is expired.
     * @param token The JWT string.
     * @return A boolean indicating whether the token is expired.
     */
    private boolean isTokenExpired(String token) {
        return !extractExpiration(token).before(new Date());
    }

    /**
     * This method extracts the expiration date from the token.
     * @param token The JWT string.
     * @return The expiration date.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * This method extracts a claim from the token.
     * @param token The JWT string.
     * @param claimsResolver The function that resolves the claim.
     * @param <T> The type of the claim.
     * @return The claim object.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * This method extracts all the claims from the token.
     * @param token The JWT string.
     * @return The Claims object.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * This method generates an access token for the user.
     * @param user The User object.
     * @return The access token string.
     */
    public String generateAccessToken(User user) {
        return generateToken(user, expirationAccess);
    }

    /**
     * This method generates a refresh token for the user.
     * @param user The User object.
     * @return The refresh token string.
     */
    public String generateRefreshToken(User user) {
        return generateToken(user, expirationRefresh);
    }

    /**
     * This method generates a token for the user.
     * @param user The User object.
     * @param expiration The expiration time in minutes.
     * @return The token string.
     */
    private String generateToken(User user, long expiration) {
        return Jwts.builder()
                .subject(user.getPhoneNumber())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * expiration))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * This method gets the secret key from the application.properties file.
     * @return The SecretKey object.
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}

