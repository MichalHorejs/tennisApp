package com.example.tennisapp.services;

import com.example.tennisapp.models.Court;
import com.example.tennisapp.daos.CourtDao;
import com.example.tennisapp.exceptions.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourtService {

    private final CourtDao courtDao;

    public List<Court> getCourts() {
        return this.courtDao.getCourts();
    }

    public Court getCourtById(Long courtId) {

        Court court = this.courtDao.getCourtById(courtId);

        if (court == null) {
            throw new BadRequestException("Court with id " + courtId + " does not exist.");
        }
        return court;
    }

    @Transactional
    public void addCourt(Court court) {
        this.courtDao.save(court);
    }
}
