package com.jakan.report_service.feign;

import com.jakan.report_service.dto.SLAReportDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "sla-service")
public interface SLAServiceClient {
    @GetMapping("/api/slas/report-data")
    List<SLAReportDataDTO> getSLAData(
            @RequestParam(required = false) String incidentType,
            @RequestParam(required = false) String timeRange);
}
