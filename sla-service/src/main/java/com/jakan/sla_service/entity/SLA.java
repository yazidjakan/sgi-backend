package com.jakan.sla_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "slas")
public class SLA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String incidentType;

    @Column(nullable = false)
    private Duration maxResponseTime; // Temps max pour première réponse

    @Column(nullable = false)
    private Duration maxResolutionTime; // Temps max pour résolution

    @OneToMany(mappedBy = "sla", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SLAMetric> metrics = new ArrayList<>();
}
