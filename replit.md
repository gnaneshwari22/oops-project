# Patient Management System

## Project Overview
A comprehensive full-stack patient management system with Java backend and modern web frontend. This system demonstrates Object-Oriented Programming principles, design patterns, concurrent operations, and comprehensive testing.

**Date Created:** October 24, 2025

## Technology Stack

### Backend
- **Java 11+** - Core programming language
- **Maven** - Build automation and dependency management
- **JUnit 5** - Testing framework
- **Jackson** - JSON serialization/deserialization
- **Java HttpServer** - Built-in HTTP server for REST API

### Frontend
- **HTML5** - Structure
- **CSS3** - Styling with gradient backgrounds and animations
- **Vanilla JavaScript** - Client-side logic
- **Fetch API** - REST API communication

## Project Structure

### Backend Files (`src/main/java/com/hospital/`)

**Models** (`models/`)
- `Patient.java` - Patient entity with medical history tracking
- `Doctor.java` - Doctor entity extending Staff, includes appointment slots
- `Appointment.java` - Appointment entity with scheduling details
- `Staff.java` - Abstract base class for hospital staff
- `Nurse.java` - Nurse entity extending Staff
- `MedicalReport.java` - Medical report entity

**Factories** (`factories/`)
- `MedicalReportFactory.java` - Factory pattern for creating different report types (Blood Test, X-Ray, MRI, Prescription)

**Strategies** (`strategies/`)
- `BillingStrategy.java` - Strategy interface for billing
- `InsuranceBilling.java` - Insurance-based billing implementation
- `CashBilling.java` - Cash payment billing implementation

**Services** (`services/`)
- `PatientService.java` - Patient business logic with SOLID principles
- `DoctorService.java` - Doctor management service
- `AppointmentService.java` - **Thread-safe** appointment scheduling with synchronized methods
- `BillingService.java` - Billing calculation using strategy pattern

**Repositories** (`repositories/`)
- `PatientRepository.java` - HashMap-based patient storage with Stream operations
- `DoctorRepository.java` - Doctor-patient mapping using HashMap<doctorId, List<Patient>>
- `AppointmentRepository.java` - Concurrent appointment management

**Controllers** (`controllers/`)
- `HospitalController.java` - REST API request handler

**Utilities** (`utils/`)
- `FileIOManager.java` - File I/O for medical history persistence
- `SerializationManager.java` - Patient record serialization/deserialization
- `ReflectionUtils.java` - Java Reflection API for inspecting Doctor class

**Exceptions** (`exceptions/`)
- `InvalidPatientException.java` - Custom exception for patient validation

**Main Application**
- `Main.java` - Application entry point with embedded HTTP server

### Frontend Files (`frontend/`)
- `index.html` - Main application interface with tabbed navigation
- `styles.css` - Professional healthcare-themed styling
- `app.js` - Client-side application logic with API integration

### Test Files (`src/test/java/com/hospital/`)
- `AppointmentServiceTest.java` - Comprehensive appointment tests including concurrent booking
- `PatientServiceTest.java` - Patient service tests with Stream operations
- `BillingStrategyTest.java` - Billing strategy pattern tests

## 5 Module Implementation

### Module 1: Core Classes with Encapsulation
✅ Patient, Doctor, Appointment classes
✅ Encapsulated fields with getters/setters
✅ Constructors with validation
✅ Arrays for appointment slots in Doctor class

### Module 2: OOP Design Patterns
✅ Abstract Staff class → Doctor, Nurse subclasses
✅ Factory Pattern: MedicalReportFactory for different report types
✅ Strategy Pattern: BillingStrategy with Insurance and Cash implementations
✅ Reflection API: ReflectionUtils inspects Doctor class structure

### Module 3: I/O and Exception Handling
✅ File I/O: Medical history stored in text files (data/medical_history/)
✅ Custom Exception: InvalidPatientException with detailed messages
✅ Serialization: Patient records serialized to .ser files (data/serialized/)

### Module 4: Collections and Streams
✅ HashMap<String, Patient> for patient storage
✅ HashMap<String, List<Patient>> for doctor-patient mapping
✅ Streams: Filter critical patients
✅ Streams: Group doctors by department
✅ Streams: Compute average consultation fees by department

### Module 5: Concurrency and Testing
✅ Synchronized appointment scheduling prevents double-booking
✅ ConcurrentHashMap for thread-safe data structures
✅ SOLID Principles: Single Responsibility, Dependency Injection
✅ JUnit 5: 15 comprehensive tests (all passing)
✅ Concurrent booking test validates thread safety

## Features

### Patient Management
- Register new patients with complete medical information
- Track critical patient status
- Maintain medical history with timestamps
- Serialize patient records for persistence
- Group patients by blood type
- Filter critical patients using Streams

### Doctor Management
- Register doctors with specialization and department
- Manage appointment time slots
- Track qualifications and experience
- Calculate average consultation fees by department
- Group doctors by department using Streams

### Appointment System
- **Thread-safe concurrent booking** prevents conflicts
- Real-time slot availability checking
- Multiple appointment statuses (SCHEDULED, COMPLETED, CANCELLED)
- Diagnosis and prescription tracking
- Filter appointments by patient, doctor, date, or status

### Billing System
- Strategy Pattern allows flexible billing methods
- **Insurance Billing**: Calculates patient portion based on coverage percentage
- **Cash Billing**: Supports optional discounts
- Detailed bill generation with breakdowns

### Medical Reports
- Factory Pattern creates different report types
- Support for Blood Tests, X-Rays, MRI Scans, Prescriptions
- Timestamped report generation

## REST API Endpoints

### Patients
- `POST /api/patients` - Register new patient
- `GET /api/patients` - Get all patients
- `GET /api/patients/critical` - Get critical patients
- `GET /api/patients/stats` - Get patient statistics

### Doctors
- `POST /api/doctors` - Register new doctor
- `GET /api/doctors` - Get all doctors
- `GET /api/doctors/stats` - Get doctor statistics with averages

### Appointments
- `POST /api/appointments` - Book appointment (thread-safe)
- `GET /api/appointments` - Get all appointments
- `POST /api/appointments/cancel` - Cancel appointment
- `POST /api/appointments/complete` - Complete appointment with diagnosis

### Billing
- `POST /api/billing` - Calculate bill using selected strategy

### Reports
- `POST /api/reports` - Generate medical report

### Utilities
- `GET /api/reflection` - Inspect Doctor class using Reflection API

## Running the Application

The application starts automatically via the workflow:
```bash
mvn compile exec:java -Dexec.mainClass="com.hospital.Main"
```

Server runs on: **http://0.0.0.0:5000**

## Running Tests

```bash
mvn test
```

All 15 tests validate:
- Appointment booking and concurrency
- Patient management with Streams
- Billing strategies
- Exception handling

## Sample Data

The system loads sample data on startup:
- 3 Patients (including 1 critical patient)
- 3 Doctors across different specializations
- Ready for appointment booking

## Design Principles Applied

### SOLID Principles
- **Single Responsibility**: Each class has one clear purpose
- **Open/Closed**: Extensible through interfaces (BillingStrategy)
- **Liskov Substitution**: Staff hierarchy maintains contract
- **Interface Segregation**: Focused interfaces (BillingStrategy)
- **Dependency Injection**: Services accept repository dependencies

### Design Patterns
- **Factory Pattern**: MedicalReportFactory
- **Strategy Pattern**: Billing system
- **Repository Pattern**: Data access layer
- **MVC Pattern**: Models, Services (Controllers), Views (Frontend)

## Concurrency Features

1. **Synchronized Scheduling**: `synchronized` blocks prevent race conditions
2. **ConcurrentHashMap**: Thread-safe collections
3. **Atomic Operations**: Slot booking is atomic
4. **Concurrent Testing**: JUnit tests validate multi-threaded scenarios

## Data Persistence

1. **File I/O**: Medical history in text files
2. **Serialization**: Patient objects saved as .ser files
3. **In-Memory**: HashMap-based repositories for fast access

## Future Enhancements

- Database integration (PostgreSQL)
- User authentication and authorization
- Email/SMS notifications for appointments
- Prescription management system
- Advanced reporting and analytics dashboard
- Mobile application support
