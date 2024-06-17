package com.example.tennisapp.dtos.reservation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class ReservationDeleteDto {

    @NotNull(message = "Reservation id is required")
    private Long reservationId;
}
