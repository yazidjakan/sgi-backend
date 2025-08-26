package com.jakan.notification_service.entity.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmailRequest {
    @NotNull
    @Email
    private String recipientEmail;

    @NotNull
    private String subject;

    @NotNull
    private String content;

    private Long relatedIncidentId;
}