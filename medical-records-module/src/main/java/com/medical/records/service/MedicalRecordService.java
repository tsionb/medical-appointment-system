package com.medical.records.service;

import com.medical.appointment.entity.Appointment;
import com.medical.appointment.repository.AppointmentRepository;
import com.medical.records.dto.*;
import com.medical.records.entity.MedicalRecord;
import com.medical.records.entity.Medication;
import com.medical.records.entity.Prescription;
import com.medical.records.repository.MedicalRecordRepository;
import com.medical.records.repository.MedicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicationRepository medicationRepository;

    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository,
                                AppointmentRepository appointmentRepository,
                                MedicationRepository medicationRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.appointmentRepository = appointmentRepository;
        this.medicationRepository = medicationRepository;
    }

    @Transactional
    public MedicalRecordResponseDto createMedicalRecord(MedicalRecordCreateDto dto) {
        if (medicalRecordRepository.existsByAppointmentId(dto.getAppointmentId())) {
            throw new IllegalArgumentException("A medical record already exists for appointment ID: " + dto.getAppointmentId());
        }

        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found with ID: " + dto.getAppointmentId()));

        MedicalRecord record = new MedicalRecord();
        record.setAppointment(appointment);
        record.setDiagnosis(dto.getDiagnosis());
        record.setNotes(dto.getNotes());
        record.setFollowUpRecommendation(dto.getFollowUpRecommendation());

        if (dto.getPrescriptions() != null && !dto.getPrescriptions().isEmpty()) {
            List<Prescription> prescriptions = new ArrayList<>();
            for (PrescriptionRequestDto pDto : dto.getPrescriptions()) {
                Medication medication = medicationRepository.findById(pDto.getMedicationId())
                        .orElseThrow(() -> new RuntimeException("Medication not found with ID: " + pDto.getMedicationId()));

                Prescription prescription = new Prescription();
                prescription.setMedicalRecord(record);
                prescription.setMedication(medication);
                prescription.setDosage(pDto.getDosage());
                prescription.setFrequency(pDto.getFrequency());
                prescription.setDuration(pDto.getDuration());
                prescription.setInstructions(pDto.getInstructions());

                prescriptions.add(prescription);
            }
            record.setPrescriptions(prescriptions);
        }

        MedicalRecord savedRecord = medicalRecordRepository.save(record);
        return mapToResponseDto(savedRecord);
    }

    @Transactional(readOnly = true)
    public MedicalRecordResponseDto getMedicalRecordById(Long id) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical record not found with ID: " + id));
        return mapToResponseDto(record);
    }

    @Transactional(readOnly = true)
    public MedicalRecordResponseDto getMedicalRecordByAppointmentId(Long appointmentId) {
        MedicalRecord record = medicalRecordRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("Medical record not found for appointment ID: " + appointmentId));
        return mapToResponseDto(record);
    }

    private MedicalRecordResponseDto mapToResponseDto(MedicalRecord record) {
        MedicalRecordResponseDto dto = new MedicalRecordResponseDto();
        dto.setId(record.getId());
        dto.setAppointmentId(record.getAppointment().getId());
        dto.setDiagnosis(record.getDiagnosis());
        dto.setNotes(record.getNotes());
        dto.setFollowUpRecommendation(record.getFollowUpRecommendation());
        dto.setCreatedAt(record.getCreatedAt());

        if (record.getPrescriptions() != null) {
            List<PrescriptionResponseDto> pDtos = record.getPrescriptions().stream().map(p -> {
                PrescriptionResponseDto pDto = new PrescriptionResponseDto();
                pDto.setId(p.getId());
                pDto.setMedicationId(p.getMedication().getId());
                pDto.setMedicationName(p.getMedication().getName());
                pDto.setDosage(p.getDosage());
                pDto.setFrequency(p.getFrequency());
                pDto.setDuration(p.getDuration());
                pDto.setInstructions(p.getInstructions());
                return pDto;
            }).collect(Collectors.toList());
            dto.setPrescriptions(pDtos);
        }

        return dto;
    }
}