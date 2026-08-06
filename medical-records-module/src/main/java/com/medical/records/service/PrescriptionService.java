package com.medical.records.service;

import com.medical.records.dto.request.CreatePrescriptionRequest;
import com.medical.records.dto.response.PrescriptionResponse;

import java.util.List;


public interface PrescriptionService {

    PrescriptionResponse addPrescription(Long medicalRecordId, CreatePrescriptionRequest request);

    List<PrescriptionResponse> getPrescriptionsByMedicalRecord(Long medicalRecordId);

    PrescriptionResponse getPrescriptionById(Long prescriptionId);

    void deletePrescription(Long medicalRecordId, Long prescriptionId);
}