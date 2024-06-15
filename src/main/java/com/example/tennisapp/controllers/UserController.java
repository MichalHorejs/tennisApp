package com.example.tennisapp.controllers;

import com.example.tennisapp.dtos.user.UserDeleteDto;
import com.example.tennisapp.dtos.user.UserPutDto;
import com.example.tennisapp.dtos.user.UserResponse;
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
        public UserResponse getUserByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
            User user = this.userService.getUserByPhoneNumber(phoneNumber);
            return new UserResponse(user.getPhoneNumber(), user.getName(), user.getRole());
        }

        @PostMapping
        public ResponseEntity<?> save(@RequestBody User user) {
            this.userService.save(user);
            return ResponseEntity.ok().build();
        }

        @PutMapping
        public ResponseEntity<?> update(@RequestBody UserPutDto userPutDto) {
            this.userService.update(
                    new User(userPutDto.getPhoneNumber(), userPutDto.getName(), userPutDto.getPassword())
            );
            return ResponseEntity.ok().build();
        }

        @DeleteMapping
        public ResponseEntity<?> delete(@RequestBody UserDeleteDto userDeleteDto){
            this.userService.delete(
                    new User(userDeleteDto.getPhoneNumber())
            );
            return ResponseEntity.ok().build();
        }
}
