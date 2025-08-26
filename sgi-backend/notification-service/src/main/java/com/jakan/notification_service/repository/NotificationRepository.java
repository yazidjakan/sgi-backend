package com.jakan.notification_service.repository;

import com.jakan.notification_service.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientEmail(String email);
    List<Notification> findByRecipientEmailAndIsReadFalse(String email);
    List<Notification> findByRelatedIncidentId(Long incidentId);
}
