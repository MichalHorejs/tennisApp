package com.example.tennisapp.services;

import com.example.tennisapp.enums.Role;
import com.example.tennisapp.exceptions.BadRequestException;
import com.example.tennisapp.models.User;
import com.example.tennisapp.daos.UserDao;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public User getUserByPhoneNumber(String phoneNumber) {
        return this.userDao.getUserByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

//    @Transactional
    public void save(User user) {
        if (user.getRole().equals(Role.ADMIN)) {
            throw new BadRequestException("Admin cannot be registered");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }

    public void update(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.userDao.update(user)
                .orElseThrow(() -> new BadRequestException("User not found"));
    }
}
