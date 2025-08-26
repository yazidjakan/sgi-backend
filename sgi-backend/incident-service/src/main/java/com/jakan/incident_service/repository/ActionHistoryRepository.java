package com.jakan.incident_service.repository;

import com.jakan.incident_service.entity.ActionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionHistoryRepository extends JpaRepository<ActionHistory, Long> {
    List<ActionHistory> findByIncidentId(Long incidentId);
    List<ActionHistory> findByUserId(Long userId);
    List<ActionHistory> findByIncidentIdOrderByDateDesc(Long incidentId);
}
