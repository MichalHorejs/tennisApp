package com.example.tennisapp.dtos.court;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * This class represents a data transfer object for deleting a court.
 * It contains the ID of the court to be deleted.
 */
@Data
@JsonIgnoreProperties()
public class CourtDeleteDto {

    @NotNull(message = "CourtId cannot be null")
    @Positive(message = "CourtId must be positive")
    private Long courtId;

}
