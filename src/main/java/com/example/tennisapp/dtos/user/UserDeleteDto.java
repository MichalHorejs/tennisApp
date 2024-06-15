package com.example.tennisapp.dtos.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties()
public class UserDeleteDto {

    @NotNull(message = "phoneNumber cannot be null") private String phoneNumber;
}
