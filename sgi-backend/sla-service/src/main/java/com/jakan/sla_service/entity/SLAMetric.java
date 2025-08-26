package com.jakan.sla_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "sla_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SLAMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date calculationDate;

    private Double complianceRate; // % de respect du SLA
    private Integer totalIncidents;
    private Integer incidentsWithinSLA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sla_id")
    private SLA sla;
}
