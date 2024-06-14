package com.example.tennisapp.dtos;

import com.example.tennisapp.enums.Surface;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = false)
public class CourtPutDto {

    @NotNull(message = "CourtId cannot be null")
    @Positive(message = "CourtId must be positive")
    private Long courtId;

    @NotNull(message = "Surface cannot be null")
    private Surface surface;
}
