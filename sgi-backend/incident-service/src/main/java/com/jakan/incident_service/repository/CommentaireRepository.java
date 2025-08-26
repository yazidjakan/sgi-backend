package com.jakan.incident_service.repository;

import com.jakan.incident_service.entity.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentaireRepository extends JpaRepository<Commentaire,Long> {
    List<Commentaire> findByIncidentId(Long incidentId);
    List<Commentaire> findByAuthorId(Long authorId);
}
