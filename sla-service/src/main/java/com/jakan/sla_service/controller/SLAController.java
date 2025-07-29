package com.jakan.sla_service.controller;

import com.jakan.sla_service.dto.SLADTO;
import com.jakan.sla_service.dto.SLAMetricDTO;
import com.jakan.sla_service.dto.SLAViolationDTO;
import com.jakan.sla_service.service.SLAService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/slas")
@RequiredArgsConstructor
public class SLAController {
    private final SLAService slaService;

    @PostMapping
    public ResponseEntity<SLADTO> createSLA(@RequestBody SLADTO slaDto) {
        return ResponseEntity.ok(slaService.createSLA(slaDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SLADTO> getSLAById(@PathVariable Long id) {
        return ResponseEntity.ok(slaService.getSLAById(id));
    }

    @GetMapping("/by-type/{incidentType}")
    public ResponseEntity<SLADTO> getSLAByType(@PathVariable String incidentType) {
        return ResponseEntity.ok(slaService.getSLAByIncidentType(incidentType));
    }

    @PostMapping("/{id}/calculate-metrics")
    public ResponseEntity<SLAMetricDTO> calculateMetrics(@PathVariable Long id) {
        return ResponseEntity.ok(slaService.calculateSLAMetrics(id));
    }

    @GetMapping("/violations")
    public ResponseEntity<List<SLAViolationDTO>> detectViolations() {
        return ResponseEntity.ok(slaService.detectSLAViolations());
    }

    @GetMapping("/{id}/metrics")
    public ResponseEntity<List<SLAMetricDTO>> getMetrics(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {

        // Implémentez la récupération des métriques avec filtres temporels
        return ResponseEntity.ok(slaService.getSLAMetrics(id, startDate, endDate));
    }
}
