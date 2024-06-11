package com.example.tennisapp.court;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/court")
public class CourtController {

    private final CourtService courtService;

    @Autowired
    public CourtController(CourtService courtService) {
        this.courtService = courtService;
    }

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
