package com.jakan.report_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class IncidentReportDataDTO {
    private Long id;
    private String title;
    private String description;
    private String incidentType;
    private String priority;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime resolutionDate;

    private String status;
    private String reporterName;
    private String assignedTechnicianName;
    private List<CommentDTO> comments;
    private List<AttachmentDTO> attachments;
    private SLADataDTO slaData;

    @Data
    public static class CommentDTO {
        private String author;
        private String content;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime creationDate;
    }

    @Data
    public static class AttachmentDTO {
        private String fileName;
        private String fileType;
    }

    @Data
    public static class SLADataDTO {
        private boolean responseTimeViolated;
        private boolean resolutionTimeViolated;
        private Duration responseTime;
        private Duration resolutionTime;
        private Duration allowedResponseTime;
        private Duration allowedResolutionTime;
    }
}
