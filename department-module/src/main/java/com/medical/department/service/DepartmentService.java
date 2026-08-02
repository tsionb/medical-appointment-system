package com.medical.department.service;

import com.medical.department.dto.request.CreateDepartmentRequest;
import com.medical.department.dto.request.UpdateDepartmentRequest;
import com.medical.department.dto.response.DepartmentResponse;
import com.medical.department.entity.Department;

import java.util.List;

public interface DepartmentService {

    DepartmentResponse createDepartment(CreateDepartmentRequest request);

    List<DepartmentResponse> getAllDepartments();

    DepartmentResponse getDepartmentById(Long id);

    DepartmentResponse updateDepartment(Long id, UpdateDepartmentRequest request);

    void deleteDepartment(Long id);
    
    Department getDepartmentEntityById(Long id);
}