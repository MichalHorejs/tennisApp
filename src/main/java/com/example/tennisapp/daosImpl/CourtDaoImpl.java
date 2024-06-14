package com.example.tennisapp.daosImpl;

import com.example.tennisapp.models.Court;
import com.example.tennisapp.daos.CourtDao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    public Optional<Court> getCourtById(Long courtId) {
        String query = "SELECT c FROM Court c WHERE c.id = :courtId AND c.isDeleted = false";
        TypedQuery<Court> typedQuery = entityManager.createQuery(query, Court.class);
        typedQuery.setParameter("courtId", courtId);
        Court court = typedQuery.getResultStream().findFirst().orElse(null);
        return Optional.ofNullable(court);
    }

    @Override
    public void save(Court court) {
        entityManager.persist(court);
    }

    @Override
    public Optional<Court> update(Court toUpdateCourt) {
        Optional<Court> court = getCourtById(toUpdateCourt.getCourtId());

        if (court.isEmpty()) {
            return Optional.empty();
        } else {
            court.get().setSurface(toUpdateCourt.getSurface());
            entityManager.merge(court.get());
            return court;
        }
    }

    @Override
    public Optional<Court> delete(Court toDeleteCourt) {
        Optional<Court> court = getCourtById(toDeleteCourt.getCourtId());

        if (court.isEmpty()) {
            return Optional.empty();
        } else {
            court.get().setIsDeleted(true);
            entityManager.merge(court.get());
            return court;
        }
    }


}
