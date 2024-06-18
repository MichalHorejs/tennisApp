package com.example.tennisapp.daosImpl;

import com.example.tennisapp.models.Court;
import com.example.tennisapp.daos.CourtDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This class implements the CourtDao interface.
 * It is used to interact with the database and perform operations on the Court entity.
 */
@Repository
@Transactional
public class CourtDaoImpl implements CourtDao {


    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method returns a list of all the courts in the database.
     * @return List<Court> - a list of all the courts in the database.
     */
    @Override
    public List<Court> getCourts() {
        String query = "SELECT c FROM Court c WHERE c.isDeleted = false";
        TypedQuery<Court> typedQuery = entityManager.createQuery(query, Court.class);
        return typedQuery.getResultList();
    }

    /**
     * This method returns a court from the database given its id.
     * @param courtId - the id of the court to be returned.
     * @return Optional<Court> - the court with the given id.
     */
    @Override
    public Optional<Court> getCourtById(Long courtId) {
        String query = "SELECT c FROM Court c WHERE c.id = :courtId AND c.isDeleted = false";
        Court court = entityManager.createQuery(query, Court.class)
                .setParameter("courtId", courtId)
                .getResultStream()
                .findFirst()
                .orElse(null);
        return Optional.ofNullable(court);
    }

    /**
     * This method saves a court from provided object.
     * @param court - the name of the court to be saved.
     */
    @Override
    public void save(Court court) {
        entityManager.persist(court);
    }

    /**
     * This method updates a court in database from provided object.
     * @param court - the id of the court to be updated.
     */
    @Override
    public void update(Court court) {
        entityManager.merge(court);
    }

}
