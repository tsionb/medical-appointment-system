package com.medical.doctor.service;

import com.medical.doctor.dto.request.CreateDoctorRequest;
import com.medical.doctor.dto.request.UpdateDoctorRequest;
import com.medical.doctor.dto.response.DoctorResponse;
import com.medical.doctor.entity.Doctor;

import java.util.List;

public interface DoctorService {

    DoctorResponse createDoctor(CreateDoctorRequest request);

    List<DoctorResponse> getAllDoctors();

    List<DoctorResponse> getDoctorsByDepartment(Long departmentId);

    List<DoctorResponse> getDoctorsBySpecialization(String specialization);

    DoctorResponse getDoctorById(Long id);

    DoctorResponse updateDoctor(Long id, UpdateDoctorRequest request);

    void deleteDoctor(Long id);

    Doctor getDoctorEntityById(Long id);
}
