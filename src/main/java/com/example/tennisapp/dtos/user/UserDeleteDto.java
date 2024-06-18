package com.example.tennisapp.dtos.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * This class represents a data transfer object for deleting a user.
 * It contains the phone number of the user to be deleted.
 */
@Data
@JsonIgnoreProperties()
public class UserDeleteDto {

    @NotNull(message = "phoneNumber cannot be null") private String phoneNumber;
}
