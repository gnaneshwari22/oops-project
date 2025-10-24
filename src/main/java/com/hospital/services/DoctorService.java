package com.hospital.services;

import com.hospital.models.Doctor;
import com.hospital.models.Patient;
import com.hospital.repositories.DoctorRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DoctorService {
    private DoctorRepository doctorRepository;
    
    public DoctorService() {
        this.doctorRepository = new DoctorRepository();
    }
    
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }
    
    public Doctor registerDoctor(String name, String specialization, String department,
                                String contactNumber, String email, int experienceYears,
                                double consultationFee) {
        String doctorId = "DOC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        Doctor doctor = new Doctor(doctorId, name, specialization, department,
                                   contactNumber, email, experienceYears, consultationFee);
        doctorRepository.addDoctor(doctor);
        
        return doctor;
    }
    
    public Doctor getDoctor(String doctorId) {
        return doctorRepository.getDoctor(doctorId);
    }
    
    public void updateDoctor(Doctor doctor) {
        doctorRepository.updateDoctor(doctor);
    }
    
    public void deleteDoctor(String doctorId) {
        doctorRepository.deleteDoctor(doctorId);
    }
    
    public List<Doctor> getAllDoctors() {
        return doctorRepository.getAllDoctors();
    }
    
    public void assignPatientToDoctor(String doctorId, Patient patient) {
        doctorRepository.assignPatientToDoctor(doctorId, patient);
    }
    
    public List<Patient> getPatientsByDoctor(String doctorId) {
        return doctorRepository.getPatientsByDoctor(doctorId);
    }
    
    public Map<String, List<Doctor>> getDoctorsByDepartment() {
        return doctorRepository.getDoctorsByDepartment();
    }
    
    public Map<String, Double> getAverageConsultationFeeByDepartment() {
        return doctorRepository.getAverageConsultationFeeByDepartment();
    }
    
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.getDoctorsBySpecialization(specialization);
    }
    
    public List<Doctor> getDoctorsByDepartmentName(String department) {
        return doctorRepository.getDoctorsByDepartmentName(department);
    }
    
    public boolean doctorExists(String doctorId) {
        return doctorRepository.doctorExists(doctorId);
    }
    
    public long getTotalDoctors() {
        return doctorRepository.getTotalDoctors();
    }
    
    public Map<String, List<Patient>> getDoctorPatientMapping() {
        return doctorRepository.getDoctorPatientMapping();
    }
}
