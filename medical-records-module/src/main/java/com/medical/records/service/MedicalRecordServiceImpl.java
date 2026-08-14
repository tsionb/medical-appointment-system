package com.medical.records.service;

import com.medical.appointment.entity.Appointment;
import com.medical.appointment.repository.AppointmentRepository;
import com.medical.common.enums.AppointmentStatus;
import com.medical.common.exception.custom.AppointmentNotCompletedException;
import com.medical.common.exception.custom.DuplicateResourceException;
import com.medical.common.exception.custom.ResourceNotFoundException;
import com.medical.records.dto.request.CreateMedicalRecordRequest;
import com.medical.records.dto.request.UpdateMedicalRecordRequest;
import com.medical.records.dto.response.MedicalRecordResponse;
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
public class MedicalRecordServiceImpl {

    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicationRepository medicationRepository;

    public MedicalRecordServiceImpl(MedicalRecordRepository medicalRecordRepository,
                                AppointmentRepository appointmentRepository,
                                MedicationRepository medicationRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.appointmentRepository = appointmentRepository;
        this.medicationRepository = medicationRepository;
    }


    
    @Transactional
    public MedicalRecordResponse createMedicalRecord(CreateMedicalRecordRequest request) {


        Appointment appointment = appointmentRepository
                .findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Appointment", "id", request.getAppointmentId()));

        
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new AppointmentNotCompletedException(
                "Cannot create a medical record for an appointment with status: " +
                appointment.getStatus() +
                ". The appointment must be COMPLETED first."
            );
        }


        if (medicalRecordRepository.existsByAppointmentId(
                request.getAppointmentId())) {
            throw new DuplicateResourceException(
                "A medical record already exists for appointment ID " +
                request.getAppointmentId() +
                ". Only one medical record is allowed per appointment."
            );
        }

        MedicalRecord record = new MedicalRecord();
        record.setAppointment(appointment);
        record.setDiagnosis(request.getDiagnosis());
        record.setNotes(request.getNotes());
        record.setFollowUpRecommendation(request.getFollowUpRecommendation());

        return MedicalRecordResponse.fromEntity(
                medicalRecordRepository.save(record));
    }

 
    @Transactional(readOnly = true)
    public MedicalRecordResponse getMedicalRecordById(Long id) {
    	return MedicalRecordResponse.fromEntity(
                medicalRecordRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                            "MedicalRecord", "id", id))
        );
    }

    @Transactional(readOnly = true)
    public MedicalRecordResponse getMedicalRecordByAppointmentId(Long appointmentId) {
    	return MedicalRecordResponse.fromEntity(
                medicalRecordRepository.findByAppointmentId(appointmentId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                            "MedicalRecord", "appointmentId", appointmentId))
        );
    }

    

    @Transactional(readOnly = true)
    public List<MedicalRecordResponse> getMedicalRecordsByPatient(Long patientId) {
        return medicalRecordRepository.findByAppointmentPatientId(patientId)
                .stream()
                .map(MedicalRecordResponse::fromEntity)
                .collect(Collectors.toList());
    }



    @Transactional(readOnly = true)
    public List<MedicalRecordResponse> getMedicalRecordsByDoctor(Long doctorId) {
        return medicalRecordRepository.findByAppointmentDoctorId(doctorId)
                .stream()
                .map(MedicalRecordResponse::fromEntity)
                .collect(Collectors.toList());
    }



    @Transactional
    public MedicalRecordResponse updateMedicalRecord(Long id, UpdateMedicalRecordRequest request) {

        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "MedicalRecord", "id", id));

        if (request.getDiagnosis() != null) {
            record.setDiagnosis(request.getDiagnosis());
        }
        if (request.getNotes() != null) {
            record.setNotes(request.getNotes());
        }
        if (request.getFollowUpRecommendation() != null) {
            record.setFollowUpRecommendation(request.getFollowUpRecommendation());
        }

        return MedicalRecordResponse.fromEntity(
                medicalRecordRepository.save(record));
    }



    @Transactional
    public void deleteMedicalRecord(Long id) {
        if (!medicalRecordRepository.existsById(id)) {
            throw new ResourceNotFoundException("MedicalRecord", "id", id);
        }

        medicalRecordRepository.deleteById(id);
    }
}