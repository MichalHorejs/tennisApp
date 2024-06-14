package com.example.tennisapp.dtos;

import com.example.tennisapp.enums.Surface;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = false)
public class CourtPostDto {

    @NotNull(message = "Surface cannot be null")
    private Surface surface;

}
