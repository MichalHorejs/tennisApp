package com.example.tennisapp.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements  UserDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        return entityManager.find(User.class, phoneNumber);
    }

    @Override
    @Transactional
    public void save(User user) {
        entityManager.persist(user);
    }


}
