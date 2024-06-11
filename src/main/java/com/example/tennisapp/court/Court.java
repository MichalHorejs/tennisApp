package com.example.tennisapp.court;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "COURT")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COURTID")
    private Long courtId;

//  TODO: Error message for invalid surface
    @NotNull(message = "Surface cannot be null.")
    @Enumerated(EnumType.STRING)
    @Column(name = "SURFACE")
    private Surface surface;

    @JsonIgnore
    @Column(name = "ISDELETED")
    private Boolean isDeleted = false;

    public Court(Surface surface) {
        this.surface = surface;
    }
}
