package com.example.tennisapp.controllers;

import com.example.tennisapp.models.Court;
import com.example.tennisapp.services.CourtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/court")
@AllArgsConstructor
public class CourtController {

    private final CourtService courtService;

    @GetMapping
    public List<Court> getCourt() {
        return this.courtService.getCourts();
    }

    @GetMapping(path = "{courtId}")
    public Court getCourtById(@PathVariable("courtId") Long courtId) {
        return this.courtService.getCourtById(courtId);
    }

    @PostMapping
    public ResponseEntity<?> addCourt(@Valid @RequestBody Court court) {
        this.courtService.addCourt(court);
        return ResponseEntity.ok().build();
    }

}
