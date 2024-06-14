package com.example.tennisapp.daosImpl;

import com.example.tennisapp.daos.UserDao;
import com.example.tennisapp.models.Court;
import com.example.tennisapp.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional // must be due to spring.security
    public Optional<User> getUserByPhoneNumber(String phoneNumber) {
        String query = "SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber AND u.isDeleted = false";
        TypedQuery<User> typedQuery = entityManager.createQuery(query, User.class);
        typedQuery.setParameter("phoneNumber", phoneNumber);
        User user = typedQuery.getResultStream().findFirst().orElse(null);
        return Optional.ofNullable(user);
    }

    @Override
    @Transactional // must be due to spring.security when inserting sample data
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    @Transactional
    public Optional<User> update(User toUpdateUser) {
        Optional<User> user = getUserByPhoneNumber(toUpdateUser.getPhoneNumber());

        if (user.isEmpty()) {
            return Optional.empty();
        } else {
            user.get().setName(toUpdateUser.getName());
            user.get().setPassword(toUpdateUser.getPassword());
            entityManager.merge(user.get());
            return user;
        }
    }


}
