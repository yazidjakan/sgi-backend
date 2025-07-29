package com.jakan.sla_service.dto;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class SLAViolationDTO {
    private Long incidentId;
    private String incidentType;
    private LocalDateTime violationTime;
    private String violationType; // RESPONSE or RESOLUTION
    private Duration exceededBy;
}
