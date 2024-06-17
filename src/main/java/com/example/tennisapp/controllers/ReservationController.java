package com.example.tennisapp.controllers;

import com.example.tennisapp.dtos.reservation.ReservationDeleteDto;
import com.example.tennisapp.dtos.reservation.ReservationPostDto;
import com.example.tennisapp.dtos.reservation.ReservationPutDto;
import com.example.tennisapp.services.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/reservation")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ReservationPostDto reservationPostDto) {

        this.reservationService.save(reservationPostDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody ReservationPutDto reservationPutDto) {
        this.reservationService.update(reservationPutDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestBody ReservationDeleteDto reservationDeleteDto) {
        this.reservationService.delete(reservationDeleteDto);
        return ResponseEntity.ok().build();
    }


}
