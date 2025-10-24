package com.hospital.strategies;

public class CashBilling implements BillingStrategy {
    private double discountPercentage;
    
    public CashBilling() {
        this.discountPercentage = 0.0;
    }
    
    public CashBilling(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    
    @Override
    public double calculateBill(double baseFee) {
        double discount = baseFee * (discountPercentage / 100);
        return baseFee - discount;
    }
    
    @Override
    public String getBillingType() {
        return "Cash";
    }
    
    @Override
    public String generateBillDetails(double baseFee) {
        double discount = baseFee * (discountPercentage / 100);
        double finalAmount = baseFee - discount;
        
        if (discountPercentage > 0) {
            return String.format(
                "Billing Type: Cash\n" +
                "Base Fee: $%.2f\n" +
                "Discount: %.0f%%\n" +
                "Discount Amount: $%.2f\n" +
                "Total Payable: $%.2f",
                baseFee, discountPercentage, discount, finalAmount
            );
        } else {
            return String.format(
                "Billing Type: Cash\n" +
                "Total Payable: $%.2f",
                finalAmount
            );
        }
    }
    
    public double getDiscountPercentage() {
        return discountPercentage;
    }
    
    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}
