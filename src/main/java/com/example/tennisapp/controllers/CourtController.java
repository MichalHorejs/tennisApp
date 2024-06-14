package com.example.tennisapp.controllers;

import com.example.tennisapp.dtos.court.CourtDeleteDto;
import com.example.tennisapp.dtos.court.CourtPutDto;
import com.example.tennisapp.dtos.court.CourtPostDto;
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
    public ResponseEntity<?> save(@Valid @RequestBody CourtPostDto courtPostDto) {
        this.courtService.save(
                new Court(courtPostDto.getSurface())
        );
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody CourtPutDto courtPutDto) {
        this.courtService.update(
                new Court(courtPutDto.getCourtId(), courtPutDto.getSurface())
        );
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@Valid @RequestBody CourtDeleteDto courtDeleteDto) {
        this.courtService.delete(
                new Court(courtDeleteDto.getCourtId())
        );
        return ResponseEntity.ok().build();
    }

}
