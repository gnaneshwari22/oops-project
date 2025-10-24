package com.hospital.repositories;

import com.hospital.models.Appointment;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AppointmentRepository {
    private Map<String, Appointment> appointments;
    private Map<String, Set<String>> bookedSlots;
    
    public AppointmentRepository() {
        this.appointments = new ConcurrentHashMap<>();
        this.bookedSlots = new ConcurrentHashMap<>();
    }
    
    public synchronized void addAppointment(Appointment appointment) {
        if (appointment != null && appointment.getAppointmentId() != null) {
            appointments.put(appointment.getAppointmentId(), appointment);
            
            String slotKey = appointment.getDoctorId() + "_" + 
                           appointment.getAppointmentDate() + "_" + 
                           appointment.getTimeSlot();
            bookedSlots.computeIfAbsent(appointment.getDoctorId(), k -> ConcurrentHashMap.newKeySet())
                      .add(slotKey);
        }
    }
    
    public Appointment getAppointment(String appointmentId) {
        return appointments.get(appointmentId);
    }
    
    public synchronized void updateAppointment(Appointment appointment) {
        if (appointment != null && appointments.containsKey(appointment.getAppointmentId())) {
            appointments.put(appointment.getAppointmentId(), appointment);
        }
    }
    
    public synchronized void deleteAppointment(String appointmentId) {
        Appointment appointment = appointments.remove(appointmentId);
        if (appointment != null) {
            String slotKey = appointment.getDoctorId() + "_" + 
                           appointment.getAppointmentDate() + "_" + 
                           appointment.getTimeSlot();
            Set<String> slots = bookedSlots.get(appointment.getDoctorId());
            if (slots != null) {
                slots.remove(slotKey);
            }
        }
    }
    
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments.values());
    }
    
    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return appointments.values().stream()
                .filter(a -> a.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }
    
    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return appointments.values().stream()
                .filter(a -> a.getDoctorId().equals(doctorId))
                .collect(Collectors.toList());
    }
    
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return appointments.values().stream()
                .filter(a -> a.getAppointmentDate().equals(date))
                .collect(Collectors.toList());
    }
    
    public synchronized boolean isSlotAvailable(String doctorId, LocalDate date, String timeSlot) {
        String slotKey = doctorId + "_" + date + "_" + timeSlot;
        Set<String> slots = bookedSlots.get(doctorId);
        return slots == null || !slots.contains(slotKey);
    }
    
    public List<Appointment> getAppointmentsByStatus(String status) {
        return appointments.values().stream()
                .filter(a -> a.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }
    
    public long getTotalAppointments() {
        return appointments.size();
    }
}
