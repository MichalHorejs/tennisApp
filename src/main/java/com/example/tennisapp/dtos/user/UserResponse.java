package com.example.tennisapp.dtos.user;

import com.example.tennisapp.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This class represents a data transfer object for a user response.
 * It contains the phone number, name, and role of the user.
 */
@Data
@AllArgsConstructor
public class UserResponse {
    private String phoneNumber;
    private String name;
    private Role role;
}
