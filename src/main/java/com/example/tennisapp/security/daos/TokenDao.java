package com.example.tennisapp.security.daos;

import com.example.tennisapp.security.models.Token;

import java.util.List;
import java.util.Optional;

public interface TokenDao {

    List<Token> findAllAccessTokensByUser(String phoneNumber);
    Optional<Token> findByAccessToken(String token);
    Optional<Token> findByRefreshToken(String token);
    void save(Token token);
    void saveAll(List<Token> tokens);
    void update(Token token);
}
