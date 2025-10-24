package com.hospital.exceptions;

public class InvalidPatientException extends Exception {
    private String patientId;
    
    public InvalidPatientException(String message) {
        super(message);
    }
    
    public InvalidPatientException(String message, String patientId) {
        super(message);
        this.patientId = patientId;
    }
    
    public InvalidPatientException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public String getPatientId() {
        return patientId;
    }
    
    @Override
    public String toString() {
        if (patientId != null) {
            return "InvalidPatientException: " + getMessage() + " (Patient ID: " + patientId + ")";
        }
        return "InvalidPatientException: " + getMessage();
    }
}
