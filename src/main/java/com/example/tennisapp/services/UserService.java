package com.example.tennisapp.services;

import com.example.tennisapp.dtos.user.UserDeleteDto;
import com.example.tennisapp.dtos.user.UserPutDto;
import com.example.tennisapp.enums.Role;
import com.example.tennisapp.exceptions.BadRequestException;
import com.example.tennisapp.models.User;
import com.example.tennisapp.daos.UserDao;
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

    public void save(User user) {
        if (user.getRole().equals(Role.ADMIN)) {
            throw new BadRequestException("Admin cannot be registered");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }

    public void update(UserPutDto userPutDto) {
        User existingUser = this.getUserByPhoneNumber(userPutDto.getPhoneNumber());
        existingUser.setName(userPutDto.getName());
        existingUser.setPassword(passwordEncoder.encode(userPutDto.getPassword()));

        this.userDao.update(existingUser);
    }

    public void delete(UserDeleteDto userDeleteDto) {
        User user = this.getUserByPhoneNumber(userDeleteDto.getPhoneNumber());
        user.setIsDeleted(true);

        this.userDao.update(user);

    }
}
