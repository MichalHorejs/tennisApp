package com.example.tennisapp.daos;

import com.example.tennisapp.models.Court;
import com.example.tennisapp.models.Reservation;
import com.example.tennisapp.models.User;

import java.util.List;
import java.util.Optional;

public interface ReservationDao {

    Optional<Reservation> getReservationById(Long reservationId);
    void save(Reservation reservation);
    boolean isCourtAvailable(Reservation reservation, Long excludeReservationId);
    void update(Reservation reservation);
    List<Reservation> getReservationsByPhone(User user, boolean futureOnly);
    List<Reservation> getReservationsByCourt(Court courtById, boolean futureOnly);
}
