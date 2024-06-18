package com.example.tennisapp.security.models;

import com.example.tennisapp.models.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TOKEN")
@Data
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "LOGGEDOUT")
    private boolean loggedOut;

    @ManyToOne
    @JoinColumn(name = "PHONENUMBER")
    private User user;

}
