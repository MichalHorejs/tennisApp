package com.example.tennisapp.daosImpl;

import com.example.tennisapp.daos.UserDao;
import com.example.tennisapp.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This class implements the UserDao interface.
 * It is used to interact with the database and perform operations on the User entity.
 */
@Repository
@Transactional
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method retrieves a user by their phone number.
     * @param phoneNumber The phone number of the user to retrieve.
     * @return Optional<User> The user with the given phone number, if they exist.
     */
    @Override
    public Optional<User> getUserByPhoneNumber(String phoneNumber) {
        String query = "SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber AND u.isDeleted = false";
        User user = entityManager.createQuery(query, User.class)
                .setParameter("phoneNumber", phoneNumber)
                .getResultStream()
                .findFirst()
                .orElse(null);
        return Optional.ofNullable(user);
    }

    /**
     * This method saves a new user to the database.
     * @param user The user to save.
     */
    @Override
    public void save(User user) {
        entityManager.persist(user);
    }

    /**
     * This method updates an existing user in the database.
     * @param user The user to update.
     */
    @Override
    public void update(User user) {
        entityManager.merge(user);
    }

}
