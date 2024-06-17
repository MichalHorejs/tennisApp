package com.example.tennisapp.daosImpl;

import com.example.tennisapp.daos.ReservationDao;
import com.example.tennisapp.models.Reservation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Transactional
public class ReservationDaoImpl implements ReservationDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Reservation> getReservationById(Long reservationId) {
        String query = "SELECT r FROM Reservation r WHERE r.id = :reservationId AND r.isDeleted = false";
        Reservation reservation = entityManager.createQuery(query, Reservation.class)
                .setParameter("reservationId", reservationId)
                .getResultStream()
                .findFirst()
                .orElse(null);

        return Optional.ofNullable(reservation);
    }

    @Override
    public void save(Reservation reservation) {
        entityManager.persist(reservation);
    }

    @Override
    public void update(Reservation reservation) {
        entityManager.merge(reservation);
    }

    // Check if court is available for reservation
    // excludeReservationId is used to exclude the current reservation from the check
    // this is used when updating a reservation
    // to not exculde insert: excludeReservationId = 0L
    @Override
    public boolean isCourtAvailable(Reservation reservation, Long excludeReservationId) {
        String query = "SELECT r FROM Reservation r WHERE " +
                "r.court.id = :courtId AND r.date = :date AND r.date = :date " +
                "AND ((r.startTime <= :startTime AND r.endTime > :startTime) " +
                "OR (r.startTime < :endTime AND r.endTime >= :endTime) " +
                "OR (r.startTime >= :startTime AND r.endTime <= :endTime))" +
                "AND r.id != :excludeReservationId AND r.isDeleted = false";

        List<Reservation> overlappingReservations = entityManager.createQuery(query, Reservation.class)
                .setParameter("courtId", reservation.getCourt().getCourtId())
                .setParameter("date", reservation.getDate())
                .setParameter("startTime", reservation.getStartTime())
                .setParameter("endTime", reservation.getEndTime())
                .setParameter("excludeReservationId", excludeReservationId)
                .getResultList();

        return overlappingReservations.isEmpty();
    }


}
