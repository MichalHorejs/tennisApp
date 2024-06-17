package com.example.tennisapp.services;

import com.example.tennisapp.daos.CourtDao;
import com.example.tennisapp.dtos.court.CourtDeleteDto;
import com.example.tennisapp.dtos.court.CourtPutDto;
import com.example.tennisapp.exceptions.BadRequestException;
import com.example.tennisapp.models.Court;
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

    public void save(Court court) {
        this.courtDao.save(court);
    }

    public void update(CourtPutDto courtPutDto) {
        Court court = this.getCourtById(courtPutDto.getCourtId());
        court.setSurface(courtPutDto.getSurface());
        court.setPrice(courtPutDto.getPrice());
        this.courtDao.update(court);
    }

    public void delete(CourtDeleteDto courtDeleteDto) {
        Court court = this.getCourtById(courtDeleteDto.getCourtId());
        court.setIsDeleted(true);
        this.courtDao.update(court);
    }
}
