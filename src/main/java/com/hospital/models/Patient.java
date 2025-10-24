package com.hospital.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Patient implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String patientId;
    private String name;
    private int age;
    private String gender;
    private String bloodGroup;
    private String contactNumber;
    private String address;
    private List<String> medicalHistory;
    private boolean isCritical;
    private double consultationFee;
    private LocalDate registrationDate;
    
    public Patient() {
        this.medicalHistory = new ArrayList<>();
        this.registrationDate = LocalDate.now();
    }
    
    public Patient(String patientId, String name, int age, String gender, 
                   String bloodGroup, String contactNumber, String address) {
        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.contactNumber = contactNumber;
        this.address = address;
        this.medicalHistory = new ArrayList<>();
        this.isCritical = false;
        this.consultationFee = 0.0;
        this.registrationDate = LocalDate.now();
    }
    
    public String getPatientId() {
        return patientId;
    }
    
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getBloodGroup() {
        return bloodGroup;
    }
    
    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
    
    public String getContactNumber() {
        return contactNumber;
    }
    
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public List<String> getMedicalHistory() {
        return new ArrayList<>(medicalHistory);
    }
    
    public void addMedicalHistory(String record) {
        this.medicalHistory.add(record);
    }
    
    public void setMedicalHistory(List<String> medicalHistory) {
        this.medicalHistory = new ArrayList<>(medicalHistory);
    }
    
    public boolean isCritical() {
        return isCritical;
    }
    
    public void setCritical(boolean critical) {
        isCritical = critical;
    }
    
    public double getConsultationFee() {
        return consultationFee;
    }
    
    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }
    
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }
    
    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    @Override
    public String toString() {
        return "Patient{" +
                "patientId='" + patientId + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", isCritical=" + isCritical +
                '}';
    }
}
