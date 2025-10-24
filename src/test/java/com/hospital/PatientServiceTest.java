package com.hospital;

import com.hospital.models.Patient;
import com.hospital.services.PatientService;
import com.hospital.repositories.PatientRepository;
import com.hospital.exceptions.InvalidPatientException;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class PatientServiceTest {
    private PatientService patientService;
    private PatientRepository patientRepository;
    
    @BeforeEach
    public void setUp() {
        patientRepository = new PatientRepository();
        patientService = new PatientService(patientRepository);
    }
    
    @Test
    @DisplayName("Test patient registration")
    public void testRegisterPatient() throws InvalidPatientException {
        Patient patient = patientService.registerPatient("John Doe", 30, "Male", "O+", "1234567890", "123 Main St");
        
        assertNotNull(patient);
        assertNotNull(patient.getPatientId());
        assertEquals("John Doe", patient.getName());
        assertEquals(30, patient.getAge());
    }
    
    @Test
    @DisplayName("Test get critical patients")
    public void testGetCriticalPatients() throws InvalidPatientException {
        Patient patient1 = patientService.registerPatient("John Doe", 30, "Male", "O+", "1234567890", "123 Main St");
        Patient patient2 = patientService.registerPatient("Jane Smith", 28, "Female", "A+", "0987654321", "456 Oak Ave");
        
        patientService.markPatientAsCritical(patient1.getPatientId(), true);
        
        List<Patient> criticalPatients = patientService.getCriticalPatients();
        
        assertEquals(1, criticalPatients.size());
        assertTrue(criticalPatients.get(0).isCritical());
    }
    
    @Test
    @DisplayName("Test patient count by blood group")
    public void testPatientCountByBloodGroup() throws InvalidPatientException {
        patientService.registerPatient("John Doe", 30, "Male", "O+", "1234567890", "123 Main St");
        patientService.registerPatient("Jane Smith", 28, "Female", "O+", "0987654321", "456 Oak Ave");
        patientService.registerPatient("Bob Johnson", 35, "Male", "A+", "1112223333", "789 Pine Rd");
        
        Map<String, Long> bloodGroupCount = patientService.getPatientCountByBloodGroup();
        
        assertEquals(2L, bloodGroupCount.get("O+"));
        assertEquals(1L, bloodGroupCount.get("A+"));
    }
    
    @Test
    @DisplayName("Test invalid patient exception")
    public void testInvalidPatientException() {
        assertThrows(InvalidPatientException.class, () -> {
            patientService.getPatient("INVALID_ID");
        });
    }
}
