package com.hospital.services;

import com.hospital.models.Appointment;
import com.hospital.repositories.AppointmentRepository;
import com.hospital.repositories.PatientRepository;
import com.hospital.repositories.DoctorRepository;
import com.hospital.exceptions.InvalidPatientException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class AppointmentService {
    private AppointmentRepository appointmentRepository;
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;
    private final Object schedulingLock = new Object();
    
    public AppointmentService() {
        this.appointmentRepository = new AppointmentRepository();
        this.patientRepository = new PatientRepository();
        this.doctorRepository = new DoctorRepository();
    }
    
    public AppointmentService(AppointmentRepository appointmentRepository,
                            PatientRepository patientRepository,
                            DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }
    
    public Appointment bookAppointment(String patientId, String doctorId,
                                      LocalDate appointmentDate, String timeSlot)
            throws InvalidPatientException {
        synchronized (schedulingLock) {
            if (!patientRepository.patientExists(patientId)) {
                throw new InvalidPatientException("Patient not found", patientId);
            }
            
            if (!doctorRepository.doctorExists(doctorId)) {
                throw new InvalidPatientException("Doctor not found with ID: " + doctorId);
            }
            
            if (!appointmentRepository.isSlotAvailable(doctorId, appointmentDate, timeSlot)) {
                throw new InvalidPatientException("Time slot already booked for this doctor");
            }
            
            String appointmentId = "APT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            Appointment appointment = new Appointment(appointmentId, patientId, doctorId,
                                                      appointmentDate, timeSlot);
            
            appointmentRepository.addAppointment(appointment);
            return appointment;
        }
    }
    
    public Appointment getAppointment(String appointmentId) {
        return appointmentRepository.getAppointment(appointmentId);
    }
    
    public void updateAppointment(Appointment appointment) {
        appointmentRepository.updateAppointment(appointment);
    }
    
    public void cancelAppointment(String appointmentId) {
        synchronized (schedulingLock) {
            Appointment appointment = appointmentRepository.getAppointment(appointmentId);
            if (appointment != null) {
                appointment.setStatus("CANCELLED");
                appointmentRepository.updateAppointment(appointment);
            }
        }
    }
    
    public void completeAppointment(String appointmentId, String diagnosis, String prescription) {
        Appointment appointment = appointmentRepository.getAppointment(appointmentId);
        if (appointment != null) {
            appointment.setStatus("COMPLETED");
            appointment.setDiagnosis(diagnosis);
            appointment.setPrescription(prescription);
            appointmentRepository.updateAppointment(appointment);
        }
    }
    
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.getAllAppointments();
    }
    
    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return appointmentRepository.getAppointmentsByPatient(patientId);
    }
    
    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return appointmentRepository.getAppointmentsByDoctor(doctorId);
    }
    
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return appointmentRepository.getAppointmentsByDate(date);
    }
    
    public boolean isSlotAvailable(String doctorId, LocalDate date, String timeSlot) {
        return appointmentRepository.isSlotAvailable(doctorId, date, timeSlot);
    }
    
    public List<Appointment> getAppointmentsByStatus(String status) {
        return appointmentRepository.getAppointmentsByStatus(status);
    }
    
    public long getTotalAppointments() {
        return appointmentRepository.getTotalAppointments();
    }
}
