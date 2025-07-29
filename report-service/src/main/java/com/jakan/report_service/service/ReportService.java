package com.jakan.report_service.service;

import com.jakan.report_service.dto.ReportRequest;
import com.jakan.report_service.dto.ReportResultDTO;
import com.jakan.report_service.feign.IncidentServiceClient;
import com.jakan.report_service.feign.SLAServiceClient;
import com.jakan.report_service.model.ReportTemplate;
import com.jakan.report_service.repository.ReportTemplateRepository;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportTemplateRepository templateRepository;
    private final IncidentServiceClient incidentServiceClient;
    private final SLAServiceClient slaServiceClient;

    public ReportResultDTO generateReport(ReportRequest request) {
        ReportTemplate template = templateRepository.findByTemplateName(request.getTemplateName())
                .orElseThrow(() -> new RuntimeException("Template not found"));

        try {
            InputStream reportStream = getClass().getResourceAsStream(template.getFilePath());
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            Map<String, Object> parameters = new HashMap<>(request.getParameters());
            // Ajout des données supplémentaires
            parameters.put("LOGO_PATH", getClass().getResource("/reports/logo.png").getPath());

            // Remplissage avec données des autres services
            if (request.getParameters().containsKey("incidentId")) {
                parameters.put("incidentData",
                        incidentServiceClient.getIncidentData(
                                Long.valueOf(request.getParameters().get("incidentId").toString())));
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters,
                    new JREmptyDataSource());

            byte[] content;
            String contentType;

            switch (request.getFormat()) {
                case PDF:
                    content = JasperExportManager.exportReportToPdf(jasperPrint);
                    contentType = "application/pdf";
                    break;

                case HTML:
                    ByteArrayOutputStream htmlOut = new ByteArrayOutputStream();
                    HtmlExporter htmlExporter = new HtmlExporter();
                    htmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(htmlOut));
                    htmlExporter.exportReport();

                    content = htmlOut.toByteArray();
                    contentType = "text/html";
                    break;

                case EXCEL:
                    ByteArrayOutputStream excelOut = new ByteArrayOutputStream();
                    JRXlsxExporter exporter = new JRXlsxExporter();
                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(excelOut));

                    SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                    configuration.setOnePagePerSheet(false);
                    configuration.setDetectCellType(true);
                    configuration.setCollapseRowSpan(false);
                    exporter.setConfiguration(configuration);

                    exporter.exportReport();
                    content = excelOut.toByteArray();
                    contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                    break;

                default:
                    throw new RuntimeException("Unsupported format: " + request.getFormat());
            }

            return new ReportResultDTO(
                    content,
                    template.getTemplateName() + "." + request.getFormat().name().toLowerCase(),
                    contentType
            );

        } catch (Exception e) {
            throw new RuntimeException("Report generation failed", e);
        }
    }
}
