package com.example.tennisapp.daos;

import com.example.tennisapp.enums.Surface;
import com.example.tennisapp.models.Court;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface CourtDao {

    List<Court> getCourts();
    Optional<Court> getCourtById(Long courtId);
    void save(Court court);
    Optional<Court> update(Court court);
    Optional<Court> delete(Court court);
}
