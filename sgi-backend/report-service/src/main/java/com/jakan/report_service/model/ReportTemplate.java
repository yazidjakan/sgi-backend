package com.jakan.report_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "report_templates")
@Data
public class ReportTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String templateName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String filePath;
}
