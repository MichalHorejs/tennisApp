package com.example.tennisapp.user;

public interface UserDao {
    User getUserByPhoneNumber(String phoneNumber);
    void save(User user);
}
