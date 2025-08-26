package com.jakan.sla_service.repository;

import com.jakan.sla_service.entity.SLAMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SLAMetricRepository extends JpaRepository<SLAMetric, Long> {
    List<SLAMetric> findBySlaId(Long slaId);
    List<SLAMetric> findByCalculationDateBetween(Date startDate, Date endDate);
    //List<SLAMetric> findBySlaIdAndDateRange(Long slaId, Date startDate, Date endDate);
    List<SLAMetric> findBySla_IdAndCalculationDateBetween(Long slaId, Date start, Date end);

}
