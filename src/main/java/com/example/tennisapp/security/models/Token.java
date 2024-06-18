package com.example.tennisapp.security.models;

import com.example.tennisapp.models.User;
import jakarta.persistence.*;
import lombok.Data;

/**
 * This class represents the Token entity in the database.
 * It contains fields for the token ID, access token, refresh token, logged out status, and the associated user.
 */
@Entity
@Table(name = "TOKEN")
@Data
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ACCESSTOKEN")
    private String accessToken;

    @Column(name = "REFRESHTOKEN")
    private String refreshToken;

    @Column(name = "LOGGEDOUT")
    private boolean loggedOut;

    @ManyToOne
    @JoinColumn(name = "PHONENUMBER")
    private User user;

}
