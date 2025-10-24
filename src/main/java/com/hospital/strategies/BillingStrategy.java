package com.hospital.strategies;

public interface BillingStrategy {
    double calculateBill(double baseFee);
    String getBillingType();
    String generateBillDetails(double baseFee);
}
