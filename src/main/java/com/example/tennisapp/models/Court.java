package com.example.tennisapp.models;

import com.example.tennisapp.enums.Surface;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "COURT")
@Data
@NoArgsConstructor
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COURTID")
    private Long courtId;

    @Enumerated(EnumType.STRING)
    @Column(name = "SURFACE")
    private Surface surface;

    @JsonIgnore
    @Column(name = "ISDELETED")
    private Boolean isDeleted = false;

    public Court(Surface surface) {
        this.surface = surface;
    }

    public Court(Long courtId, Surface surface) {
        this.courtId = courtId;
        this.surface = surface;
    }

    public Court(Long courtId) {
        this.courtId = courtId;
    }
}
