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

/**
 * This class represents the service layer for the User entity.
 * It contains methods for retrieving, saving, updating, and deleting users.
 */
@Service
@AllArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    /**
     * This method retrieves a user by their phone number.
     * If the user is not found, it throws a RuntimeException.
     * @param phoneNumber The phone number of the user.
     * @return The user with the given phone number.
     */
    public User getUserByPhoneNumber(String phoneNumber) {
        return this.userDao.getUserByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * This method saves a user to the database.
     * If the user is an admin, it throws a BadRequestException.
     * It encodes the user's password before saving.
     * @param user The user to save.
     */
    public void save(User user) {
        if (user.getRole().equals(Role.ADMIN)) {
            throw new BadRequestException("Admin cannot be registered");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }

    /**
     * This method updates a user in the database.
     * It first retrieves the user by their phone number,
     * then updates their name and password, and finally saves the updated user.
     * It encodes the user's new password before saving.
     * @param userPutDto The DTO (Data Transfer Object) containing the new name and password for the user.
     */
    public void update(UserPutDto userPutDto) {
        User existingUser = this.getUserByPhoneNumber(userPutDto.getPhoneNumber());
        existingUser.setName(userPutDto.getName());
        existingUser.setPassword(passwordEncoder.encode(userPutDto.getPassword()));

        this.userDao.update(existingUser);
    }

    /**
     * This method deletes a user from the database.
     * It first retrieves the user by their phone number,
     * then sets their isDeleted flag to true, and finally saves the updated user.
     * @param userDeleteDto The DTO (Data Transfer Object)
     *                      containing the phone number of the user to delete.
     */
    public void delete(UserDeleteDto userDeleteDto) {
        User user = this.getUserByPhoneNumber(userDeleteDto.getPhoneNumber());
        user.setIsDeleted(true);

        this.userDao.update(user);

    }
}
