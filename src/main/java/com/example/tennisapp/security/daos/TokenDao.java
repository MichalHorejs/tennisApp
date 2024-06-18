package com.example.tennisapp.security.daos;

import com.example.tennisapp.security.models.Token;

import java.util.List;
import java.util.Optional;

/**
 * This interface represents the DAO (Data Access Object) for the Token entity.
 * It contains methods for retrieving, saving, and updating tokens.
 */
public interface TokenDao {

    /**
     * This method retrieves all access tokens for a user by their phone number.
     * @param phoneNumber The phone number of the user.
     * @return A list of Token objects.
     */
    List<Token> findAllAccessTokensByUser(String phoneNumber);

    /**
     * This method retrieves a token by its access token string.
     * @param token The access token string.
     * @return An Optional containing the Token object if found, or an empty Optional if not found.
     */
    Optional<Token> findByAccessToken(String token);

    /**
     * This method retrieves a token by its refresh token string.
     * @param token The refresh token string.
     * @return An Optional containing the Token object if found, or an empty Optional if not found.
     */
    Optional<Token> findByRefreshToken(String token);

    /**
     * This method saves a Token object to the database.
     * @param token The Token object to save.
     */
    void save(Token token);

    /**
     * This method saves a list of Token objects to the database.
     * @param tokens The list of Token objects to save.
     */
    void saveAll(List<Token> tokens);

    /**
     * This method updates a Token object in the database.
     * @param token The Token object to update.
     */
    void update(Token token);
}
