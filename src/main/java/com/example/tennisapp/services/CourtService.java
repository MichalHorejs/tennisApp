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
        List<Court> courts = this.courtDao.getCourts();
        if (courts.isEmpty()) {
            throw new BadRequestException("Zero courts found");
        }
        return courts;
    }

    public Court getCourtById(Long courtId) {
        return this.courtDao.getCourtById(courtId)
                .orElseThrow(() -> new BadRequestException("Court not found"));
    }

    @Transactional
    public void save(Court court) {
        this.courtDao.save(court);
    }

    @Transactional
    public void update(Court court) {
        this.courtDao.update(court)
                .orElseThrow(() -> new BadRequestException("Court not found"));
    }

    @Transactional
    public void delete(Court court) {
        this.courtDao.delete(court)
                .orElseThrow(() -> new BadRequestException("Court not found"));
    }
}
