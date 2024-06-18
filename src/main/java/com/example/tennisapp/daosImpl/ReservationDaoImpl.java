package com.example.tennisapp.daosImpl;

import com.example.tennisapp.daos.ReservationDao;
import com.example.tennisapp.models.Court;
import com.example.tennisapp.models.Reservation;
import com.example.tennisapp.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * This class implements the ReservationDao interface.
 * It is used to interact with the database and perform operations on the Reservation entity.
 */
@Repository
@AllArgsConstructor
@Transactional
public class ReservationDaoImpl implements ReservationDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method retrieves a reservation by its ID.
     * @param reservationId ID of the reservation to retrieve.
     * @return Optional<Reservation> The reservation with the given ID, if it exists.
     */
    @Override
    public Optional<Reservation> getReservationById(Long reservationId) {
        String query = "SELECT r FROM Reservation r WHERE r.id = :reservationId AND r.isDeleted = false";
        Reservation reservation = entityManager
                .createQuery(query, Reservation.class)
                .setParameter("reservationId", reservationId)
                .getResultStream()
                .findFirst()
                .orElse(null);

        return Optional.ofNullable(reservation);
    }

    /**
     * This method saves a new reservation to the database.
     * @param reservation The reservation to save.
     */
    @Override
    public void save(Reservation reservation) {
        entityManager.persist(reservation);
    }

    /**
     * This method updates an existing reservation in the database.
     * @param reservation The reservation to update.
     */
    @Override
    public void update(Reservation reservation) {
        entityManager.merge(reservation);
    }

    /**
     * This method retrieves a list of reservations by a user's phone number.
     * @param user The user whose phone number to use.
     * @param futureOnly If true, only reservations in the future are returned.
     * @return List<Reservation> The list of reservations.
     */
    @Override
    public List<Reservation> getReservationsByPhone(User user, boolean futureOnly) {
        StringBuilder query = new StringBuilder("SELECT r FROM Reservation r WHERE " +
                "r.user.phoneNumber = :phoneNumber AND r.isDeleted = false");

        if (futureOnly) {
            query.append(" AND r.date >= :today");
        }

        TypedQuery<Reservation> queryResult = entityManager
                .createQuery(query.toString(), Reservation.class)
                .setParameter("phoneNumber", user.getPhoneNumber());

        if (futureOnly) {
            queryResult.setParameter("today", LocalDate.now());
        }

        return queryResult.getResultList();

    }

    /**
     * This method retrieves a list of reservations by court.
     * @param court The court to retrieve reservations for.
     * @param futureOnly If true, only reservations in the future are returned.
     * @return List<Reservation> The list of reservations.
     */
    @Override
    public List<Reservation> getReservationsByCourt(Court court, boolean futureOnly) {
        StringBuilder query = new StringBuilder("SELECT r FROM Reservation r WHERE " +
                "r.court.id = :courtId AND r.isDeleted = false");

        if (futureOnly) {
            query.append(" AND r.date >= :today");
        }

        query.append(" ORDER BY r.reservationCreated ASC");

        TypedQuery<Reservation> queryResult = entityManager
                .createQuery(query.toString(), Reservation.class)
                .setParameter("courtId", court.getCourtId());

        if (futureOnly) {
            queryResult.setParameter("today", LocalDate.now());
        }

        return queryResult.getResultList();
    }

    /**
     * This method checks if a court is available for a reservation.
     * @param reservation The reservation to check.
     * @param excludeReservationId The ID of a reservation to exclude from the check.
     *                             This is used when updating a reservation.
     *                             To not exculde insert: excludeReservationId = 0L .
     * @return boolean True if the court is available, false otherwise.
     */
    @Override
    public boolean isCourtAvailable(Reservation reservation, Long excludeReservationId) {
        String query = "SELECT r FROM Reservation r WHERE " +
                "r.court.id = :courtId AND r.date = :date AND r.date = :date " +
                "AND ((r.startTime <= :startTime AND r.endTime > :startTime) " +
                "OR (r.startTime < :endTime AND r.endTime >= :endTime) " +
                "OR (r.startTime >= :startTime AND r.endTime <= :endTime))" +
                "AND r.id != :excludeReservationId AND r.isDeleted = false";

        List<Reservation> overlappingReservations = entityManager
                .createQuery(query, Reservation.class)
                .setParameter("courtId", reservation.getCourt().getCourtId())
                .setParameter("date", reservation.getDate())
                .setParameter("startTime", reservation.getStartTime())
                .setParameter("endTime", reservation.getEndTime())
                .setParameter("excludeReservationId", excludeReservationId)
                .getResultList();

        return overlappingReservations.isEmpty();
    }


}
