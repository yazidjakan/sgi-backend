package com.jakan.report_service.feign;

import com.jakan.report_service.dto.IncidentReportDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "incident-service")
public interface IncidentServiceClient {
    @GetMapping("/api/incidents/{id}/report-data")
    IncidentReportDataDTO getIncidentData(@PathVariable Long id);
    @GetMapping("/api/incidents/resolved-for-ai")
    List<IncidentReportDataDTO> getAllResolvedIncidentsForAI();
}
