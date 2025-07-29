package com.jakan.report_service.dto;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Data
public class SLAReportDataDTO {
    private String incidentType;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private double complianceRate;
    private int totalIncidents;
    private int incidentsWithinSLA;
    private Duration averageResponseTime;
    private Duration averageResolutionTime;
    private List<ViolationSummaryDTO> violations;

    @Data
    public static class ViolationSummaryDTO {
        private String violationType; // RESPONSE_TIME or RESOLUTION_TIME
        private int count;
        private Duration averageExceededBy;
    }
}
