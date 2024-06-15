package com.example.tennisapp.daos;

import com.example.tennisapp.models.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> getUserByPhoneNumber(String phoneNumber);
    void save(User user);
    Optional<User> update(User user);
    Optional<User> delete(User toUpdateUser);
}
