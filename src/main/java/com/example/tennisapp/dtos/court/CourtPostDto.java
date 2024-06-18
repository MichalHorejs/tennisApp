package com.example.tennisapp.dtos.court;

import com.example.tennisapp.enums.Surface;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * This class represents a data transfer object for posting a court.
 * It contains the surface type and price of the court to be added.
 */
@Data
@JsonIgnoreProperties()
public class CourtPostDto {

    @NotNull(message = "Surface cannot be null")
    private Surface surface;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "CourtId must be positive")
    private Double price;

}
