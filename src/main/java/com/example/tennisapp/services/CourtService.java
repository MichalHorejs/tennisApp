package com.example.tennisapp.services;

import com.example.tennisapp.daos.CourtDao;
import com.example.tennisapp.dtos.court.CourtDeleteDto;
import com.example.tennisapp.dtos.court.CourtPutDto;
import com.example.tennisapp.exceptions.BadRequestException;
import com.example.tennisapp.models.Court;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class represents the service layer for the Court entity.
 * It contains methods for retrieving, saving, updating, and deleting courts.
 */
@Service
@AllArgsConstructor
public class CourtService {

    private final CourtDao courtDao;

    /**
     * This method retrieves all courts from the database.
     * If no courts are found, it throws a BadRequestException.
     * @return A list of all courts.
     */
    public List<Court> getCourts() {
        List<Court> courts = this.courtDao.getCourts();
        if (courts.isEmpty()) {
            throw new BadRequestException("Zero courts found");
        }
        return courts;
    }

    /**
     * This method retrieves a court by its ID.
     * If the court is not found, it throws a BadRequestException.
     * @param courtId The ID of the court.
     * @return The court with the given ID.
     */
    public Court getCourtById(Long courtId) {
        return this.courtDao.getCourtById(courtId)
                .orElseThrow(() -> new BadRequestException("Court not found"));
    }

    /**
     * This method saves a court to the database.
     * @param court The court to save.
     */
    public void save(Court court) {
        this.courtDao.save(court);
    }

    /**
     * This method updates a court in the database.
     * It first retrieves the court by its ID, then updates its surface and price, and finally saves the updated court.
     * @param courtPutDto The DTO (Data Transfer Object) containing the new surface and price for the court.
     */
    public void update(CourtPutDto courtPutDto) {
        Court court = this.getCourtById(courtPutDto.getCourtId());
        court.setSurface(courtPutDto.getSurface());
        court.setPrice(courtPutDto.getPrice());
        this.courtDao.update(court);
    }

    /**
     * This method deletes a court from the database.
     * It first retrieves the court by its ID, then sets its isDeleted flag to true, and finally saves the updated court.
     * @param courtDeleteDto The DTO (Data Transfer Object) containing the ID of the court to delete.
     */
    public void delete(CourtDeleteDto courtDeleteDto) {
        Court court = this.getCourtById(courtDeleteDto.getCourtId());
        court.setIsDeleted(true);
        this.courtDao.update(court);
    }
}
