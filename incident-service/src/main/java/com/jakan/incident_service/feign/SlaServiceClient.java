package com.jakan.incident_service.feign;


import com.jakan.incident_service.dto.SLADTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "sla-service")
public interface SlaServiceClient {

    @GetMapping("/api/v1/sla/by-type")
    SLADTO getSlaByIncidentType(@RequestParam("typeIncident") String typeIncident);
}

