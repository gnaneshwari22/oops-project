package com.hospital;

import com.hospital.controllers.HospitalController;
import com.hospital.services.*;
import com.hospital.repositories.*;
import com.hospital.models.*;
import com.hospital.exceptions.InvalidPatientException;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    private static HospitalController controller;
    private static PatientService patientService;
    private static DoctorService doctorService;
    private static AppointmentService appointmentService;
    
    public static void main(String[] args) throws Exception {
        initializeServices();
        loadSampleData();
        
        int port = 5000;
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
        
        server.createContext("/api/", new ApiHandler());
        server.createContext("/", new StaticFileHandler());
        
        server.setExecutor(null);
        server.start();
        
        System.out.println("========================================");
        System.out.println("Patient Management System Started");
        System.out.println("Server running on http://0.0.0.0:" + port);
        System.out.println("========================================");
        System.out.println("\nSample Data Loaded:");
        System.out.println("- Patients: " + patientService.getTotalPatients());
        System.out.println("- Doctors: " + doctorService.getTotalDoctors());
        System.out.println("- Appointments: " + appointmentService.getTotalAppointments());
        System.out.println("\nAccess the application at the URL shown above");
    }
    
    private static void initializeServices() {
        PatientRepository patientRepository = new PatientRepository();
        DoctorRepository doctorRepository = new DoctorRepository();
        AppointmentRepository appointmentRepository = new AppointmentRepository();
        
        patientService = new PatientService(patientRepository);
        doctorService = new DoctorService(doctorRepository);
        appointmentService = new AppointmentService(appointmentRepository, patientRepository, doctorRepository);
        
        controller = new HospitalController(patientService, doctorService, appointmentService);
    }
    
    private static void loadSampleData() throws InvalidPatientException {
        Patient p1 = patientService.registerPatient("John Smith", 45, "Male", "O+", "555-0101", "123 Main St");
        Patient p2 = patientService.registerPatient("Sarah Johnson", 32, "Female", "A+", "555-0102", "456 Oak Ave");
        Patient p3 = patientService.registerPatient("Michael Brown", 58, "Male", "B+", "555-0103", "789 Pine Rd");
        patientService.markPatientAsCritical(p3.getPatientId(), true);
        
        Doctor d1 = doctorService.registerDoctor("Dr. Emily Davis", "Cardiology", "Cardiology", "555-0201", "emily.davis@hospital.com", 15, 250.0);
        Doctor d2 = doctorService.registerDoctor("Dr. Robert Wilson", "Orthopedics", "Orthopedics", "555-0202", "robert.wilson@hospital.com", 12, 200.0);
        Doctor d3 = doctorService.registerDoctor("Dr. Lisa Anderson", "Neurology", "Neurology", "555-0203", "lisa.anderson@hospital.com", 10, 300.0);
    }
    
    static class ApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath().substring(4);
            
            Map<String, String> params = new HashMap<>();
            
            if ("POST".equals(method)) {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                String query = br.readLine();
                if (query != null && !query.isEmpty()) {
                    params = parseQueryString(query);
                }
            } else if ("GET".equals(method)) {
                String query = exchange.getRequestURI().getQuery();
                if (query != null) {
                    params = parseQueryString(query);
                }
            }
            
            String response = controller.handleRequest(method, path, params);
            
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
        
        private Map<String, String> parseQueryString(String query) {
            Map<String, String> params = new HashMap<>();
            if (query != null && !query.isEmpty()) {
                String[] pairs = query.split("&");
                for (String pair : pairs) {
                    int idx = pair.indexOf("=");
                    if (idx > 0) {
                        try {
                            String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
                            String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
                            params.put(key, value);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return params;
        }
    }
    
    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if ("/".equals(path)) {
                path = "/index.html";
            }
            
            String filePath = "frontend" + path;
            File file = new File(filePath);
            
            if (file.exists() && !file.isDirectory()) {
                String contentType = getContentType(filePath);
                exchange.getResponseHeaders().set("Content-Type", contentType);
                
                byte[] fileBytes = Files.readAllBytes(file.toPath());
                exchange.sendResponseHeaders(200, fileBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(fileBytes);
                os.close();
            } else {
                String response = "404 Not Found";
                exchange.sendResponseHeaders(404, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
        
        private String getContentType(String filePath) {
            if (filePath.endsWith(".html")) return "text/html";
            if (filePath.endsWith(".css")) return "text/css";
            if (filePath.endsWith(".js")) return "application/javascript";
            if (filePath.endsWith(".json")) return "application/json";
            if (filePath.endsWith(".png")) return "image/png";
            if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) return "image/jpeg";
            return "text/plain";
        }
    }
}
