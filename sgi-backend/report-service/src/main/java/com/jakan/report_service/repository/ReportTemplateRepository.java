package com.jakan.report_service.repository;

import com.jakan.report_service.model.ReportTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportTemplateRepository extends JpaRepository<ReportTemplate, Long> {

    Optional<ReportTemplate> findByTemplateName(String templateName);

    List<ReportTemplate> findByTemplateNameContainingIgnoreCase(String namePart);

    @Query("SELECT t FROM ReportTemplate t WHERE LOWER(t.description) LIKE LOWER(CONCAT('%', :descriptionPart, '%'))")
    List<ReportTemplate> searchByDescription(String descriptionPart);

    boolean existsByTemplateName(String templateName);

    @Query("SELECT t FROM ReportTemplate t WHERE " +
            "(:reportType IS NULL OR LOWER(t.templateName) LIKE LOWER(CONCAT('%', :reportType, '%')))")
    List<ReportTemplate> findAllByReportType(String reportType);
}
