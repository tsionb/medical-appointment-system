package com.medical.patient.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.medical.patient.entity.Patient;
import com.medical.common.exception.custom.DuplicateResourceException;
import com.medical.common.exception.custom.ResourceNotFoundException;
import com.medical.patient.repository.PatientRepository;
import com.medical.patient.service.PatientService;

@Service("PrimaryPatientService")
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient createPatient(Patient patient) {
        if (patientRepository.existsByEmail(patient.getEmail())) {
            throw new DuplicateResourceException(
                    "A patient with email '" + patient.getEmail() + "' already exists.");
        }
        return patientRepository.save(patient);
    }

    @Override
    public Patient updatePatient(Long id, Patient patient) {

        Patient existing = patientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient", "id", id));

        existing.setFirstName(patient.getFirstName());
        existing.setLastName(patient.getLastName());
        existing.setEmail(patient.getEmail());
        existing.setPhone(patient.getPhone());
        existing.setDateOfBirth(patient.getDateOfBirth());
        existing.setAddress(patient.getAddress());

        return patientRepository.save(existing);
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient", "id", id));
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public void deletePatient(Long id) {

        Patient patient = getPatientById(id);

        patientRepository.delete(patient);
    }
}