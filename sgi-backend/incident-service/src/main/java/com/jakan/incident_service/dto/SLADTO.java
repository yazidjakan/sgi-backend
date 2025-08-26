package com.jakan.incident_service.dto;

import lombok.Data;

import java.time.Duration;

@Data
public class SLADTO {
    private Long id;
    private String incidentType;
    private Duration maxResponseTime;
    private Duration maxResolutionTime;
}
