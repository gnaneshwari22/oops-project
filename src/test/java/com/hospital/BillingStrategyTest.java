package com.hospital;

import com.hospital.strategies.BillingStrategy;
import com.hospital.strategies.InsuranceBilling;
import com.hospital.strategies.CashBilling;
import com.hospital.services.BillingService;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class BillingStrategyTest {
    
    @Test
    @DisplayName("Test Insurance Billing Strategy")
    public void testInsuranceBilling() {
        BillingStrategy insuranceBilling = new InsuranceBilling("BlueCross", "POL123456", 80.0);
        BillingService billingService = new BillingService(insuranceBilling);
        
        double baseFee = 1000.0;
        double patientPays = billingService.calculateBill(baseFee);
        
        assertEquals(200.0, patientPays, 0.01);
        assertEquals("Insurance", billingService.getBillingType());
    }
    
    @Test
    @DisplayName("Test Cash Billing Strategy with discount")
    public void testCashBillingWithDiscount() {
        BillingStrategy cashBilling = new CashBilling(10.0);
        BillingService billingService = new BillingService(cashBilling);
        
        double baseFee = 1000.0;
        double patientPays = billingService.calculateBill(baseFee);
        
        assertEquals(900.0, patientPays, 0.01);
        assertEquals("Cash", billingService.getBillingType());
    }
    
    @Test
    @DisplayName("Test Cash Billing Strategy without discount")
    public void testCashBillingNoDiscount() {
        BillingStrategy cashBilling = new CashBilling();
        BillingService billingService = new BillingService(cashBilling);
        
        double baseFee = 1000.0;
        double patientPays = billingService.calculateBill(baseFee);
        
        assertEquals(1000.0, patientPays, 0.01);
    }
    
    @Test
    @DisplayName("Test changing billing strategy")
    public void testChangingStrategy() {
        BillingService billingService = new BillingService();
        double baseFee = 1000.0;
        
        billingService.setBillingStrategy(new CashBilling(10.0));
        assertEquals(900.0, billingService.calculateBill(baseFee), 0.01);
        
        billingService.setBillingStrategy(new InsuranceBilling("Aetna", "POL789012", 70.0));
        assertEquals(300.0, billingService.calculateBill(baseFee), 0.01);
    }
}
