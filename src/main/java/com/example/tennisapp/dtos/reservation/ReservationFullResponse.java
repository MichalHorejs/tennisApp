package com.example.tennisapp.dtos.reservation;

import com.example.tennisapp.models.Reservation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.sql.Time;

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
