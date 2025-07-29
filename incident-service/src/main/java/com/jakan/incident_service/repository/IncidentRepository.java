package com.jakan.incident_service.repository;

import com.jakan.incident_service.entity.Incident;
import com.jakan.incident_service.enums.NiveauPriorite;
import com.jakan.incident_service.enums.StatutIncident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByReporterId(Long reporterId);
    List<Incident> findByAssignedTechnicianId(Long technicianId);
    List<Incident> findByStatus(StatutIncident status);
    List<Incident> findByPriority(NiveauPriorite priority);
    List<Incident> findByPriority(String priority);
    @Query("SELECT i FROM Incident i WHERE i.status = 'RESOLU'")
    List<Incident> findAllResolvedWithTeams();
}
