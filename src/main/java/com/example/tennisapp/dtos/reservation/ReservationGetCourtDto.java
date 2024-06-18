package com.example.tennisapp.dtos.reservation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * This class represents a data transfer object for getting a reservation by court.
 * It contains the ID of the court and a flag indicating whether to fetch future reservations only.
 */
@Data
@JsonIgnoreProperties
public class ReservationGetCourtDto {

    @NotNull(message = "Court ID is required")
    private Long courtId;

    @NotNull(message = "Future only is required")
    private boolean futureOnly;
}
