package com.example.tennisapp.dtos.reservation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class ReservationGetPhoneDto {

    @NotNull(message = "Phone number is required")
    private String phoneNumber;

    @NotNull(message = "Future only is required")
    private boolean futureOnly;
}
