package com.jakan.report_service.controller;

import com.jakan.report_service.dto.IncidentReportDataDTO;
import com.jakan.report_service.dto.ReportRequest;
import com.jakan.report_service.dto.ReportResultDTO;
import com.jakan.report_service.feign.IncidentServiceClient;
import com.jakan.report_service.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    private final IncidentServiceClient incidentServiceClient;

    @GetMapping("/incidents/csv")
    public ResponseEntity<Resource> exportIncidentsForPrediction() {
        // 1. Récupération des données
        List<IncidentReportDataDTO> incidents = incidentServiceClient.getAllResolvedIncidentsForAI();

        // 2. Génération du CSV en mémoire
        ByteArrayResource resource = new ByteArrayResource(generateCsvContent(incidents));

        // 3. Préparation de la réponse
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=incidents_for_ai_training.csv")
                .contentLength(resource.contentLength())
                .body(resource);
    }

    private byte[] generateCsvContent(List<IncidentReportDataDTO> incidents) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(out)) {

            // En-têtes CSV
            writer.println("type_incident,priorite,temps_creation,equipe_assignee,temps_reel_resolution,sla_respecte");

            // Données
            incidents.forEach(incident -> {
                long creationHours = ChronoUnit.HOURS.between(
                        incident.getCreationDate(),
                        incident.getResolutionDate()
                );

                writer.println(String.join(",",
                        escapeCsv(incident.getIncidentType()),
                        escapeCsv(incident.getPriority()),
                        String.valueOf(ChronoUnit.HOURS.between(
                                incident.getCreationDate(),
                                LocalDateTime.now()
                        )),
                        escapeCsv(incident.getAssignedTechnicianName()),
                        incident.getResolutionDate() != null ?
                                String.valueOf(ChronoUnit.HOURS.between(
                                        incident.getCreationDate(),
                                        incident.getResolutionDate()
                                )) : "N/A",
                        (incident.getResolutionDate() != null &&
                                incident.getSlaData() != null &&
                                incident.getSlaData().getAllowedResolutionTime() != null &&
                                incident.getResolutionDate().isBefore(
                                        incident.getCreationDate().plus(incident.getSlaData().getAllowedResolutionTime())
                                )
                        ) ? "1" : "0"
                ));
            });

            writer.flush();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("CSV generation failed", e);
        }
    }

    private String escapeCsv(String value) {
        return value.contains(",") ? "\"" + value + "\"" : value;
    }

    @PostMapping
    public ResponseEntity<byte[]> generateReport(@RequestBody ReportRequest request) {
        ReportResultDTO result = reportService.generateReport(request);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(result.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + result.getFileName() + "\"")
                .body(result.getContent());
    }

    @GetMapping("/incident/{id}")
    public ResponseEntity<byte[]> generateIncidentReport(@PathVariable Long id) {
        ReportRequest request = new ReportRequest();
        request.setTemplateName("incident_report");
        request.setFormat(ReportRequest.ExportFormat.PDF);
        request.getParameters().put("incidentId", id);

        return generateReport(request);
    }
}
