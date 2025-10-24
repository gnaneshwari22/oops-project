package com.hospital.models;

import java.io.Serializable;

public class Nurse extends Staff implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String nurseId;
    private String shift;
    private String assignedWard;
    private int experienceYears;
    
    public Nurse() {
        super();
    }
    
    public Nurse(String nurseId, String name, String department, String contactNumber,
                 String shift, String assignedWard, int experienceYears) {
        super(nurseId, name, department, contactNumber);
        this.nurseId = nurseId;
        this.shift = shift;
        this.assignedWard = assignedWard;
        this.experienceYears = experienceYears;
    }
    
    @Override
    public void performDuties() {
        System.out.println("Nurse " + name + " is attending patients in " + assignedWard + " ward");
    }
    
    @Override
    public String getRole() {
        return "Nurse - " + shift + " Shift";
    }
    
    public String getNurseId() {
        return nurseId;
    }
    
    public void setNurseId(String nurseId) {
        this.nurseId = nurseId;
    }
    
    public String getShift() {
        return shift;
    }
    
    public void setShift(String shift) {
        this.shift = shift;
    }
    
    public String getAssignedWard() {
        return assignedWard;
    }
    
    public void setAssignedWard(String assignedWard) {
        this.assignedWard = assignedWard;
    }
    
    public int getExperienceYears() {
        return experienceYears;
    }
    
    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }
    
    @Override
    public String toString() {
        return "Nurse{" +
                "nurseId='" + nurseId + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", shift='" + shift + '\'' +
                ", assignedWard='" + assignedWard + '\'' +
                '}';
    }
}
