package com.hospital;

import com.hospital.models.Appointment;
import com.hospital.models.Patient;
import com.hospital.models.Doctor;
import com.hospital.services.AppointmentService;
import com.hospital.services.PatientService;
import com.hospital.services.DoctorService;
import com.hospital.repositories.*;
import com.hospital.exceptions.InvalidPatientException;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

public class AppointmentServiceTest {
    private AppointmentService appointmentService;
    private PatientService patientService;
    private DoctorService doctorService;
    private AppointmentRepository appointmentRepository;
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;
    
    @BeforeEach
    public void setUp() {
        appointmentRepository = new AppointmentRepository();
        patientRepository = new PatientRepository();
        doctorRepository = new DoctorRepository();
        
        appointmentService = new AppointmentService(appointmentRepository, patientRepository, doctorRepository);
        patientService = new PatientService(patientRepository);
        doctorService = new DoctorService(doctorRepository);
    }
    
    @Test
    @DisplayName("Test successful appointment booking")
    public void testBookAppointmentSuccess() throws InvalidPatientException {
        Patient patient = patientService.registerPatient("John Doe", 30, "Male", "O+", "1234567890", "123 Main St");
        Doctor doctor = doctorService.registerDoctor("Dr. Smith", "Cardiology", "Cardiology", "9876543210", "smith@hospital.com", 10, 200.0);
        
        LocalDate date = LocalDate.now().plusDays(1);
        Appointment appointment = appointmentService.bookAppointment(patient.getPatientId(), doctor.getDoctorId(), date, "09:00-10:00");
        
        assertNotNull(appointment);
        assertEquals(patient.getPatientId(), appointment.getPatientId());
        assertEquals(doctor.getDoctorId(), appointment.getDoctorId());
        assertEquals("SCHEDULED", appointment.getStatus());
    }
    
    @Test
    @DisplayName("Test appointment booking with invalid patient")
    public void testBookAppointmentInvalidPatient() throws InvalidPatientException {
        Doctor doctor = doctorService.registerDoctor("Dr. Smith", "Cardiology", "Cardiology", "9876543210", "smith@hospital.com", 10, 200.0);
        
        LocalDate date = LocalDate.now().plusDays(1);
        
        assertThrows(InvalidPatientException.class, () -> {
            appointmentService.bookAppointment("INVALID_ID", doctor.getDoctorId(), date, "09:00-10:00");
        });
    }
    
    @Test
    @DisplayName("Test concurrent appointment booking for same slot")
    public void testConcurrentAppointmentBooking() throws Exception {
        Patient patient1 = patientService.registerPatient("John Doe", 30, "Male", "O+", "1234567890", "123 Main St");
        Patient patient2 = patientService.registerPatient("Jane Smith", 28, "Female", "A+", "0987654321", "456 Oak Ave");
        Doctor doctor = doctorService.registerDoctor("Dr. Smith", "Cardiology", "Cardiology", "9876543210", "smith@hospital.com", 10, 200.0);
        
        LocalDate date = LocalDate.now().plusDays(1);
        String timeSlot = "09:00-10:00";
        
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);
        
        final boolean[] success1 = {false};
        final boolean[] success2 = {false};
        
        Future<?> future1 = executor.submit(() -> {
            try {
                appointmentService.bookAppointment(patient1.getPatientId(), doctor.getDoctorId(), date, timeSlot);
                success1[0] = true;
            } catch (Exception e) {
                success1[0] = false;
            } finally {
                latch.countDown();
            }
        });
        
        Future<?> future2 = executor.submit(() -> {
            try {
                appointmentService.bookAppointment(patient2.getPatientId(), doctor.getDoctorId(), date, timeSlot);
                success2[0] = true;
            } catch (Exception e) {
                success2[0] = false;
            } finally {
                latch.countDown();
            }
        });
        
        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();
        
        assertTrue(success1[0] != success2[0], "Exactly one booking should succeed");
    }
    
    @Test
    @DisplayName("Test appointment cancellation")
    public void testCancelAppointment() throws InvalidPatientException {
        Patient patient = patientService.registerPatient("John Doe", 30, "Male", "O+", "1234567890", "123 Main St");
        Doctor doctor = doctorService.registerDoctor("Dr. Smith", "Cardiology", "Cardiology", "9876543210", "smith@hospital.com", 10, 200.0);
        
        LocalDate date = LocalDate.now().plusDays(1);
        Appointment appointment = appointmentService.bookAppointment(patient.getPatientId(), doctor.getDoctorId(), date, "09:00-10:00");
        
        appointmentService.cancelAppointment(appointment.getAppointmentId());
        
        Appointment cancelled = appointmentService.getAppointment(appointment.getAppointmentId());
        assertEquals("CANCELLED", cancelled.getStatus());
    }
    
    @Test
    @DisplayName("Test slot availability check")
    public void testSlotAvailability() throws InvalidPatientException {
        Patient patient = patientService.registerPatient("John Doe", 30, "Male", "O+", "1234567890", "123 Main St");
        Doctor doctor = doctorService.registerDoctor("Dr. Smith", "Cardiology", "Cardiology", "9876543210", "smith@hospital.com", 10, 200.0);
        
        LocalDate date = LocalDate.now().plusDays(1);
        String timeSlot = "09:00-10:00";
        
        assertTrue(appointmentService.isSlotAvailable(doctor.getDoctorId(), date, timeSlot));
        
        appointmentService.bookAppointment(patient.getPatientId(), doctor.getDoctorId(), date, timeSlot);
        
        assertFalse(appointmentService.isSlotAvailable(doctor.getDoctorId(), date, timeSlot));
    }
    
    @Test
    @DisplayName("Test get appointments by patient")
    public void testGetAppointmentsByPatient() throws InvalidPatientException {
        Patient patient = patientService.registerPatient("John Doe", 30, "Male", "O+", "1234567890", "123 Main St");
        Doctor doctor = doctorService.registerDoctor("Dr. Smith", "Cardiology", "Cardiology", "9876543210", "smith@hospital.com", 10, 200.0);
        
        LocalDate date = LocalDate.now().plusDays(1);
        appointmentService.bookAppointment(patient.getPatientId(), doctor.getDoctorId(), date, "09:00-10:00");
        appointmentService.bookAppointment(patient.getPatientId(), doctor.getDoctorId(), date, "10:00-11:00");
        
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patient.getPatientId());
        
        assertEquals(2, appointments.size());
    }
    
    @Test
    @DisplayName("Test complete appointment")
    public void testCompleteAppointment() throws InvalidPatientException {
        Patient patient = patientService.registerPatient("John Doe", 30, "Male", "O+", "1234567890", "123 Main St");
        Doctor doctor = doctorService.registerDoctor("Dr. Smith", "Cardiology", "Cardiology", "9876543210", "smith@hospital.com", 10, 200.0);
        
        LocalDate date = LocalDate.now().plusDays(1);
        Appointment appointment = appointmentService.bookAppointment(patient.getPatientId(), doctor.getDoctorId(), date, "09:00-10:00");
        
        appointmentService.completeAppointment(appointment.getAppointmentId(), "Mild fever", "Paracetamol 500mg");
        
        Appointment completed = appointmentService.getAppointment(appointment.getAppointmentId());
        assertEquals("COMPLETED", completed.getStatus());
        assertEquals("Mild fever", completed.getDiagnosis());
        assertEquals("Paracetamol 500mg", completed.getPrescription());
    }
}
