package com.jakan.incident_service.entity;

import com.jakan.incident_service.enums.NiveauPriorite;
import com.jakan.incident_service.enums.StatutIncident;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "incidents")
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private NiveauPriorite priority; // Critique, Élevée, Moyenne, Faible

    private String incidentType; // Type d'incident

    @Column(name = "reporter_id", nullable = false)
    private Long reporterId;

    @Column(name = "assigned_technician_id")
    private Long assignedTechnicianId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "response_date")
    private Date responseDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "resolution_date")
    private Date resolutionDate;

    @Enumerated(EnumType.STRING)
    private StatutIncident status;
}
