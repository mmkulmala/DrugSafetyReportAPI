package org.marno.drugsafetyreport.repository;

import org.marno.drugsafetyreport.model.DrugSafetyReport;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for DrugSafetyReports
 */
public interface DrugSafetyReportRepository extends JpaRepository<DrugSafetyReport, Long> {
}
