package com.example.tennisapp.models;

import com.example.tennisapp.enums.Surface;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class represents a tennis court.
 * It contains the court ID, surface type, price, and a flag indicating if the court is deleted.
 */
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

    @Column(name = "PRICE")
    private Double price;

    @JsonIgnore
    @Column(name = "ISDELETED")
    private Boolean isDeleted = false;

    public Court(Surface surface, Double price) {
        this.surface = surface;
        this.price = price;
    }

}
