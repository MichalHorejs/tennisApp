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

/**
 * This class is the controller for the Court entity.
 * It has the following endpoints:
 * - GET /api/court
 * - GET /api/court/{courtId}
 * - POST /api/court
 * - PUT /api/court
 * - DELETE /api/court
 */
@RestController
@RequestMapping(path = "api/court")
@AllArgsConstructor
public class CourtController {

    private final CourtService courtService;

    /**
     * Endpoint for getting all courts.
     * @return List of all courts.
     */
    @GetMapping
    public List<Court> getCourt() {
        return this.courtService.getCourts();
    }

    /**
     * Endpoint for getting a court by its ID.
     * @param courtId ID of the court to retrieve.
     * @return The court with the given ID.
     */
    @GetMapping(path = "{courtId}")
    public Court getCourtById(@PathVariable("courtId") Long courtId) {
        return this.courtService.getCourtById(courtId);
    }

    /**
     * Endpoint for saving a new court.
     * @param courtPostDto Data transfer object containing the details of the court to save.
     * @return HTTP response entity.
     */
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody CourtPostDto courtPostDto) {
        this.courtService.save(
                new Court(courtPostDto.getSurface(), courtPostDto.getPrice())
        );
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint for updating an existing court.
     * @param courtPutDto Data transfer object containing the details of the court to update.
     * @return HTTP response entity.
     */
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody CourtPutDto courtPutDto) {
        this.courtService.update(courtPutDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint for deleting a court.
     * @param courtDeleteDto Data transfer object containing the details of the court to delete.
     * @return HTTP response entity.
     */
    @DeleteMapping
    public ResponseEntity<?> delete(@Valid @RequestBody CourtDeleteDto courtDeleteDto) {
        this.courtService.delete(courtDeleteDto);
        return ResponseEntity.ok().build();
    }

}
