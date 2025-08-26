package com.jakan.incident_service.controller;

import com.jakan.incident_service.entity.ActionHistory;
import com.jakan.incident_service.entity.Attachment;
import com.jakan.incident_service.entity.Commentaire;
import com.jakan.incident_service.entity.Incident;
import com.jakan.incident_service.enums.StatutIncident;
import com.jakan.incident_service.service.IncidentServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/incidents")
public class IncidentController {
    private final IncidentServiceImpl incidentService;

    public IncidentController(IncidentServiceImpl incidentService) {
        this.incidentService = incidentService;
    }

    @PostMapping
    public ResponseEntity<Incident> createIncident(
            @RequestBody Incident incident,
            @RequestHeader("X-User-ID") Long reporterId) {
        return ResponseEntity.ok(incidentService.createIncident(incident, reporterId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncident(@PathVariable Long id) {
        Optional<Incident> incident = incidentService.getIncidentById(id);
        return incident.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/export-csv")
    public ResponseEntity<String> exportCsv() throws IOException {
        incidentService.exportIncidentsForML("incident_data.csv");
        return ResponseEntity.ok("Export terminé.");
    }

    @GetMapping("/download-csv")
    public ResponseEntity<Resource> downloadCsv() {
        File file = new File("incident_data.csv");
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=incident_data.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Incident> updateIncident(
            @PathVariable Long id,
            @RequestBody Incident incident) {
        return ResponseEntity.ok(incidentService.updateIncident(id, incident));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
        incidentService.deleteIncident(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/incidents/resolved-for-ai")
    public ResponseEntity<List<Incident>> getAllResolvedForAI() {
        List<Incident> resolvedIncidents = incidentService.getAllResolvedWithTeams();
        return ResponseEntity.ok(resolvedIncidents);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Incident> updateStatus(
            @PathVariable Long id,
            @RequestParam StatutIncident status) {
        return ResponseEntity.ok(incidentService.updateStatus(id, status));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Commentaire> addComment(
            @PathVariable Long id,
            @RequestParam String content,
            @RequestHeader("X-User-ID") Long userId) {
        return ResponseEntity.ok(incidentService.addComment(id, content, userId));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Commentaire>> getComments(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.getCommentsByIncidentId(id));
    }

    @PostMapping("/{id}/attachments")
    public ResponseEntity<Attachment> addAttachment(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestHeader("X-User-ID") Long userId) throws IOException {
        return ResponseEntity.ok(incidentService.addAttachment(id, file, userId));
    }

    @GetMapping("/{id}/attachments")
    public ResponseEntity<List<Attachment>> getAttachments(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.getAttachmentsByIncidentId(id));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<ActionHistory>> getActionHistory(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.getActionHistoryByIncidentId(id));
    }
}