package com.example.tennisapp.dtos.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties()
public class UserPutDto {

    @NotNull(message = "phoneNumber cannot be null") private String phoneNumber;
    @NotNull(message = "name cannot be null") private String name;
    @NotNull(message = "password cannot be null") private String password;

}
