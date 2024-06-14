package com.example.tennisapp.controllers;

import com.example.tennisapp.models.User;
import com.example.tennisapp.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/user")
@AllArgsConstructor
public class UserController {

        private final UserService userService;

        @GetMapping(path = "{phoneNumber}")
        public User getUserByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
            System.out.println("Controller phoneNumber = " + phoneNumber);
            return this.userService.getUserByPhoneNumber(phoneNumber);
        }

        @PostMapping
        public ResponseEntity<?> saveUser(@RequestBody User user) {
            this.userService.saveUser(user);
            return ResponseEntity.ok().build();
        }
}
