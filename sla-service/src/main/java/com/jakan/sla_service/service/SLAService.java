package com.jakan.sla_service.service;

import com.jakan.sla_service.dto.IncidentDTO;
import com.jakan.sla_service.dto.SLADTO;
import com.jakan.sla_service.dto.SLAMetricDTO;
import com.jakan.sla_service.dto.SLAViolationDTO;
import com.jakan.sla_service.entity.SLA;
import com.jakan.sla_service.entity.SLAMetric;
import com.jakan.sla_service.feign.IncidentServiceClient;
import com.jakan.sla_service.repository.SLAMetricRepository;
import com.jakan.sla_service.repository.SLARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SLAService {
    private final SLARepository slaRepository;
    private final SLAMetricRepository slaMetricRepository;
    private final IncidentServiceClient incidentServiceClient;

    public SLADTO createSLA(SLADTO slaDto) {
        SLA sla = new SLA();
        sla.setIncidentType(slaDto.getIncidentType());
        sla.setMaxResponseTime(slaDto.getMaxResponseTime());
        sla.setMaxResolutionTime(slaDto.getMaxResolutionTime());

        SLA saved = slaRepository.save(sla);
        return convertToDTO(saved);
    }

    public SLADTO getSLAById(Long id) {
        SLA sla = slaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SLA not found"));
        return convertToDTO(sla);
    }

    public SLADTO getSLAByIncidentType(String incidentType) {
        SLA sla = slaRepository.findByIncidentType(incidentType)
                .orElseThrow(() -> new RuntimeException("SLA not found for type: " + incidentType));
        return convertToDTO(sla);
    }

    @Transactional
    public SLAMetricDTO calculateSLAMetrics(Long slaId) {
        SLA sla = slaRepository.findById(slaId)
                .orElseThrow(() -> new RuntimeException("SLA not found"));

        List<IncidentDTO> incidents = incidentServiceClient
                .getIncidentsByType(sla.getIncidentType());

        long total = incidents.size();
        long withinSLA = incidents.stream()
                .filter(i -> isWithinSLA(i, sla))
                .count();

        double complianceRate = total > 0 ? (double) withinSLA / total * 100 : 100.0;

        SLAMetric metric = new SLAMetric();
        metric.setSla(sla);
        metric.setCalculationDate(new Date());
        metric.setTotalIncidents((int) total);
        metric.setIncidentsWithinSLA((int) withinSLA);
        metric.setComplianceRate(complianceRate);

        SLAMetric saved = slaMetricRepository.save(metric);
        return convertToDTO(saved);
    }

    public List<SLAViolationDTO> detectSLAViolations() {
        List<SLAViolationDTO> violations = new ArrayList<>();
        List<SLA> allSLAs = slaRepository.findAll();

        for (SLA sla : allSLAs) {
            List<IncidentDTO> incidents = incidentServiceClient
                    .getIncidentsByType(sla.getIncidentType());

            incidents.forEach(incident -> {
                checkForViolation(incident, sla).ifPresent(violations::add);
            });
        }

        return violations;
    }

    private boolean isWithinSLA(IncidentDTO incident, SLA sla) {
        Duration timeToResponse = incident.getTimeToResponse();
        Duration timeToResolution = incident.getTimeToResolution();

        boolean responseOK = timeToResponse != null &&
                !timeToResponse.minus(sla.getMaxResponseTime()).isNegative();

        boolean resolutionOK = timeToResolution != null &&
                !timeToResolution.minus(sla.getMaxResolutionTime()).isNegative();


        return responseOK && resolutionOK;
    }


    private Optional<SLAViolationDTO> checkForViolation(IncidentDTO incident, SLA sla) {
        Duration timeToResponse = incident.getTimeToResponse();
        Duration timeToResolution = incident.getTimeToResolution();

        if (timeToResponse != null &&
                timeToResponse.compareTo(sla.getMaxResponseTime()) > 0) {

            Duration exceededBy = timeToResponse.minus(sla.getMaxResponseTime());

            SLAViolationDTO violation = new SLAViolationDTO();
            violation.setIncidentId(incident.getId());
            violation.setIncidentType(incident.getIncidentType());
            violation.setViolationType("RESPONSE");
            violation.setViolationTime(incident.getResponseDate());
            violation.setExceededBy(exceededBy);
            return Optional.of(violation);
        }

        if (timeToResolution != null &&
                timeToResolution.compareTo(sla.getMaxResolutionTime()) > 0) {

            Duration exceededBy = timeToResolution.minus(sla.getMaxResolutionTime());

            SLAViolationDTO violation = new SLAViolationDTO();
            violation.setIncidentId(incident.getId());
            violation.setIncidentType(incident.getIncidentType());
            violation.setViolationType("RESOLUTION");
            violation.setViolationTime(incident.getResolutionDate());
            violation.setExceededBy(exceededBy);
            return Optional.of(violation);
        }

        return Optional.empty(); // Pas de violation
    }


    private String buildViolationReason(boolean responseViolated, boolean resolutionViolated) {
        if (responseViolated && resolutionViolated) {
            return "Dépassement des temps de réponse et de résolution";
        } else if (responseViolated) {
            return "Dépassement du temps de réponse";
        } else if (resolutionViolated) {
            return "Dépassement du temps de résolution";
        } else {
            return "Aucune violation";
        }
    }


    public List<SLAMetricDTO> getSLAMetrics(Long id, Date startDate, Date endDate) {
        List<SLAMetric> metrics = slaMetricRepository.findBySla_IdAndCalculationDateBetween(id, startDate, endDate);

        return metrics.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private SLADTO convertToDTO(SLA sla) {
        SLADTO dto = new SLADTO();
        dto.setId(sla.getId());
        dto.setIncidentType(sla.getIncidentType());
        dto.setMaxResponseTime(sla.getMaxResponseTime());
        dto.setMaxResolutionTime(sla.getMaxResolutionTime());
        return dto;
    }

    private SLAMetricDTO convertToDTO(SLAMetric metric) {
        SLAMetricDTO dto = new SLAMetricDTO();
        dto.setId(metric.getId());
        dto.setCalculationDate(metric.getCalculationDate());
        dto.setComplianceRate(metric.getComplianceRate());
        dto.setTotalIncidents(metric.getTotalIncidents());
        dto.setIncidentsWithinSLA(metric.getIncidentsWithinSLA());
        dto.setSlaId(metric.getSla().getId());
        return dto;
    }
}
