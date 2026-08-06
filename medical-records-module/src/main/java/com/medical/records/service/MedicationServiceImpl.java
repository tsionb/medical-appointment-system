package com.medical.records.service;

import com.medical.common.exception.custom.DuplicateResourceException;
import com.medical.common.exception.custom.ResourceNotFoundException;
import com.medical.records.dto.request.*;
import com.medical.records.dto.response.*;
import com.medical.records.entity.Medication;
import com.medical.records.repository.MedicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepository;

    public MedicationServiceImpl(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @Override
    @Transactional
    public MedicationResponse createMedication(CreateMedicationRequest dto) {
    	if (medicationRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException(
                "Medication '" + dto.getName() + "' already exists"
            );
        }

        Medication medication = new Medication();
        medication.setName(dto.getName());
        medication.setDescription(dto.getDescription());
        medication.setCategory(dto.getCategory());

        return MedicationResponse.fromEntity(medicationRepository.save(medication));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicationResponse> getAllMedications() {
        return medicationRepository.findAll()
                .stream()
                .map(MedicationResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MedicationResponse> searchByName(String name) {
        return medicationRepository.findByNameIgnoreCase(name)
                .stream()
                .map(MedicationResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MedicationResponse> getByCategory(String category) {
        return medicationRepository.findByCategory(category)
                .stream()
                .map(MedicationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MedicationResponse getMedicationById(Long id) {
    	return MedicationResponse.fromEntity(
                medicationRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                            "Medication", "id", id))
        );
    }

    @Override
    @Transactional
    public MedicationResponse updateMedication(Long id, CreateMedicationRequest request) {

        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Medication", "id", id));

        if (request.getName() != null && !request.getName().isBlank()) {
            boolean nameChanged = !request.getName().equals(medication.getName());
            if (nameChanged && medicationRepository.existsByName(request.getName())) {
                throw new DuplicateResourceException(
                    "Medication '" + request.getName() + "' already exists"
                );
            }
            medication.setName(request.getName());
        }

        if (request.getDescription() != null) {
            medication.setDescription(request.getDescription());
        }
        if (request.getCategory() != null && !request.getCategory().isBlank()) {
            medication.setCategory(request.getCategory());
        }

        return MedicationResponse.fromEntity(medicationRepository.save(medication));
    }

    @Override
    @Transactional
    public void deleteMedication(Long id) {
        if (!medicationRepository.existsById(id)) {
            throw new RuntimeException("Medication not found with ID: " + id);
        }
        medicationRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Medication getMedicationEntityById(Long id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Medication", "id", id));
    }
}