package org.marno.drugsafetyreport.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.marno.drugsafetyreport.enums.DrugSafetyReportStatus;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Model class DrugSafetyReport
 */
@Entity
@JsonIdentityInfo(
  generator = ObjectIdGenerators.PropertyGenerator.class, 
  property = "id")
public class DrugSafetyReport {
    @Id 
    @GeneratedValue 
    @JsonProperty("id")
    private Long id;
    
    @NotNull(message = "Reporter name is required")
    @JsonProperty("reporterName")
    private String reporterName;
    
    @NotNull(message = "Product name is required")
    @JsonProperty("productName")
    private String productName;
    
    @NotNull(message = "Description of issue is required")
    @JsonProperty("descriptionOfIssue")
    private String descriptionOfIssue;
    
    @NotNull(message = "Timestamp is required")
    @JsonProperty("timestamp")
    private Timestamp timestamp;
    
    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @JsonProperty("status")
    private DrugSafetyReportStatus status;

    // Default constructor required by JPA
    public DrugSafetyReport() {
    }

    // All-args constructor
    public DrugSafetyReport(Long id, String reporterName, String productName, 
                           String descriptionOfIssue, Timestamp timestamp, 
                           DrugSafetyReportStatus status) {
        this.id = id;
        this.reporterName = reporterName;
        this.productName = productName;
        this.descriptionOfIssue = descriptionOfIssue;
        this.timestamp = timestamp;
        this.status = status;
    }
    
    // Builder pattern for tests
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private Long id;
        private String reporterName;
        private String productName;
        private String descriptionOfIssue;
        private Timestamp timestamp;
        private DrugSafetyReportStatus status;
        
        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        
        public Builder reporterName(String reporterName) {
            this.reporterName = reporterName;
            return this;
        }
        
        public Builder productName(String productName) {
            this.productName = productName;
            return this;
        }
        
        public Builder descriptionOfIssue(String descriptionOfIssue) {
            this.descriptionOfIssue = descriptionOfIssue;
            return this;
        }
        
        public Builder timestamp(Timestamp timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public Builder status(DrugSafetyReportStatus status) {
            this.status = status;
            return this;
        }
        
        public DrugSafetyReport build() {
            return new DrugSafetyReport(id, reporterName, productName, 
                                        descriptionOfIssue, timestamp, status);
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getReporterName() {
        return reporterName;
    }
    
    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getDescriptionOfIssue() {
        return descriptionOfIssue;
    }
    
    public void setDescriptionOfIssue(String descriptionOfIssue) {
        this.descriptionOfIssue = descriptionOfIssue;
    }
    
    public Timestamp getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    
    public DrugSafetyReportStatus getStatus() {
        return status;
    }
    
    public void setStatus(DrugSafetyReportStatus status) {
        this.status = status;
    }
    
    // toString, equals, and hashCode methods
    @Override
    public String toString() {
        return "DrugSafetyReport{" +
                "id=" + id +
                ", reporterName='" + reporterName + '\'' +
                ", productName='" + productName + '\'' +
                ", descriptionOfIssue='" + descriptionOfIssue + '\'' +
                ", timestamp=" + timestamp +
                ", status=" + status +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DrugSafetyReport report = (DrugSafetyReport) o;
        return Objects.equals(id, report.id) &&
               Objects.equals(reporterName, report.reporterName) &&
               Objects.equals(productName, report.productName) &&
               Objects.equals(descriptionOfIssue, report.descriptionOfIssue) &&
               Objects.equals(timestamp, report.timestamp) &&
               status == report.status;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, reporterName, productName, descriptionOfIssue, timestamp, status);
    }
}
