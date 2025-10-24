package com.hospital.repositories;

import com.hospital.models.Doctor;
import com.hospital.models.Patient;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DoctorRepository {
    private Map<String, Doctor> doctors;
    private Map<String, List<Patient>> doctorPatients;
    
    public DoctorRepository() {
        this.doctors = new ConcurrentHashMap<>();
        this.doctorPatients = new ConcurrentHashMap<>();
    }
    
    public void addDoctor(Doctor doctor) {
        if (doctor != null && doctor.getDoctorId() != null) {
            doctors.put(doctor.getDoctorId(), doctor);
            doctorPatients.putIfAbsent(doctor.getDoctorId(), new ArrayList<>());
        }
    }
    
    public Doctor getDoctor(String doctorId) {
        return doctors.get(doctorId);
    }
    
    public void updateDoctor(Doctor doctor) {
        if (doctor != null && doctors.containsKey(doctor.getDoctorId())) {
            doctors.put(doctor.getDoctorId(), doctor);
        }
    }
    
    public void deleteDoctor(String doctorId) {
        doctors.remove(doctorId);
        doctorPatients.remove(doctorId);
    }
    
    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctors.values());
    }
    
    public void assignPatientToDoctor(String doctorId, Patient patient) {
        if (doctors.containsKey(doctorId) && patient != null) {
            doctorPatients.computeIfAbsent(doctorId, k -> new ArrayList<>()).add(patient);
        }
    }
    
    public List<Patient> getPatientsByDoctor(String doctorId) {
        return new ArrayList<>(doctorPatients.getOrDefault(doctorId, new ArrayList<>()));
    }
    
    public Map<String, List<Doctor>> getDoctorsByDepartment() {
        return doctors.values().stream()
                .collect(Collectors.groupingBy(Doctor::getDepartment));
    }
    
    public Map<String, Double> getAverageConsultationFeeByDepartment() {
        return doctors.values().stream()
                .collect(Collectors.groupingBy(
                    Doctor::getDepartment,
                    Collectors.averagingDouble(Doctor::getConsultationFee)
                ));
    }
    
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctors.values().stream()
                .filter(d -> d.getSpecialization().equalsIgnoreCase(specialization))
                .collect(Collectors.toList());
    }
    
    public List<Doctor> getDoctorsByDepartmentName(String department) {
        return doctors.values().stream()
                .filter(d -> d.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }
    
    public boolean doctorExists(String doctorId) {
        return doctors.containsKey(doctorId);
    }
    
    public long getTotalDoctors() {
        return doctors.size();
    }
    
    public Map<String, List<Patient>> getDoctorPatientMapping() {
        return new HashMap<>(doctorPatients);
    }
}
