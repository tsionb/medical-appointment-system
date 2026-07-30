package com.medical.patient.service;

import java.util.List;

import com.medical.patient.dto.PatientRequest;
import com.medical.patient.dto.PatientResponse;
 

public interface PatientService {

    PatientResponse createPatient(PatientRequest request);

    PatientResponse updatePatient(Long id, PatientRequest request);

    PatientResponse getPatientById(Long id);

    List<PatientResponse> getAllPatients();

    void deletePatient(Long id);
}
