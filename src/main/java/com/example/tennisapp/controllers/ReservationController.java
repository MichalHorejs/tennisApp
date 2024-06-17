package com.example.tennisapp.controllers;

import com.example.tennisapp.dtos.reservation.*;
import com.example.tennisapp.services.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/reservation")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("phone")
    public ResponseEntity<List<ReservationFullResponse>> getReservationsByPhone(
            @RequestBody ReservationGetPhoneDto reservationGetPhoneDto
    ) {
        List<ReservationFullResponse> reservations = reservationService.getReservationsByPhone(reservationGetPhoneDto);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("court")
    public ResponseEntity<List<ReservationFullResponse>> getReservationsByCourt(
            @RequestBody ReservationGetCourtDto reservationGetCourtDto
    ) {
        List<ReservationFullResponse> reservations = reservationService.getReservationsByCourt(reservationGetCourtDto);
        return ResponseEntity.ok(reservations);
    }


    @PostMapping
    public ResponseEntity<?> save(@RequestBody ReservationPostDto reservationPostDto) {
        ReservationResponse response = this.reservationService.save(reservationPostDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody ReservationPutDto reservationPutDto) {
        ReservationResponse response = this.reservationService.update(reservationPutDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody ReservationDeleteDto reservationDeleteDto) {
        this.reservationService.delete(reservationDeleteDto);
        return ResponseEntity.ok().build();
    }


}
