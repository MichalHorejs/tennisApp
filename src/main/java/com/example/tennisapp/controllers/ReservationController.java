package com.example.tennisapp.controllers;

import com.example.tennisapp.dtos.reservation.*;
import com.example.tennisapp.services.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class is the controller for the Reservation entity.
 * It has the following endpoints:
 * - GET /api/reservation/phone
 * - GET /api/reservation/court
 * - POST /api/reservation
 * - PUT /api/reservation
 * - DELETE /api/reservation
 */
@RestController
@RequestMapping(path = "api/reservation")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * This method returns a list of reservations by phone number.
     * @param reservationGetPhoneDto Data Transfer Object for getting a reservation by phone number.
     * @return List<ReservationFullResponse>
     */
    @GetMapping("phone")
    public ResponseEntity<List<ReservationFullResponse>> getReservationsByPhone(
            @RequestBody ReservationGetPhoneDto reservationGetPhoneDto
    ) {
        List<ReservationFullResponse> reservations = reservationService.getReservationsByPhone(reservationGetPhoneDto);
        return ResponseEntity.ok(reservations);
    }

    /**
     * This method returns a list of reservations by court.
     * @param reservationGetCourtDto Data Transfer Object for getting a reservation by court.
     * @return List<ReservationFullResponse>
     */
    @GetMapping("court")
    public ResponseEntity<List<ReservationFullResponse>> getReservationsByCourt(
            @RequestBody ReservationGetCourtDto reservationGetCourtDto
    ) {
        List<ReservationFullResponse> reservations = reservationService.getReservationsByCourt(reservationGetCourtDto);
        return ResponseEntity.ok(reservations);
    }


    /**
     * This method saves a reservation.
     * @param reservationPostDto Data Transfer Object for saving a reservation.
     * @return ReservationResponse
     */
    @PostMapping
    public ResponseEntity<?> save(@RequestBody ReservationPostDto reservationPostDto) {
        ReservationResponse response = this.reservationService.save(reservationPostDto);
        return ResponseEntity.ok(response);
    }

    /**
     * This method updates a reservation.
     * @param reservationPutDto Data Transfer Object for updating a reservation.
     * @return ReservationResponse
     */
    @PutMapping
    public ResponseEntity<?> update(@RequestBody ReservationPutDto reservationPutDto) {
        ReservationResponse response = this.reservationService.update(reservationPutDto);
        return ResponseEntity.ok(response);
    }

    /**
     * This method deletes a reservation.
     * @param reservationDeleteDto Data Transfer Object for deleting a reservation.
     * @return ResponseEntity<?>
     */
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody ReservationDeleteDto reservationDeleteDto) {
        this.reservationService.delete(reservationDeleteDto);
        return ResponseEntity.ok().build();
    }


}
