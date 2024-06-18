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

    public String extractPhoneNumber(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    // Check if the token is valid and not expired
    public boolean isValid(String token, UserDetails user) {
        String phoneNumber = extractPhoneNumber(token);

        boolean isValidToken = tokenDao.findByAccessToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return (phoneNumber.equals(user.getUsername())) && isTokenExpired(token) && isValidToken;
    }

    public boolean isValidRefreshToken(String token, User user) {
        String phoneNumber = extractPhoneNumber(token);

        boolean isValidRefreshToken = tokenDao.findByRefreshToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return (phoneNumber.equals(user.getUsername())) && isTokenExpired(token) && isValidRefreshToken;
    }

    private boolean isTokenExpired(String token) {
        return !extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Generate a token for the user on login
    public String generateAccessToken(User user) {
        return generateToken(user, expirationAccess);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, expirationRefresh);
    }

    private String generateToken(User user, long expiration) {
        return Jwts.builder()
                .subject(user.getPhoneNumber())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * expiration))
                .signWith(getSignInKey())
                .compact();
    }

    // Get the secret key from the application.properties file
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}

