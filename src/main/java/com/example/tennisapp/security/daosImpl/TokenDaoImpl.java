package com.example.tennisapp.security.daosImpl;

import com.example.tennisapp.security.daos.TokenDao;
import com.example.tennisapp.security.models.Token;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TokenDaoImpl implements TokenDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Token> findAllAccessTokensByUser(String phoneNumber) {
        String query = "SELECT t FROM Token t WHERE " +
                "t.user.phoneNumber = :phoneNumber AND t.loggedOut = false";

        return entityManager.createQuery(query, Token.class)
                .setParameter("phoneNumber", phoneNumber)
                .getResultList();
    }

    @Override
    public Optional<Token> findByAccessToken(String token) {
        String query = "SELECT t FROM Token t WHERE t.accessToken = :token";
        return entityManager.createQuery(query, Token.class)
                .setParameter("token", token)
                .getResultStream()
                .findFirst();
    }

    @Override
    public Optional<Token> findByRefreshToken(String token) {
        String query = "SELECT t FROM Token t WHERE t.refreshToken = :token";
        return entityManager.createQuery(query, Token.class)
                .setParameter("token", token)
                .getResultStream()
                .findFirst();
    }

    @Override
    public void save(Token token) {
        entityManager.persist(token);
    }

    @Override
    public void saveAll(List<Token> tokens) {
        for (Token token : tokens) {
            entityManager.persist(token);
        }
    }

    @Override
    public void update(Token token) {
        entityManager.merge(token);
    }

}
