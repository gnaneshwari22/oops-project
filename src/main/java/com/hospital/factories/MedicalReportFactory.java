package com.hospital.factories;

import com.hospital.models.MedicalReport;
import java.util.UUID;

public class MedicalReportFactory {
    
    public static MedicalReport createReport(String patientId, String reportType, String content, String generatedBy) {
        String reportId = "RPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        switch (reportType.toUpperCase()) {
            case "BLOOD_TEST":
                return createBloodTestReport(reportId, patientId, content, generatedBy);
            case "XRAY":
                return createXRayReport(reportId, patientId, content, generatedBy);
            case "MRI":
                return createMRIReport(reportId, patientId, content, generatedBy);
            case "PRESCRIPTION":
                return createPrescriptionReport(reportId, patientId, content, generatedBy);
            case "GENERAL":
            default:
                return createGeneralReport(reportId, patientId, content, generatedBy);
        }
    }
    
    private static MedicalReport createBloodTestReport(String reportId, String patientId, String content, String generatedBy) {
        MedicalReport report = new MedicalReport(reportId, patientId, "Blood Test", content, generatedBy);
        return report;
    }
    
    private static MedicalReport createXRayReport(String reportId, String patientId, String content, String generatedBy) {
        MedicalReport report = new MedicalReport(reportId, patientId, "X-Ray", content, generatedBy);
        return report;
    }
    
    private static MedicalReport createMRIReport(String reportId, String patientId, String content, String generatedBy) {
        MedicalReport report = new MedicalReport(reportId, patientId, "MRI Scan", content, generatedBy);
        return report;
    }
    
    private static MedicalReport createPrescriptionReport(String reportId, String patientId, String content, String generatedBy) {
        MedicalReport report = new MedicalReport(reportId, patientId, "Prescription", content, generatedBy);
        return report;
    }
    
    private static MedicalReport createGeneralReport(String reportId, String patientId, String content, String generatedBy) {
        MedicalReport report = new MedicalReport(reportId, patientId, "General Report", content, generatedBy);
        return report;
    }
}
