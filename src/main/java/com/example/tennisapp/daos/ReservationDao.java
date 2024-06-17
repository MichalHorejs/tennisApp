package com.example.tennisapp.daos;

import com.example.tennisapp.models.Reservation;

import java.util.Optional;

public interface ReservationDao {

    Optional<Reservation> getReservationById(Long reservationId);
    void save(Reservation reservation);
    boolean isCourtAvailable(Reservation reservation, Long excludeReservationId);
    void update(Reservation reservation);
}
