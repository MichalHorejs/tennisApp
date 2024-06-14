package com.example.tennisapp.dtos.user;

import com.example.tennisapp.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private String phoneNumber;
    private String name;
    private Role role;
}
