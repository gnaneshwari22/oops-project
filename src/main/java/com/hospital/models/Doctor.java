package com.hospital.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Doctor extends Staff implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String doctorId;
    private String name;
    private String specialization;
    private String department;
    private String contactNumber;
    private String email;
    private int experienceYears;
    private double consultationFee;
    private String[] appointmentSlots;
    private List<String> qualifications;
    
    public Doctor() {
        super();
        this.qualifications = new ArrayList<>();
        initializeAppointmentSlots();
    }
    
    public Doctor(String doctorId, String name, String specialization, String department,
                  String contactNumber, String email, int experienceYears, double consultationFee) {
        super(doctorId, name, department, contactNumber);
        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
        this.department = department;
        this.contactNumber = contactNumber;
        this.email = email;
        this.experienceYears = experienceYears;
        this.consultationFee = consultationFee;
        this.qualifications = new ArrayList<>();
        initializeAppointmentSlots();
    }
    
    private void initializeAppointmentSlots() {
        this.appointmentSlots = new String[]{
            "09:00-10:00", "10:00-11:00", "11:00-12:00",
            "14:00-15:00", "15:00-16:00", "16:00-17:00"
        };
    }
    
    @Override
    public void performDuties() {
        System.out.println("Dr. " + name + " is consulting patients in " + department + " department");
    }
    
    @Override
    public String getRole() {
        return "Doctor - " + specialization;
    }
    
    public String getDoctorId() {
        return doctorId;
    }
    
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getContactNumber() {
        return contactNumber;
    }
    
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public int getExperienceYears() {
        return experienceYears;
    }
    
    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }
    
    public double getConsultationFee() {
        return consultationFee;
    }
    
    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }
    
    public String[] getAppointmentSlots() {
        return appointmentSlots.clone();
    }
    
    public void setAppointmentSlots(String[] appointmentSlots) {
        this.appointmentSlots = appointmentSlots.clone();
    }
    
    public List<String> getQualifications() {
        return new ArrayList<>(qualifications);
    }
    
    public void addQualification(String qualification) {
        this.qualifications.add(qualification);
    }
    
    public void setQualifications(List<String> qualifications) {
        this.qualifications = new ArrayList<>(qualifications);
    }
    
    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId='" + doctorId + '\'' +
                ", name='" + name + '\'' +
                ", specialization='" + specialization + '\'' +
                ", department='" + department + '\'' +
                ", experienceYears=" + experienceYears +
                ", consultationFee=" + consultationFee +
                '}';
    }
}
