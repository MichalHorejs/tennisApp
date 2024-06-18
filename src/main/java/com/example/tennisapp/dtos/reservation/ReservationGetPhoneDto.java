package com.example.tennisapp.dtos.reservation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * This class represents a data transfer object for getting a reservation by phone number.
 * It contains the phone number and a flag indicating whether to fetch future reservations only.
 */
@Data
@JsonIgnoreProperties
public class ReservationGetPhoneDto {

    @NotNull(message = "Phone number is required")
    private String phoneNumber;

    @NotNull(message = "Future only is required")
    private boolean futureOnly;
}
