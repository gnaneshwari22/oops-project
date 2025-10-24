package com.hospital.models;

import java.io.Serializable;

public abstract class Staff implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String staffId;
    protected String name;
    protected String department;
    protected String contactNumber;
    
    public Staff() {
    }
    
    public Staff(String staffId, String name, String department, String contactNumber) {
        this.staffId = staffId;
        this.name = name;
        this.department = department;
        this.contactNumber = contactNumber;
    }
    
    public abstract void performDuties();
    
    public abstract String getRole();
    
    public String getStaffId() {
        return staffId;
    }
    
    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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
}
