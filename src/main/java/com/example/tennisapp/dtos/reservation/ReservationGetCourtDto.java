package com.example.tennisapp.dtos.reservation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class ReservationGetCourtDto {

    @NotNull(message = "Court ID is required")
    private Long courtId;

    @NotNull(message = "Future only is required")
    private boolean futureOnly;
}
