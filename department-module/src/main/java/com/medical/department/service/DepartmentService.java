package com.medical.department.service;

import java.util.List;

import com.medical.department.dto.DepartmentRequest;
import com.medical.department.dto.DepartmentResponse;

public interface DepartmentService {

    DepartmentResponse saveDepartment(DepartmentRequest request);

    List<DepartmentResponse> getAllDepartments();

    DepartmentResponse getDepartmentById(Long id);

    DepartmentResponse updateDepartment(Long id, DepartmentRequest request);

    void deleteDepartment(Long id);
}