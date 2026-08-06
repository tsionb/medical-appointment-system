package com.medical.records.service;

import com.medical.common.exception.custom.ResourceNotFoundException;
import com.medical.records.dto.request.CreatePrescriptionRequest;
import com.medical.records.dto.response.PrescriptionResponse;
import com.medical.records.entity.MedicalRecord;
import com.medical.records.entity.Medication;
import com.medical.records.entity.Prescription;
import com.medical.records.repository.MedicalRecordRepository;
import com.medical.records.repository.MedicationRepository;
import com.medical.records.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicationRepository medicationRepository;

    public PrescriptionServiceImpl(
            PrescriptionRepository prescriptionRepository,
            MedicalRecordRepository medicalRecordRepository,
            MedicationRepository medicationRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.medicationRepository = medicationRepository;
    }


    @Override
    @Transactional
    public PrescriptionResponse addPrescription(
            Long medicalRecordId, CreatePrescriptionRequest request) {


        MedicalRecord record = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "MedicalRecord", "id", medicalRecordId));

        Medication medication = medicationRepository
                .findById(request.getMedicationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Medication", "id", request.getMedicationId()));

        Prescription prescription = new Prescription();
        prescription.setMedicalRecord(record);
        prescription.setMedication(medication);
        prescription.setDosage(request.getDosage());
        prescription.setFrequency(request.getFrequency());
        prescription.setDuration(request.getDuration());
        prescription.setInstructions(request.getInstructions());

        return PrescriptionResponse.fromEntity(
                prescriptionRepository.save(prescription));
    }


    @Override
    @Transactional(readOnly = true)
    public List<PrescriptionResponse> getPrescriptionsByMedicalRecord(
            Long medicalRecordId) {

        if (!medicalRecordRepository.existsById(medicalRecordId)) {
            throw new ResourceNotFoundException(
                "MedicalRecord", "id", medicalRecordId);
        }

        return prescriptionRepository.findByMedicalRecordId(medicalRecordId)
                .stream()
                .map(PrescriptionResponse::fromEntity)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public PrescriptionResponse getPrescriptionById(Long prescriptionId) {
        return PrescriptionResponse.fromEntity(
                prescriptionRepository.findById(prescriptionId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                            "Prescription", "id", prescriptionId))
        );
    }


    @Override
    @Transactional
    public void deletePrescription(Long medicalRecordId, Long prescriptionId) {

        if (!medicalRecordRepository.existsById(medicalRecordId)) {
            throw new ResourceNotFoundException(
                "MedicalRecord", "id", medicalRecordId);
        }


        Prescription prescription = prescriptionRepository
                .findById(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Prescription", "id", prescriptionId));


        if (!prescription.getMedicalRecord().getId().equals(medicalRecordId)) {
            throw new ResourceNotFoundException(
                "Prescription", "id", prescriptionId);
        }

        prescriptionRepository.deleteById(prescriptionId);
    }
}
