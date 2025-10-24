const API_BASE = '/api';

let patients = [];
let doctors = [];
let appointments = [];

function showTab(tabName) {
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.remove('active');
    });
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    document.getElementById(tabName).classList.add('active');
    event.target.classList.add('active');
    
    if (tabName === 'dashboard') {
        loadDashboard();
    } else if (tabName === 'patients') {
        loadPatients();
    } else if (tabName === 'doctors') {
        loadDoctors();
    } else if (tabName === 'appointments') {
        loadAppointments();
        populateAppointmentSelectors();
    }
}

function showNotification(message, type = 'success') {
    const notification = document.getElementById('notification');
    notification.textContent = message;
    notification.className = `notification ${type}`;
    notification.style.display = 'block';
    
    setTimeout(() => {
        notification.style.display = 'none';
    }, 3000);
}

async function apiCall(endpoint, method = 'GET', params = {}) {
    try {
        const url = `${API_BASE}${endpoint}`;
        let options = {
            method: method,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        };
        
        if (method === 'POST') {
            const formData = new URLSearchParams(params).toString();
            options.body = formData;
        }
        
        const response = await fetch(url, options);
        const data = await response.json();
        
        if (!data.success) {
            throw new Error(data.error || 'Request failed');
        }
        
        return data.data;
    } catch (error) {
        showNotification(error.message, 'error');
        throw error;
    }
}

async function loadDashboard() {
    try {
        const patientStats = await apiCall('/patients/stats');
        const doctorStats = await apiCall('/doctors/stats');
        const allAppointments = await apiCall('/appointments');
        const criticalPatients = await apiCall('/patients/critical');
        
        document.getElementById('totalPatients').textContent = patientStats.total;
        document.getElementById('criticalPatients').textContent = patientStats.critical;
        document.getElementById('totalDoctors').textContent = doctorStats.total;
        document.getElementById('totalAppointments').textContent = allAppointments.length;
        
        displayCriticalPatients(criticalPatients);
        displayRecentAppointments(allAppointments);
    } catch (error) {
        console.error('Error loading dashboard:', error);
    }
}

function displayCriticalPatients(criticalPatients) {
    const container = document.getElementById('criticalPatientsList');
    
    if (criticalPatients.length === 0) {
        container.innerHTML = '<p>No critical patients at this time.</p>';
        return;
    }
    
    container.innerHTML = criticalPatients.map(patient => `
        <div class="data-item">
            <h4>${patient.name} <span class="badge badge-danger">CRITICAL</span></h4>
            <p><strong>ID:</strong> ${patient.patientId} | <strong>Age:</strong> ${patient.age} | <strong>Blood:</strong> ${patient.bloodGroup}</p>
            <p><strong>Contact:</strong> ${patient.contactNumber}</p>
        </div>
    `).join('');
}

function displayRecentAppointments(allAppointments) {
    const container = document.getElementById('recentAppointments');
    const recent = allAppointments.slice(0, 5);
    
    if (recent.length === 0) {
        container.innerHTML = '<p>No appointments scheduled.</p>';
        return;
    }
    
    container.innerHTML = recent.map(appt => `
        <div class="data-item">
            <h4>Appointment ${appt.appointmentId}</h4>
            <p><strong>Date:</strong> ${appt.appointmentDate} | <strong>Time:</strong> ${appt.timeSlot}</p>
            <p><strong>Status:</strong> <span class="badge badge-${getStatusClass(appt.status)}">${appt.status}</span></p>
        </div>
    `).join('');
}

document.getElementById('patientForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const params = {
        name: document.getElementById('patientName').value,
        age: document.getElementById('patientAge').value,
        gender: document.getElementById('patientGender').value,
        bloodGroup: document.getElementById('patientBloodGroup').value,
        contactNumber: document.getElementById('patientContact').value,
        address: document.getElementById('patientAddress').value,
        isCritical: document.getElementById('patientCritical').checked
    };
    
    try {
        await apiCall('/patients', 'POST', params);
        showNotification('Patient registered successfully!');
        e.target.reset();
        loadPatients();
    } catch (error) {
        console.error('Error registering patient:', error);
    }
});

async function loadPatients() {
    try {
        patients = await apiCall('/patients');
        displayPatients(patients);
    } catch (error) {
        console.error('Error loading patients:', error);
    }
}

function displayPatients(patientList) {
    const container = document.getElementById('patientsList');
    
    if (patientList.length === 0) {
        container.innerHTML = '<p>No patients registered yet.</p>';
        return;
    }
    
    container.innerHTML = patientList.map(patient => `
        <div class="data-item">
            <h4>${patient.name} ${patient.isCritical ? '<span class="badge badge-danger">CRITICAL</span>' : ''}</h4>
            <p><strong>ID:</strong> ${patient.patientId} | <strong>Age:</strong> ${patient.age} | <strong>Gender:</strong> ${patient.gender}</p>
            <p><strong>Blood Group:</strong> ${patient.bloodGroup} | <strong>Contact:</strong> ${patient.contactNumber}</p>
            <p><strong>Address:</strong> ${patient.address}</p>
        </div>
    `).join('');
}

document.getElementById('doctorForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const params = {
        name: document.getElementById('doctorName').value,
        specialization: document.getElementById('doctorSpecialization').value,
        department: document.getElementById('doctorDepartment').value,
        contactNumber: document.getElementById('doctorContact').value,
        email: document.getElementById('doctorEmail').value,
        experienceYears: document.getElementById('doctorExperience').value,
        consultationFee: document.getElementById('doctorFee').value
    };
    
    try {
        await apiCall('/doctors', 'POST', params);
        showNotification('Doctor registered successfully!');
        e.target.reset();
        loadDoctors();
    } catch (error) {
        console.error('Error registering doctor:', error);
    }
});

async function loadDoctors() {
    try {
        doctors = await apiCall('/doctors');
        displayDoctors(doctors);
    } catch (error) {
        console.error('Error loading doctors:', error);
    }
}

function displayDoctors(doctorList) {
    const container = document.getElementById('doctorsList');
    
    if (doctorList.length === 0) {
        container.innerHTML = '<p>No doctors registered yet.</p>';
        return;
    }
    
    container.innerHTML = doctorList.map(doctor => `
        <div class="data-item">
            <h4>Dr. ${doctor.name} <span class="badge badge-info">${doctor.specialization}</span></h4>
            <p><strong>ID:</strong> ${doctor.doctorId} | <strong>Department:</strong> ${doctor.department}</p>
            <p><strong>Experience:</strong> ${doctor.experienceYears} years | <strong>Fee:</strong> $${doctor.consultationFee}</p>
            <p><strong>Contact:</strong> ${doctor.contactNumber} | <strong>Email:</strong> ${doctor.email}</p>
        </div>
    `).join('');
}

function populateAppointmentSelectors() {
    const patientSelect = document.getElementById('appointmentPatient');
    const doctorSelect = document.getElementById('appointmentDoctor');
    
    patientSelect.innerHTML = '<option value="">Select Patient</option>' + 
        patients.map(p => `<option value="${p.patientId}">${p.name} (${p.patientId})</option>`).join('');
    
    doctorSelect.innerHTML = '<option value="">Select Doctor</option>' + 
        doctors.map(d => `<option value="${d.doctorId}">Dr. ${d.name} - ${d.specialization}</option>`).join('');
}

document.getElementById('appointmentForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const params = {
        patientId: document.getElementById('appointmentPatient').value,
        doctorId: document.getElementById('appointmentDoctor').value,
        date: document.getElementById('appointmentDate').value,
        timeSlot: document.getElementById('appointmentTimeSlot').value,
        symptoms: document.getElementById('appointmentSymptoms').value
    };
    
    try {
        await apiCall('/appointments', 'POST', params);
        showNotification('Appointment booked successfully!');
        e.target.reset();
        loadAppointments();
    } catch (error) {
        console.error('Error booking appointment:', error);
    }
});

async function loadAppointments() {
    try {
        appointments = await apiCall('/appointments');
        displayAppointments(appointments);
    } catch (error) {
        console.error('Error loading appointments:', error);
    }
}

function displayAppointments(appointmentList) {
    const container = document.getElementById('appointmentsList');
    
    if (appointmentList.length === 0) {
        container.innerHTML = '<p>No appointments scheduled yet.</p>';
        return;
    }
    
    container.innerHTML = appointmentList.map(appt => `
        <div class="data-item">
            <h4>Appointment ${appt.appointmentId} <span class="badge badge-${getStatusClass(appt.status)}">${appt.status}</span></h4>
            <p><strong>Patient ID:</strong> ${appt.patientId} | <strong>Doctor ID:</strong> ${appt.doctorId}</p>
            <p><strong>Date:</strong> ${appt.appointmentDate} | <strong>Time:</strong> ${appt.timeSlot}</p>
            ${appt.symptoms ? `<p><strong>Symptoms:</strong> ${appt.symptoms}</p>` : ''}
            ${appt.diagnosis ? `<p><strong>Diagnosis:</strong> ${appt.diagnosis}</p>` : ''}
            ${appt.prescription ? `<p><strong>Prescription:</strong> ${appt.prescription}</p>` : ''}
        </div>
    `).join('');
}

function getStatusClass(status) {
    switch(status) {
        case 'SCHEDULED': return 'info';
        case 'COMPLETED': return 'success';
        case 'CANCELLED': return 'danger';
        default: return 'warning';
    }
}

function toggleBillingFields() {
    const billType = document.getElementById('billType').value;
    document.getElementById('cashFields').style.display = billType === 'cash' ? 'block' : 'none';
    document.getElementById('insuranceFields').style.display = billType === 'insurance' ? 'block' : 'none';
}

document.getElementById('billingForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const billType = document.getElementById('billType').value;
    const params = {
        baseFee: document.getElementById('billBaseFee').value,
        billingType: billType
    };
    
    if (billType === 'cash') {
        params.discount = document.getElementById('billDiscount').value || '0';
    } else {
        params.provider = document.getElementById('billProvider').value;
        params.policyNumber = document.getElementById('billPolicyNumber').value;
        params.coverage = document.getElementById('billCoverage').value;
    }
    
    try {
        const result = await apiCall('/billing', 'POST', params);
        document.getElementById('billingDetails').textContent = result.details;
        document.getElementById('billingResult').style.display = 'block';
        showNotification('Bill calculated successfully!');
    } catch (error) {
        console.error('Error calculating bill:', error);
    }
});

const today = new Date().toISOString().split('T')[0];
document.getElementById('appointmentDate').setAttribute('min', today);

window.addEventListener('load', () => {
    loadDashboard();
});
