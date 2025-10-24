package com.hospital.repositories;

import com.hospital.models.Patient;
import com.hospital.exceptions.InvalidPatientException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class PatientRepository {
    private Map<String, Patient> patients;
    
    public PatientRepository() {
        this.patients = new ConcurrentHashMap<>();
    }
    
    public void addPatient(Patient patient) throws InvalidPatientException {
        if (patient == null || patient.getPatientId() == null || patient.getPatientId().trim().isEmpty()) {
            throw new InvalidPatientException("Patient or Patient ID cannot be null or empty");
        }
        
        if (patients.containsKey(patient.getPatientId())) {
            throw new InvalidPatientException("Patient already exists", patient.getPatientId());
        }
        
        patients.put(patient.getPatientId(), patient);
    }
    
    public Patient getPatient(String patientId) throws InvalidPatientException {
        if (patientId == null || patientId.trim().isEmpty()) {
            throw new InvalidPatientException("Patient ID cannot be null or empty");
        }
        
        Patient patient = patients.get(patientId);
        if (patient == null) {
            throw new InvalidPatientException("Patient not found", patientId);
        }
        
        return patient;
    }
    
    public void updatePatient(Patient patient) throws InvalidPatientException {
        if (patient == null || patient.getPatientId() == null) {
            throw new InvalidPatientException("Patient or Patient ID cannot be null");
        }
        
        if (!patients.containsKey(patient.getPatientId())) {
            throw new InvalidPatientException("Patient not found", patient.getPatientId());
        }
        
        patients.put(patient.getPatientId(), patient);
    }
    
    public void deletePatient(String patientId) throws InvalidPatientException {
        if (patientId == null || !patients.containsKey(patientId)) {
            throw new InvalidPatientException("Patient not found", patientId);
        }
        
        patients.remove(patientId);
    }
    
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients.values());
    }
    
    public List<Patient> getCriticalPatients() {
        return patients.values().stream()
                .filter(Patient::isCritical)
                .collect(Collectors.toList());
    }
    
    public Map<String, Long> getPatientCountByBloodGroup() {
        return patients.values().stream()
                .collect(Collectors.groupingBy(
                    Patient::getBloodGroup,
                    Collectors.counting()
                ));
    }
    
    public boolean patientExists(String patientId) {
        return patients.containsKey(patientId);
    }
    
    public long getTotalPatients() {
        return patients.size();
    }
}
