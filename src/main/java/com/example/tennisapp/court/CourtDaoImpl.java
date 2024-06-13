package com.example.tennisapp.court;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
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
    public Court getCourtById(Long courtId) {
//        TODO: Query with `isDeleted = false`
        return entityManager.find(Court.class, courtId);
    }

    @Override
    @Transactional
    public void addCourt(Court court) {
        entityManager.persist(court);
    }


}
