package com.example.tennisapp.court;

import com.example.tennisapp.exceptions.BadRequestException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourtService {

    private final CourtDao courtDao;

    @Autowired
    public CourtService(CourtDao courtDao) {
        this.courtDao = courtDao;
    }

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
        this.courtDao.addCourt(court);
    }
}
