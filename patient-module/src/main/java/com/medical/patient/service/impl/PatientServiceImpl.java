package com.medical.patient.service.impl;

import java.util.List;


import org.springframework.stereotype.Service;

import com.medical.patient.dto.request.CreatePatientRequest;
import com.medical.patient.dto.request.UpdatePatientRequest;
import com.medical.patient.dto.response.PatientResponse;
import com.medical.patient.entity.Patient;
import com.medical.common.exception.custom.DuplicateResourceException;
import com.medical.common.exception.custom.ResourceNotFoundException;
import com.medical.patient.repository.PatientRepository;
import com.medical.patient.service.PatientService;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
 

@Service("PrimaryPatientService")
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    @Transactional
    public PatientResponse createPatient(CreatePatientRequest request) {
        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "A patient with email '" + request.getEmail() + "' already exists.");
        }
        
        Patient patient = new Patient();
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setAddress(request.getAddress());

        return PatientResponse.fromEntity(patientRepository.save(patient));
    }

    @Override
    @Transactional
    public PatientResponse updatePatient(Long id, UpdatePatientRequest request) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));

        if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
            patient.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().isBlank()) {
            patient.setLastName(request.getLastName());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (patientRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
                throw new DuplicateResourceException(
                    "Email '" + request.getEmail() + "' is already registered"
                );
            }
            patient.setEmail(request.getEmail());
        }
        if (request.getPhone() != null && !request.getPhone().isBlank()) {
            patient.setPhone(request.getPhone());
        }
        if (request.getDateOfBirth() != null) {
            patient.setDateOfBirth(request.getDateOfBirth());   
        }
        if (request.getGender() != null && !request.getGender().isBlank()) {
                patient.setGender(request.getGender());
         } 
        if (request.getAddress() != null) {
            patient.setAddress(request.getAddress());
        }

        return PatientResponse.fromEntity(patientRepository.save(patient));
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponse getPatientById(Long id) {
    	return PatientResponse.fromEntity(
        		patientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient", "id", id))
        );

    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll()
        		.stream()
        		.map(PatientResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePatient(Long id) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Patient", "id", id));
        patientRepository.delete(patient);

    }
    

    @Override
    @Transactional(readOnly = true)
    public Patient getPatientEntityById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
    }

}