package com.hospital.services;

import com.hospital.models.Patient;
import com.hospital.repositories.PatientRepository;
import com.hospital.exceptions.InvalidPatientException;
import com.hospital.utils.FileIOManager;
import com.hospital.utils.SerializationManager;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PatientService {
    private PatientRepository patientRepository;
    
    public PatientService() {
        this.patientRepository = new PatientRepository();
    }
    
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }
    
    public Patient registerPatient(String name, int age, String gender, String bloodGroup,
                                  String contactNumber, String address) throws InvalidPatientException {
        String patientId = "PAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        Patient patient = new Patient(patientId, name, age, gender, bloodGroup, contactNumber, address);
        patientRepository.addPatient(patient);
        
        try {
            SerializationManager.serializePatient(patient);
        } catch (Exception e) {
            System.err.println("Warning: Failed to serialize patient: " + e.getMessage());
        }
        
        return patient;
    }
    
    public Patient getPatient(String patientId) throws InvalidPatientException {
        return patientRepository.getPatient(patientId);
    }
    
    public void updatePatient(Patient patient) throws InvalidPatientException {
        patientRepository.updatePatient(patient);
        
        try {
            SerializationManager.serializePatient(patient);
        } catch (Exception e) {
            System.err.println("Warning: Failed to serialize patient: " + e.getMessage());
        }
    }
    
    public void deletePatient(String patientId) throws InvalidPatientException {
        patientRepository.deletePatient(patientId);
        FileIOManager.deleteMedicalHistory(patientId);
        SerializationManager.deleteSerializedPatient(patientId);
    }
    
    public List<Patient> getAllPatients() {
        return patientRepository.getAllPatients();
    }
    
    public void addMedicalRecord(String patientId, String record) throws Exception {
        Patient patient = patientRepository.getPatient(patientId);
        FileIOManager.appendToMedicalHistory(patient, record);
        patientRepository.updatePatient(patient);
    }
    
    public List<String> getMedicalHistory(String patientId) throws Exception {
        return FileIOManager.loadMedicalHistory(patientId);
    }
    
    public List<Patient> getCriticalPatients() {
        return patientRepository.getCriticalPatients();
    }
    
    public Map<String, Long> getPatientCountByBloodGroup() {
        return patientRepository.getPatientCountByBloodGroup();
    }
    
    public void markPatientAsCritical(String patientId, boolean isCritical) throws InvalidPatientException {
        Patient patient = patientRepository.getPatient(patientId);
        patient.setCritical(isCritical);
        patientRepository.updatePatient(patient);
    }
    
    public boolean patientExists(String patientId) {
        return patientRepository.patientExists(patientId);
    }
    
    public long getTotalPatients() {
        return patientRepository.getTotalPatients();
    }
}
