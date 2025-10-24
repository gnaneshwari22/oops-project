package com.hospital.utils;

import com.hospital.models.Patient;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileIOManager {
    private static final String MEDICAL_HISTORY_DIR = "data/medical_history/";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    static {
        File dir = new File(MEDICAL_HISTORY_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    public static void saveMedicalHistory(String patientId, String record) throws IOException {
        String filename = MEDICAL_HISTORY_DIR + patientId + "_history.txt";
        String timestamp = LocalDateTime.now().format(formatter);
        String entry = "[" + timestamp + "] " + record + "\n";
        
        try (FileWriter fw = new FileWriter(filename, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(entry);
        }
    }
    
    public static List<String> loadMedicalHistory(String patientId) throws IOException {
        List<String> history = new ArrayList<>();
        String filename = MEDICAL_HISTORY_DIR + patientId + "_history.txt";
        File file = new File(filename);
        
        if (!file.exists()) {
            return history;
        }
        
        try (FileReader fr = new FileReader(filename);
             BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                history.add(line);
            }
        }
        
        return history;
    }
    
    public static void appendToMedicalHistory(Patient patient, String record) throws IOException {
        saveMedicalHistory(patient.getPatientId(), record);
        patient.addMedicalHistory(record);
    }
    
    public static void deleteMedicalHistory(String patientId) {
        String filename = MEDICAL_HISTORY_DIR + patientId + "_history.txt";
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
    }
    
    public static boolean medicalHistoryExists(String patientId) {
        String filename = MEDICAL_HISTORY_DIR + patientId + "_history.txt";
        return new File(filename).exists();
    }
}
