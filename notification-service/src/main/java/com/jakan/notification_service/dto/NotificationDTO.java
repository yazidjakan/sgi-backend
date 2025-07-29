package com.jakan.notification_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private String recipientEmail;
    private String subject;
    private String content;
    private LocalDateTime sentAt;
    private Boolean isRead;
    private Long relatedIncidentId;
}
