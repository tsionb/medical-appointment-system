package com.medical.records.service;

import com.medical.records.dto.request.CreateMedicalRecordRequest;
import com.medical.records.dto.request.UpdateMedicalRecordRequest;
import com.medical.records.dto.response.MedicalRecordResponse;

import java.util.List;


public interface MedicalRecordService {

    
    MedicalRecordResponse createMedicalRecord(CreateMedicalRecordRequest request);

    MedicalRecordResponse getMedicalRecordById(Long id);

    MedicalRecordResponse getMedicalRecordByAppointmentId(Long appointmentId);

    List<MedicalRecordResponse> getMedicalRecordsByPatient(Long patientId);

    List<MedicalRecordResponse> getMedicalRecordsByDoctor(Long doctorId);

    MedicalRecordResponse updateMedicalRecord(Long id, UpdateMedicalRecordRequest request);

    void deleteMedicalRecord(Long id);
}
