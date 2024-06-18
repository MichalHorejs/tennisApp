package com.example.tennisapp.dtos.reservation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Date;
import java.sql.Time;

/**
 * This class represents a data transfer object for updating a reservation.
 * It contains the ID of the reservation to be updated, the phone number of the user, the ID of the court, the date and time of the reservation, and a flag indicating whether it's a doubles match.
 */
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
    Time startTime;

    @NotNull(message = "End time cannot be null")
    Time endTime;

    @NotNull(message = "Is doubles cannot be null")
    Boolean isDoubles;
}
