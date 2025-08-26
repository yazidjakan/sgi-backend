package com.jakan.report_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportResultDTO {
    private byte[] content;
    private String fileName;
    private String contentType;
}
