package com.jakan.sla_service.repository;

import com.jakan.sla_service.entity.SLA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SLARepository extends JpaRepository<SLA, Long> {
    Optional<SLA> findByIncidentType(String incidentType);
    boolean existsByIncidentType(String incidentType);
}
