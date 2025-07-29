package com.jakan.incident_service.repository;

import com.jakan.incident_service.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByIncidentId(Long incidentId);
    List<Attachment> findByUploaderId(Long uploaderId);
}
