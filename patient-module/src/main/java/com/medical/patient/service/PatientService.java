package com.medical.patient.service;

import java.util.List;

import com.medical.patient.entity.Patient;

public interface PatientService {

    Patient createPatient(Patient patient);

    Patient updatePatient(Long id, Patient patient);

    Patient getPatientById(Long id);

    List<Patient> getAllPatients();

    void deletePatient(Long id);
}
