package com.hospital.models;

import java.time.LocalDateTime;

public class MedicalReport {
    private String reportId;
    private String patientId;
    private String reportType;
    private String content;
    private String generatedBy;
    private LocalDateTime generatedAt;
    
    public MedicalReport() {
        this.generatedAt = LocalDateTime.now();
    }
    
    public MedicalReport(String reportId, String patientId, String reportType, String content, String generatedBy) {
        this.reportId = reportId;
        this.patientId = patientId;
        this.reportType = reportType;
        this.content = content;
        this.generatedBy = generatedBy;
        this.generatedAt = LocalDateTime.now();
    }
    
    public String getReportId() {
        return reportId;
    }
    
    public void setReportId(String reportId) {
        this.reportId = reportId;
    }
    
    public String getPatientId() {
        return patientId;
    }
    
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    
    public String getReportType() {
        return reportType;
    }
    
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getGeneratedBy() {
        return generatedBy;
    }
    
    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }
    
    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }
    
    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
    
    @Override
    public String toString() {
        return "MedicalReport{" +
                "reportId='" + reportId + '\'' +
                ", patientId='" + patientId + '\'' +
                ", reportType='" + reportType + '\'' +
                ", generatedBy='" + generatedBy + '\'' +
                ", generatedAt=" + generatedAt +
                '}';
    }
}
