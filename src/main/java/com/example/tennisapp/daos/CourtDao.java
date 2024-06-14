package com.example.tennisapp.daos;

import com.example.tennisapp.models.Court;

import java.util.List;

public interface CourtDao {

    List<Court> getCourts();
    Court getCourtById(Long courtId);
    void save(Court court);
}
