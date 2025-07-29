package com.jakan.incident_service.service;


import com.jakan.incident_service.dto.SLADTO;
import com.jakan.incident_service.entity.ActionHistory;
import com.jakan.incident_service.entity.Attachment;
import com.jakan.incident_service.entity.Commentaire;
import com.jakan.incident_service.entity.Incident;
import com.jakan.incident_service.enums.StatutIncident;
import com.jakan.incident_service.feign.SlaServiceClient;
import com.jakan.incident_service.repository.ActionHistoryRepository;
import com.jakan.incident_service.repository.AttachmentRepository;
import com.jakan.incident_service.repository.CommentaireRepository;
import com.jakan.incident_service.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class IncidentServiceImpl {

    private final IncidentRepository incidentRepository;
    private final CommentaireRepository commentRepository;
    private final AttachmentRepository attachmentRepository;
    private final ActionHistoryRepository actionHistoryRepository;
    private final RestTemplate restTemplate;
    private final SlaServiceClient slaServiceClient;

    public Incident createIncident(Incident incident, Long reporterId) {
        incident.setReporterId(reporterId);
        incident.setCreationDate(new Date());
        incident.setStatus(StatutIncident.BACKLOG);

        Incident savedIncident = incidentRepository.save(incident);
        logAction(savedIncident.getId(), reporterId, "Incident créé");

        return savedIncident;
    }

    public Optional<Incident> getIncidentById(Long id) {
        return incidentRepository.findById(id);
    }

    public Incident updateIncident(Long id, Incident incidentDetails) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident non trouvé"));

        incident.setTitle(incidentDetails.getTitle());
        incident.setDescription(incidentDetails.getDescription());
        incident.setPriority(incidentDetails.getPriority());

        return incidentRepository.save(incident);
    }


    public int getPrediction(List<Double> features) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("features", features);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "http://prediction-service:5000/predict", payload, Map.class);

        return (int) response.getBody().get("prediction");
    }
    public List<Incident> getAllResolvedWithTeams() {
        return incidentRepository.findAllResolvedWithTeams();
    }
    public void deleteIncident(Long id) {
        incidentRepository.deleteById(id);
        logAction(id, null, "Incident supprimé");
    }

    // Gestion des statuts
    public Incident updateStatus(Long id, StatutIncident status) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident non trouvé"));

        incident.setStatus(status);
        return incidentRepository.save(incident);
    }

    // Gestion des commentaires
    public Commentaire addComment(Long incidentId, String content, Long userId) {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident non trouvé"));

        Commentaire comment = new Commentaire();
        comment.setContent(content);
        comment.setCreationDate(new Date());
        comment.setAuthorId(userId);
        comment.setIncident(incident);

        return commentRepository.save(comment);
    }

    public List<Commentaire> getCommentsByIncidentId(Long incidentId) {
        return commentRepository.findByIncidentId(incidentId);
    }

    // Gestion des pièces jointes (version simplifiée)
    public Attachment addAttachment(Long incidentId, MultipartFile file, Long userId) throws IOException {
        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incident non trouvé"));

        Attachment attachment = new Attachment();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFileType(file.getContentType());
        attachment.setFileData(file.getBytes()); // Stockage direct dans la base (pour simplifier)
        attachment.setIncident(incident);
        attachment.setUploaderId(userId);
        attachment.setUploadDate(new Date());

        return attachmentRepository.save(attachment);
    }

    public void exportIncidentsForML(String filePath) throws IOException {
        List<Incident> incidents = incidentRepository.findAll();

        FileWriter writer = new FileWriter(filePath);
        writer.write("incident_id,type_incident,priorite,response_time,resolution_time,max_response,max_resolution,violation\n");

        for (Incident incident : incidents) {
            if (incident.getCreationDate() == null || incident.getResponseDate() == null || incident.getResolutionDate() == null)
                continue;

            Duration timeToResponse = Duration.between(incident.getCreationDate().toInstant(), incident.getResponseDate().toInstant());
            Duration timeToResolution = Duration.between(incident.getCreationDate().toInstant(), incident.getResolutionDate().toInstant());

            SLADTO sla = slaServiceClient.getSlaByIncidentType(incident.getIncidentType());

            long responseMin = timeToResponse.toMinutes();
            long resolutionMin = timeToResolution.toMinutes();
            long maxResponse = sla.getMaxResponseTime().toMinutes();
            long maxResolution = sla.getMaxResolutionTime().toMinutes();

            int violation = (responseMin > maxResponse || resolutionMin > maxResolution) ? 1 : 0;

            writer.write(String.format("%d,%s,%s,%d,%d,%d,%d,%d\n",
                    incident.getId(),
                    incident.getIncidentType(),
                    incident.getPriority(),
                    responseMin,
                    resolutionMin,
                    maxResponse,
                    maxResolution,
                    violation
            ));
        }

        writer.flush();
        writer.close();
    }


    public List<Attachment> getAttachmentsByIncidentId(Long incidentId) {
        return attachmentRepository.findByIncidentId(incidentId);
    }

    // Historique des actions
    public List<ActionHistory> getActionHistoryByIncidentId(Long incidentId) {
        return actionHistoryRepository.findByIncidentId(incidentId);
    }

    // Méthode utilitaire pour logger les actions
    private void logAction(Long incidentId, Long userId, String action) {
        ActionHistory history = new ActionHistory();
        history.setIncident(incidentRepository.getReferenceById(incidentId));
        history.setUserId(userId);
        history.setDate(new Date());
        history.setAction(action);
        actionHistoryRepository.save(history);
    }
}