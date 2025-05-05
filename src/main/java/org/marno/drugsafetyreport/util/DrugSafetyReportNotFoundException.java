package org.marno.drugsafetyreport.util;

public class DrugSafetyReportNotFoundException extends RuntimeException {
    public DrugSafetyReportNotFoundException(Long id) {
        super("Could not find Drug safety report with id " + id);
    }
}
