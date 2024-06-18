package com.example.tennisapp.security.daos;

import com.example.tennisapp.security.models.Token;

import java.util.List;
import java.util.Optional;

public interface TokenDao {

    List<Token> findAllTokensByUser(String phoneNumber);
    Optional<Token> findByToken(String token);
    void save(Token token);
    void saveAll(List<Token> tokens);
    void update(Token token);
}
