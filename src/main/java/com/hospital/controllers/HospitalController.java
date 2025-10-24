package com.hospital.controllers;

import com.hospital.models.*;
import com.hospital.services.*;
import com.hospital.strategies.*;
import com.hospital.factories.MedicalReportFactory;
import com.hospital.utils.ReflectionUtils;
import com.hospital.exceptions.InvalidPatientException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.*;

public class HospitalController {
    private PatientService patientService;
    private DoctorService doctorService;
    private AppointmentService appointmentService;
    private BillingService billingService;
    private ObjectMapper objectMapper;
    
    public HospitalController(PatientService patientService, DoctorService doctorService,
                            AppointmentService appointmentService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.billingService = new BillingService();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    
    public String handleRequest(String method, String endpoint, Map<String, String> params) {
        try {
            switch (method + " " + endpoint) {
                case "POST /patients":
                    return registerPatient(params);
                case "GET /patients":
                    return getAllPatients();
                case "GET /patients/critical":
                    return getCriticalPatients();
                case "GET /patients/stats":
                    return getPatientStats();
                case "POST /doctors":
                    return registerDoctor(params);
                case "GET /doctors":
                    return getAllDoctors();
                case "GET /doctors/stats":
                    return getDoctorStats();
                case "POST /appointments":
                    return bookAppointment(params);
                case "GET /appointments":
                    return getAllAppointments();
                case "POST /appointments/cancel":
                    return cancelAppointment(params);
                case "POST /appointments/complete":
                    return completeAppointment(params);
                case "POST /billing":
                    return calculateBill(params);
                case "POST /reports":
                    return generateReport(params);
                case "GET /reflection":
                    return inspectDoctorClass();
                default:
                    return errorResponse("Endpoint not found");
            }
        } catch (Exception e) {
            return errorResponse(e.getMessage());
        }
    }
    
    private String registerPatient(Map<String, String> params) throws Exception {
        Patient patient = patientService.registerPatient(
            params.get("name"),
            Integer.parseInt(params.get("age")),
            params.get("gender"),
            params.get("bloodGroup"),
            params.get("contactNumber"),
            params.get("address")
        );
        
        if (params.containsKey("isCritical")) {
            patient.setCritical(Boolean.parseBoolean(params.get("isCritical")));
            patientService.updatePatient(patient);
        }
        
        return successResponse(patient);
    }
    
    private String getAllPatients() throws Exception {
        List<Patient> patients = patientService.getAllPatients();
        return successResponse(patients);
    }
    
    private String getCriticalPatients() throws Exception {
        List<Patient> patients = patientService.getCriticalPatients();
        return successResponse(patients);
    }
    
    private String getPatientStats() throws Exception {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", patientService.getTotalPatients());
        stats.put("critical", patientService.getCriticalPatients().size());
        stats.put("byBloodGroup", patientService.getPatientCountByBloodGroup());
        return successResponse(stats);
    }
    
    private String registerDoctor(Map<String, String> params) throws Exception {
        Doctor doctor = doctorService.registerDoctor(
            params.get("name"),
            params.get("specialization"),
            params.get("department"),
            params.get("contactNumber"),
            params.get("email"),
            Integer.parseInt(params.get("experienceYears")),
            Double.parseDouble(params.get("consultationFee"))
        );
        return successResponse(doctor);
    }
    
    private String getAllDoctors() throws Exception {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return successResponse(doctors);
    }
    
    private String getDoctorStats() throws Exception {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", doctorService.getTotalDoctors());
        stats.put("byDepartment", doctorService.getDoctorsByDepartment());
        stats.put("avgFeeByDepartment", doctorService.getAverageConsultationFeeByDepartment());
        return successResponse(stats);
    }
    
    private String bookAppointment(Map<String, String> params) throws Exception {
        Appointment appointment = appointmentService.bookAppointment(
            params.get("patientId"),
            params.get("doctorId"),
            LocalDate.parse(params.get("date")),
            params.get("timeSlot")
        );
        
        if (params.containsKey("symptoms")) {
            appointment.setSymptoms(params.get("symptoms"));
            appointmentService.updateAppointment(appointment);
        }
        
        return successResponse(appointment);
    }
    
    private String getAllAppointments() throws Exception {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return successResponse(appointments);
    }
    
    private String cancelAppointment(Map<String, String> params) throws Exception {
        appointmentService.cancelAppointment(params.get("appointmentId"));
        return successResponse("Appointment cancelled successfully");
    }
    
    private String completeAppointment(Map<String, String> params) throws Exception {
        appointmentService.completeAppointment(
            params.get("appointmentId"),
            params.get("diagnosis"),
            params.get("prescription")
        );
        return successResponse("Appointment completed successfully");
    }
    
    private String calculateBill(Map<String, String> params) throws Exception {
        String billingType = params.get("billingType");
        double baseFee = Double.parseDouble(params.get("baseFee"));
        
        if ("insurance".equalsIgnoreCase(billingType)) {
            billingService.setBillingStrategy(new InsuranceBilling(
                params.get("provider"),
                params.get("policyNumber"),
                Double.parseDouble(params.get("coverage"))
            ));
        } else {
            double discount = params.containsKey("discount") ? 
                Double.parseDouble(params.get("discount")) : 0.0;
            billingService.setBillingStrategy(new CashBilling(discount));
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("amount", billingService.calculateBill(baseFee));
        result.put("details", billingService.generateBillDetails(baseFee));
        
        return successResponse(result);
    }
    
    private String generateReport(Map<String, String> params) throws Exception {
        MedicalReport report = MedicalReportFactory.createReport(
            params.get("patientId"),
            params.get("reportType"),
            params.get("content"),
            params.get("generatedBy")
        );
        return successResponse(report);
    }
    
    private String inspectDoctorClass() throws Exception {
        String reflection = ReflectionUtils.inspectClass(Doctor.class);
        Map<String, String> result = new HashMap<>();
        result.put("reflection", reflection);
        return successResponse(result);
    }
    
    private String successResponse(Object data) throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", data);
        return objectMapper.writeValueAsString(response);
    }
    
    private String errorResponse(String message) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", message);
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            return "{\"success\":false,\"error\":\"" + message + "\"}";
        }
    }
}
