package com.medical.records.service;

import com.medical.records.dto.request.CreateMedicationRequest;
import com.medical.records.dto.response.MedicationResponse;
import com.medical.records.entity.Medication;

import java.util.List;


public interface MedicationService {

    MedicationResponse createMedication(CreateMedicationRequest request);

    List<MedicationResponse> getAllMedications();

    List<MedicationResponse> searchByName(String name);

    List<MedicationResponse> getByCategory(String category);

    MedicationResponse getMedicationById(Long id);

    MedicationResponse updateMedication(Long id, CreateMedicationRequest request);

    void deleteMedication(Long id);

    Medication getMedicationEntityById(Long id);
}
