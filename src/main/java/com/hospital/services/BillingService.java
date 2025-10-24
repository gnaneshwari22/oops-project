package com.hospital.services;

import com.hospital.strategies.BillingStrategy;
import com.hospital.models.Doctor;

public class BillingService {
    private BillingStrategy billingStrategy;
    
    public BillingService() {
    }
    
    public BillingService(BillingStrategy billingStrategy) {
        this.billingStrategy = billingStrategy;
    }
    
    public void setBillingStrategy(BillingStrategy billingStrategy) {
        this.billingStrategy = billingStrategy;
    }
    
    public double calculateBill(double baseFee) {
        if (billingStrategy == null) {
            return baseFee;
        }
        return billingStrategy.calculateBill(baseFee);
    }
    
    public double calculateBillForDoctor(Doctor doctor) {
        if (billingStrategy == null) {
            return doctor.getConsultationFee();
        }
        return billingStrategy.calculateBill(doctor.getConsultationFee());
    }
    
    public String generateBillDetails(double baseFee) {
        if (billingStrategy == null) {
            return "No billing strategy set. Base Fee: $" + baseFee;
        }
        return billingStrategy.generateBillDetails(baseFee);
    }
    
    public String getBillingType() {
        if (billingStrategy == null) {
            return "No Strategy";
        }
        return billingStrategy.getBillingType();
    }
}
