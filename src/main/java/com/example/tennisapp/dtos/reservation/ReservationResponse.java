package com.example.tennisapp.dtos.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This class represents a data transfer object for a reservation response.
 * It contains the price of the reservation.
 */
@Data
@AllArgsConstructor
public class ReservationResponse {
    private Double price;
}
