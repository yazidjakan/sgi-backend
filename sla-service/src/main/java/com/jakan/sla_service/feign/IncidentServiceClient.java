package com.jakan.sla_service.feign;


import com.jakan.sla_service.dto.IncidentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "incident-service")
public interface IncidentServiceClient {
    @GetMapping("/api/incidents/by-type")
    List<IncidentDTO> getIncidentsByType(@RequestParam String type);
}
