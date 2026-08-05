package com.medical.records.service;

import com.medical.records.dto.MedicationRequestDto;
import com.medical.records.dto.MedicationResponseDto;
import com.medical.records.entity.Medication;
import com.medical.records.repository.MedicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;

    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @Transactional
    public MedicationResponseDto createMedication(MedicationRequestDto dto) {
        medicationRepository.findByNameIgnoreCase(dto.getName())
                .ifPresent(m -> {
                    throw new IllegalArgumentException("Medication with name '" + dto.getName() + "' already exists.");
                });

        Medication medication = new Medication();
        medication.setName(dto.getName());
        medication.setDescription(dto.getDescription());
        medication.setCategory(dto.getCategory());

        Medication savedMedication = medicationRepository.save(medication);
        return mapToResponseDto(savedMedication);
    }

    @Transactional(readOnly = true)
    public List<MedicationResponseDto> getAllMedications() {
        return medicationRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MedicationResponseDto getMedicationById(Long id) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found with ID: " + id));
        return mapToResponseDto(medication);
    }

    @Transactional
    public MedicationResponseDto updateMedication(Long id, MedicationRequestDto dto) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found with ID: " + id));

        if (!medication.getName().equalsIgnoreCase(dto.getName())) {
            medicationRepository.findByNameIgnoreCase(dto.getName())
                    .ifPresent(m -> {
                        throw new IllegalArgumentException("Medication with name '" + dto.getName() + "' already exists.");
                    });
        }

        medication.setName(dto.getName());
        medication.setDescription(dto.getDescription());
        medication.setCategory(dto.getCategory());

        Medication updatedMedication = medicationRepository.save(medication);
        return mapToResponseDto(updatedMedication);
    }

    @Transactional
    public void deleteMedication(Long id) {
        if (!medicationRepository.existsById(id)) {
            throw new RuntimeException("Medication not found with ID: " + id);
        }
        medicationRepository.deleteById(id);
    }

    private MedicationResponseDto mapToResponseDto(Medication medication) {
        return new MedicationResponseDto(
                medication.getId(),
                medication.getName(),
                medication.getDescription(),
                medication.getCategory()
        );
    }
}