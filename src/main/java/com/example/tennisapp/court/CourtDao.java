package com.example.tennisapp.court;

import java.util.List;

public interface CourtDao {

    List<Court> getCourts();
    Court getCourtById(Long courtId);
    void addCourt(Court court);
}
