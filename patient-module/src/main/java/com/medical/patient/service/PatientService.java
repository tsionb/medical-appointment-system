package com.medical.patient.service;

import java.util.List;

import com.medical.patient.dto.response.*;
import com.medical.patient.entity.Patient;
import com.medical.patient.dto.request.CreatePatientRequest;
import com.medical.patient.dto.request.UpdatePatientRequest;
 

public interface PatientService {

    PatientResponse createPatient(CreatePatientRequest request);

    PatientResponse updatePatient(Long id, UpdatePatientRequest request);

    PatientResponse getPatientById(Long id);

    List<PatientResponse> getAllPatients();

    void deletePatient(Long id);
    
    Patient getPatientEntityById(Long id);
}
