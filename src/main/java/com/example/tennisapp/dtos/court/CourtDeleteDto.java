package com.example.tennisapp.dtos.court;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@JsonIgnoreProperties()
public class CourtDeleteDto {

    @NotNull(message = "CourtId cannot be null")
    @Positive(message = "CourtId must be positive")
    private Long courtId;

}