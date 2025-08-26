package com.jakan.sla_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class IncidentDTO {
    private Long id;
    private String title;
    private String description;
    private String incidentType;
    private String priority;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime responseDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime resolutionDate;

    private String status;
    private Long reporterId;
    private Long assignedTechnicianId;

    // Calculated fields for SLA checking
    public Duration getTimeToResponse() {
        if (creationDate == null || responseDate == null) {
            return null;
        }
        return Duration.between(creationDate, responseDate);
    }

    public Duration getTimeToResolution() {
        if (creationDate == null || resolutionDate == null) {
            return null;
        }
        return Duration.between(creationDate, resolutionDate);
    }
}
