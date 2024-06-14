package com.example.tennisapp.daos;

import com.example.tennisapp.models.User;

public interface UserDao {
    User getUserByPhoneNumber(String phoneNumber);
    void save(User user);
}
