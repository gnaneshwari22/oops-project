package com.hospital.strategies;

public class InsuranceBilling implements BillingStrategy {
    private String insuranceProvider;
    private String policyNumber;
    private double coveragePercentage;
    
    public InsuranceBilling(String insuranceProvider, String policyNumber, double coveragePercentage) {
        this.insuranceProvider = insuranceProvider;
        this.policyNumber = policyNumber;
        this.coveragePercentage = coveragePercentage;
    }
    
    @Override
    public double calculateBill(double baseFee) {
        double insuranceCoverage = baseFee * (coveragePercentage / 100);
        double patientPayable = baseFee - insuranceCoverage;
        return patientPayable;
    }
    
    @Override
    public String getBillingType() {
        return "Insurance";
    }
    
    @Override
    public String generateBillDetails(double baseFee) {
        double insuranceCoverage = baseFee * (coveragePercentage / 100);
        double patientPayable = baseFee - insuranceCoverage;
        
        return String.format(
            "Billing Type: Insurance\n" +
            "Insurance Provider: %s\n" +
            "Policy Number: %s\n" +
            "Base Fee: $%.2f\n" +
            "Coverage: %.0f%%\n" +
            "Insurance Pays: $%.2f\n" +
            "Patient Pays: $%.2f",
            insuranceProvider, policyNumber, baseFee, coveragePercentage, insuranceCoverage, patientPayable
        );
    }
    
    public String getInsuranceProvider() {
        return insuranceProvider;
    }
    
    public String getPolicyNumber() {
        return policyNumber;
    }
    
    public double getCoveragePercentage() {
        return coveragePercentage;
    }
}
