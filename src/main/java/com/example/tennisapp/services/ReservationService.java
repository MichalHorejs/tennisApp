package com.example.tennisapp.services;

import com.example.tennisapp.daos.ReservationDao;
import com.example.tennisapp.dtos.reservation.*;
import com.example.tennisapp.enums.Role;
import com.example.tennisapp.exceptions.BadRequestException;
import com.example.tennisapp.models.Court;
import com.example.tennisapp.models.Reservation;
import com.example.tennisapp.models.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents the service layer for the Reservation entity.
 * It contains methods for retrieving, saving, updating, and deleting reservations.
 */
@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationDao reservationDao;
    private final UserService userService;
    private final CourtService courtService;

    /**
     * This method retrieves a reservation by its ID.
     * If the reservation is not found, it throws a BadRequestException.
     * @param reservationId The ID of the reservation.
     * @return The reservation with the given ID.
     */
    public Reservation getReservationById(Long reservationId) {
        return reservationDao.getReservationById(reservationId)
                .orElseThrow(() -> new BadRequestException("Reservation not found"));
    }

    /**
     * This method retrieves all reservations made by a user.
     * @param reservationGetPhoneDto The DTO (Data Transfer Object) containing the phone number of the user and a flag indicating if only future reservations should be returned.
     * @return A list of all reservations made by the user.
     */
    public List<ReservationFullResponse> getReservationsByPhone(ReservationGetPhoneDto reservationGetPhoneDto) {
        List<Reservation> reservations =  reservationDao.getReservationsByPhone(
                userService.getUserByPhoneNumber(reservationGetPhoneDto.getPhoneNumber()),
                reservationGetPhoneDto.isFutureOnly()
        );

        return convert(reservations);
    }

    /**
     * This method retrieves all reservations for a court.
     * @param reservationGetCourtDto The DTO (Data Transfer Object) containing the ID of the court and a flag indicating if only future reservations should be returned.
     * @return A list of all reservations for the court.
     */
    public List<ReservationFullResponse> getReservationsByCourt(ReservationGetCourtDto reservationGetCourtDto) {
        List<Reservation> reservations =  reservationDao.getReservationsByCourt(
                courtService.getCourtById(reservationGetCourtDto.getCourtId()),
                reservationGetCourtDto.isFutureOnly()
        );

        return convert(reservations);
    }

    /**
     * This method saves a new reservation to the database.
     * If the user does not exist, it creates a new user.
     * If the reservation is not valid, it throws a BadRequestException.
     * @param reservationPostDto The DTO (Data Transfer Object) containing the details
     *                           of the reservation to save.
     * @return A response containing the price of the reservation.
     */
    public ReservationResponse save(ReservationPostDto reservationPostDto) {
        User user;
        try {
            // Use existing user if user exists
            user = userService.getUserByPhoneNumber(reservationPostDto.getUser().getPhoneNumber());
        } catch (RuntimeException e) {

            // Create new user if user does not exist
            user = new User(
                    reservationPostDto.getUser().getPhoneNumber(),
                    reservationPostDto.getUser().getName(),
                    reservationPostDto.getUser().getPassword()
            );
            user.setRole(Role.USER);

            // Save new user
            userService.save(user);
        }

        Court court = courtService.getCourtById(reservationPostDto.getCourtId());

        // Create new reservation
        Reservation reservation = new Reservation(
                user,
                court,
                reservationPostDto.getDate(),
                reservationPostDto.getStartTime(),
                reservationPostDto.getEndTime(),
                reservationPostDto.getIsDoubles()
        );

        // Check if new reservation is valid
        if (newReservationCheckBeforeSave(reservation, 0L)) {
            throw new BadRequestException("Invalid reservation");
        }

        reservationDao.save(reservation);

        return new ReservationResponse(reservation.getPrice());
    }

    /**
     * This method updates an existing reservation in the database.
     * If the reservation is not valid, it throws a BadRequestException.
     * @param reservationPutDto The DTO (Data Transfer Object) containing
     *                          the details of the reservation to update.
     * @return A response containing the price of the reservation.
     */
    public ReservationResponse update(ReservationPutDto reservationPutDto) {

        // Check if user and court exists
        User user = userService.getUserByPhoneNumber(reservationPutDto.getPhoneNumber());
        Court court = courtService.getCourtById(reservationPutDto.getCourtId());

        Reservation existingReservation = this.getReservationById(reservationPutDto.getReservationId());

        // Create new reservation
        Reservation newReservation = new Reservation(
                user,
                court,
                reservationPutDto.getDate(),
                reservationPutDto.getStartTime(),
                reservationPutDto.getEndTime(),
                reservationPutDto.getIsDoubles()
        );

        // Check if new reservation is valid and exclude current reservation from check
        if (newReservationCheckBeforeSave(newReservation, existingReservation.getReservationId())) {
            throw new BadRequestException("Invalid reservation");
        }

        // Update existing reservation
        existingReservation.setUser(user);
        existingReservation.setCourt(court);
        existingReservation.setDate(reservationPutDto.getDate());
        existingReservation.setStartTime(reservationPutDto.getStartTime());
        existingReservation.setEndTime(reservationPutDto.getEndTime());
        existingReservation.setIsDoubles(reservationPutDto.getIsDoubles());

        // recalculate price
        existingReservation.setPrice(newReservation.getPrice());

        reservationDao.update(existingReservation);

        return new ReservationResponse(existingReservation.getPrice());
    }

    /**
     * This method deletes a reservation from the database.
     * It sets the isDeleted flag of the reservation to true.
     * @param reservationDeleteDto The DTO (Data Transfer Object) containing the
     *                             ID of the reservation to delete.
     */
    public void delete(ReservationDeleteDto reservationDeleteDto) {
        Reservation reservation = this.getReservationById(reservationDeleteDto.getReservationId());
        reservation.setIsDeleted(true);

        reservationDao.update(reservation);
    }

    /**
     * This method checks if a court is available for a given time and if the reservation time is valid.
     * It excludes a reservation with a given ID from the check.
     * @param reservation The reservation to check.
     * @param excludeReservationId The ID of the reservation to exclude from the check.
     * @return A boolean indicating if the court is available and the reservation time is valid.
     */
    private boolean newReservationCheckBeforeSave(Reservation reservation, Long excludeReservationId) {
        return !reservationDao.isCourtAvailable(reservation, excludeReservationId) ||
                !this.isTimeValid(reservation.getStartTime().toLocalTime(), reservation.getEndTime().toLocalTime());
    }


    /**
     * This method checks if a time is valid.
     * The time must be a whole hour, the duration must be between 1 and 3 hours,
     * and the start time must be before the end time.
     * @param startTime The start time to check.
     * @param endTime The end time to check.
     * @return A boolean indicating if the time is valid.
     */
    private Boolean isTimeValid(LocalTime startTime, LocalTime endTime) {

        boolean isWholeHour = startTime.getMinute() == 0
                && startTime.getSecond() == 0
                && endTime.getMinute() == 0
                && endTime.getSecond() == 0;

        boolean duration = Duration.between(startTime, endTime).toHours() <= 3
                && Duration.between(startTime, endTime).toHours() >= 1;

        return isWholeHour && duration && startTime.isBefore(endTime);
    }

    /**
     * This method converts a list of Reservation objects into a list of ReservationFullResponse objects.
     * It uses the stream API to map each Reservation
     * to a new ReservationFullResponse and collects the results into a list.
     * @param reservations The list of Reservation objects to convert.
     * @return A list of ReservationFullResponse objects.
     */
    private static List<ReservationFullResponse> convert(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationFullResponse::new)
                .collect(Collectors.toList());
    }

}
