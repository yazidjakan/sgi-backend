package com.jakan.report_service.dto;


import lombok.Data;

import java.util.Map;

@Data
public class ReportRequest {
    private String templateName;
    private Map<String, Object> parameters;
    private ExportFormat format;

    public enum ExportFormat {
        PDF, EXCEL, HTML
    }
}
