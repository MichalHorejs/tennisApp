package com.example.tennisapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;

/**
 * This class represents a reservation in the tennis court booking system.
 * It contains the reservation ID, user, court, date, start time, end time,
 * a flag indicating if the reservation is for doubles, the price,
 * the date when the reservation was created, and a flag indicating if the reservation is deleted.
 */
@Entity
@Table(name = "RESERVATION")
@Data
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESERVATIONID")
    private Long reservationId;

    @ManyToOne
    @JoinColumn(name = "PHONENUMBER", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "COURTID", nullable = false)
    private Court court;

    @Column(name = "DATE")
    private Date date;

    @Column(name = "STARTTIME")
    private Time startTime;

    @Column(name = "ENDTIME")
    private Time endTime;

    @Column(name = "ISDOUBLES")
    private Boolean isDoubles;

    @Column(name = "PRICE")
    private Double price;

    @Column(name = "RESERVATIONCREATED")
    private Date reservationCreated;

    @Column(name = "ISDELETED")
    private Boolean isDeleted = false;


    public Reservation(User user, Court court, Date date, Time startTime,
                       Time endTime, Boolean isDoubles) {
        this.user = user;
        this.court = court;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isDoubles = isDoubles;
        this.price = calculatePrice();
        this.reservationCreated = new Date(System.currentTimeMillis());
    }

    private Double calculatePrice() {
        LocalTime start = startTime.toLocalTime();
        LocalTime end = endTime.toLocalTime();
        long hours = Duration.between(start, end).toHours();

        if (this.isDoubles) {
            return this.court.getPrice() * hours * 1.5;
        }
        return this.court.getPrice() * hours;
    }
}