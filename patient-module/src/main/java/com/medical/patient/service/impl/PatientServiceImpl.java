package com.medical.patient.service.impl;

import java.util.List;


import org.springframework.stereotype.Service;

import com.medical.patient.dto.PatientRequest;
import com.medical.patient.dto.PatientResponse;
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
    public PatientResponse createPatient(PatientRequest request) {
        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "A patient with email '" + request.getEmail() + "' already exists.");
        }
        
        Patient patient = toEntity(request);
        Patient saved = patientRepository.save(patient);
        return toResponse(saved);
    }

    @Override
    public PatientResponse updatePatient(Long id, PatientRequest request) {

        Patient existing = patientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient", "id", id));

        existing.setFirstName(request.getFirstName());
        existing.setLastName(request.getLastName());
        existing.setEmail(request.getEmail());
        existing.setPhone(request.getPhone());
        existing.setDateOfBirth(request.getDateOfBirth());
        existing.setGender(request.getGender());
        existing.setAddress(request.getAddress());
 
        Patient saved = patientRepository.save(existing);
        return toResponse(saved);

    }

    @Override
    public PatientResponse getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient", "id", id));
        return toResponse(patient);

    }

    @Override
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll()
        		.stream()
        		.map(this::toResponse)
        		.toList();
    }

    @Override
    public void deletePatient(Long id) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient", "id", id));
        patientRepository.delete(patient);

    }
    

    private Patient toEntity(PatientRequest request) {
        Patient patient = new Patient();
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setAddress(request.getAddress());
        return patient;
    }
 
    private PatientResponse toResponse(Patient patient) {
        PatientResponse response = new PatientResponse();
        response.setId(patient.getId());
        response.setFirstName(patient.getFirstName());
        response.setLastName(patient.getLastName());
        response.setEmail(patient.getEmail());
        response.setPhone(patient.getPhone());
        response.setDateOfBirth(patient.getDateOfBirth());
        response.setGender(patient.getGender());
        response.setAddress(patient.getAddress());
        return response;
    }

}