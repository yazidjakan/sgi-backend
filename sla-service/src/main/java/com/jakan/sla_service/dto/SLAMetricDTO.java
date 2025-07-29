package com.jakan.sla_service.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SLAMetricDTO {
    private Long id;
    private Date calculationDate;
    private Double complianceRate;
    private Integer totalIncidents;
    private Integer incidentsWithinSLA;
    private Long slaId;
}
