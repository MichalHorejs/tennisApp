package com.example.tennisapp.daosImpl;

import com.example.tennisapp.daos.UserDao;
import com.example.tennisapp.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

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

    @Override
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public void update(User user) {
        entityManager.merge(user);
    }

}
