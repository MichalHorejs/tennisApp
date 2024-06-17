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

@Repository
@Transactional
public class CourtDaoImpl implements CourtDao {


    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Court> getCourts() {
        String query = "SELECT c FROM Court c WHERE c.isDeleted = false";
        TypedQuery<Court> typedQuery = entityManager.createQuery(query, Court.class);
        return typedQuery.getResultList();
    }

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

    @Override
    public void save(Court court) {
        entityManager.persist(court);
    }

    @Override
    public void update(Court court) {
        entityManager.merge(court);
    }

}
