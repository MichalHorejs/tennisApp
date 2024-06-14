package com.example.tennisapp.services;

import com.example.tennisapp.models.User;
import com.example.tennisapp.daos.UserDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserDao userDao;

    public User getUserByPhoneNumber(String phoneNumber) {
        System.out.println("Service phoneNumber = " + phoneNumber);
        return userDao.getUserByPhoneNumber(phoneNumber);
    }

    public void saveUser(User user) {
        userDao.save(user);
    }
}
