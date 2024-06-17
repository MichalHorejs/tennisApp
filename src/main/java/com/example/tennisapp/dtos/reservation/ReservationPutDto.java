package com.example.tennisapp.dtos.reservation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Date;
import java.sql.Time;

@Data
@JsonIgnoreProperties
public class ReservationPutDto {

    @NotNull(message = "Reservation id cannot be null")
    Long reservationId;

    @NotNull(message = "User cannot be null")
    String phoneNumber;

    @NotNull(message = "Court cannot be null")
    Long courtId;

    @NotNull(message = "Date cannot be null")
    @FutureOrPresent(message = "Date must be in the present or future")
    Date date;

    @NotNull(message = "Start time cannot be null")
    // TODO: custom annotation for time validation
    Time startTime;

    @NotNull(message = "End time cannot be null")
    // TODO: custom annotation for time validation
    Time endTime;

    @NotNull(message = "Is doubles cannot be null")
    Boolean isDoubles;
}
