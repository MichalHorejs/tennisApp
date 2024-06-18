package com.example.tennisapp.controllers;

import com.example.tennisapp.dtos.user.UserDeleteDto;
import com.example.tennisapp.dtos.user.UserPutDto;
import com.example.tennisapp.dtos.user.UserResponse;
import com.example.tennisapp.models.User;
import com.example.tennisapp.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class is the controller for the User entity.
 * It has the following endpoints:
 * - GET /api/user/{phoneNumber}
 * - POST /api/user
 * - PUT /api/user
 * - DELETE /api/user
 */
@RestController
@RequestMapping(path = "api/user")
@AllArgsConstructor
public class UserController {

        private final UserService userService;

        /**
         * This method returns a user by phone number.
         * @param phoneNumber Phone number of the user to retrieve.
         * @return UserResponse
         */
        @GetMapping(path = "{phoneNumber}")
        public UserResponse getUserByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
            User user = this.userService.getUserByPhoneNumber(phoneNumber);
            return new UserResponse(user.getPhoneNumber(), user.getName(), user.getRole());
        }

        /**
         * This method saves a user.
         * It has similar functionality as registering user from /api/auth/register endpoint
         * but doesn't return JWT tokens.
         * @param user User to save
         * @return ResponseEntity<?>
         */
        @PostMapping
        public ResponseEntity<?> save(@RequestBody User user) {
            this.userService.save(user);
            return ResponseEntity.ok().build();
        }

        /**
         * This method updates a user.
         * @param userPutDto DTO with updated user information
         * @return ResponseEntity<?>
         */
        @PutMapping
        public ResponseEntity<?> update(@RequestBody UserPutDto userPutDto) {
            this.userService.update(userPutDto);
            return ResponseEntity.ok().build();
        }

        /**
         * This method deletes a user.
         * @param userDeleteDto DTO with phone number of the user to delete
         * @return ResponseEntity<?>
         */
        @DeleteMapping
        public ResponseEntity<?> delete(@RequestBody UserDeleteDto userDeleteDto){
            this.userService.delete(userDeleteDto);
            return ResponseEntity.ok().build();
        }
}
