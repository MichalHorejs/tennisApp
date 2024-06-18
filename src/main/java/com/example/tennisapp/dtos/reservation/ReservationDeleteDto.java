package com.example.tennisapp.dtos.reservation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * This class represents a data transfer object for deleting a reservation.
 * It contains the ID of the reservation to be deleted.
 */
@Data
@JsonIgnoreProperties
public class ReservationDeleteDto {

    @NotNull(message = "Reservation id is required")
    private Long reservationId;
}
