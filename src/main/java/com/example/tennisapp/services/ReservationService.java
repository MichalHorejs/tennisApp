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


@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationDao reservationDao;
    private final UserService userService;
    private final CourtService courtService;

    public Reservation getReservationById(Long reservationId) {
        return reservationDao.getReservationById(reservationId)
                .orElseThrow(() -> new BadRequestException("Reservation not found"));
    }

    public List<ReservationFullResponse> getReservationsByPhone(ReservationGetPhoneDto reservationGetPhoneDto) {
        List<Reservation> reservations =  reservationDao.getReservationsByPhone(
                userService.getUserByPhoneNumber(reservationGetPhoneDto.getPhoneNumber()),
                reservationGetPhoneDto.isFutureOnly()
        );

        return reservations.stream()
                .map(ReservationFullResponse::new)
                .collect(Collectors.toList());
    }

    public List<ReservationFullResponse> getReservationsByCourt(ReservationGetCourtDto reservationGetCourtDto) {
        List<Reservation> reservations =  reservationDao.getReservationsByCourt(
                courtService.getCourtById(reservationGetCourtDto.getCourtId()),
                reservationGetCourtDto.isFutureOnly()
        );

        return reservations.stream()
                .map(ReservationFullResponse::new)
                .collect(Collectors.toList());
    }

    // Saves new reservation
    public ReservationResponse save(ReservationPostDto reservationPostDto) {
        User user;
        try {
            user = userService.getUserByPhoneNumber(reservationPostDto.getUser().getPhoneNumber());
        } catch (RuntimeException e) {
            user = new User(
                    reservationPostDto.getUser().getPhoneNumber(),
                    reservationPostDto.getUser().getName(),
                    reservationPostDto.getUser().getPassword()
            );
            user.setRole(Role.USER);
            userService.save(user);
        }

        Court court = courtService.getCourtById(reservationPostDto.getCourtId());

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

    // Updates existing reservation
    public ReservationResponse update(ReservationPutDto reservationPutDto) {

        // Check if user and court exists
        User user = userService.getUserByPhoneNumber(reservationPutDto.getPhoneNumber());
        Court court = courtService.getCourtById(reservationPutDto.getCourtId());

        Reservation existingReservation = this.getReservationById(reservationPutDto.getReservationId());

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

    // Soft deletes existing reservation
    public void delete(ReservationDeleteDto reservationDeleteDto) {
        Reservation reservation = this.getReservationById(reservationDeleteDto.getReservationId());
        reservation.setIsDeleted(true);

        reservationDao.update(reservation);
    }

    // Final check if court is avaible for given time and
    // if reservation time is valid (nonNegative, etc...)
    private boolean newReservationCheckBeforeSave(Reservation reservation, Long excludeReservationId) {
        return !reservationDao.isCourtAvailable(reservation, excludeReservationId) ||
                !this.isTimeValid(reservation.getStartTime().toLocalTime(), reservation.getEndTime().toLocalTime());
    }


    // Check if time is valid, time must be whole hour, duration must be between 1 and 3 hours
    // start time must be before end time
    private Boolean isTimeValid(LocalTime startTime, LocalTime endTime) {

        boolean isWholeHour = startTime.getMinute() == 0
                && startTime.getSecond() == 0
                && endTime.getMinute() == 0
                && endTime.getSecond() == 0;

        boolean duration = Duration.between(startTime, endTime).toHours() <= 3
                && Duration.between(startTime, endTime).toHours() >= 1;

        return isWholeHour && duration && startTime.isBefore(endTime);
    }

}
