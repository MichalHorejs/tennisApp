package com.example.tennisapp.dtos.reservation;

import com.example.tennisapp.models.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.sql.Time;

/**
 * This class represents a full response data transfer object for a reservation.
 * It contains all the details of a reservation.
 */
@AllArgsConstructor
@Data
public class ReservationFullResponse {

    private Long reservationId;
    String phoneNumber;
    Long courtId;
    Date date;
    Time startTime;
    Time endTime;
    Boolean isDoubles;

    /**
     * This constructor creates a full response data transfer object from a Reservation model.
     * @param reservation The Reservation model to create the DTO from.
     */
    public ReservationFullResponse(Reservation reservation) {
        this.reservationId = reservation.getReservationId();
        this.phoneNumber = reservation.getUser().getPhoneNumber();
        this.courtId = reservation.getCourt().getCourtId();
        this.date = reservation.getDate();
        this.startTime = reservation.getStartTime();
        this.endTime = reservation.getEndTime();
        this.isDoubles = reservation.getIsDoubles();
    }

}
