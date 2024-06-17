package com.example.tennisapp.daos;

import com.example.tennisapp.models.Court;

import java.util.List;
import java.util.Optional;

public interface CourtDao {

    List<Court> getCourts();
    Optional<Court> getCourtById(Long courtId);
    void save(Court court);
    void update(Court court);
}
