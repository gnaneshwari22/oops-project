package com.hospital.utils;

import com.hospital.models.Patient;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerializationManager {
    private static final String SERIALIZED_DATA_DIR = "data/serialized/";
    
    static {
        File dir = new File(SERIALIZED_DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    public static void serializePatient(Patient patient) throws IOException {
        String filename = SERIALIZED_DATA_DIR + patient.getPatientId() + ".ser";
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(patient);
        }
    }
    
    public static Patient deserializePatient(String patientId) throws IOException, ClassNotFoundException {
        String filename = SERIALIZED_DATA_DIR + patientId + ".ser";
        File file = new File(filename);
        
        if (!file.exists()) {
            return null;
        }
        
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Patient) ois.readObject();
        }
    }
    
    public static void serializePatientList(List<Patient> patients, String filename) throws IOException {
        String fullPath = SERIALIZED_DATA_DIR + filename;
        try (FileOutputStream fos = new FileOutputStream(fullPath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(patients);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static List<Patient> deserializePatientList(String filename) throws IOException, ClassNotFoundException {
        String fullPath = SERIALIZED_DATA_DIR + filename;
        File file = new File(fullPath);
        
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (FileInputStream fis = new FileInputStream(fullPath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (List<Patient>) ois.readObject();
        }
    }
    
    public static boolean serializedPatientExists(String patientId) {
        String filename = SERIALIZED_DATA_DIR + patientId + ".ser";
        return new File(filename).exists();
    }
    
    public static void deleteSerializedPatient(String patientId) {
        String filename = SERIALIZED_DATA_DIR + patientId + ".ser";
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
    }
}
