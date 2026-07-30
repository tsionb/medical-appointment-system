package com.medical.department.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.medical.department.entity.Department;
import com.medical.department.repository.DepartmentRepository;
import com.medical.department.dto.*;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	private final DepartmentRepository departmentRepository;
	
	public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }
	
	@Override
	public DepartmentResponse saveDepartment(DepartmentRequest request) {

	    Department department = toEntity(request);

	    Department savedDepartment = departmentRepository.save(department);

	    return toResponse(savedDepartment);
	}

	@Override
	public List<DepartmentResponse> getAllDepartments() {

	    return departmentRepository.findAll()
	            .stream()
	            .map(this::toResponse)
	            .toList();
	}

	@Override
	public DepartmentResponse getDepartmentById(Long id) {

	    Department department = departmentRepository.findById(id)
	            .orElseThrow(() ->
	                    new RuntimeException("Department not found with id " + id));

	    return toResponse(department);
	}

	@Override
	public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
		Department existingDepartment = departmentRepository.findById(id)
	            .orElseThrow(() ->
	                    new RuntimeException("Department not found with id " + id));

		existingDepartment.setName(request.getName());
		existingDepartment.setDescription(request.getDescription());

		Department updatedDepartment =
		        departmentRepository.save(existingDepartment);

		return toResponse(updatedDepartment);
	}

	@Override
	public void deleteDepartment(Long id) {
		Department existingDepartment = departmentRepository.findById(id)
	            .orElseThrow(() ->
	                    new RuntimeException("Department not found with id " + id));

	    departmentRepository.delete(existingDepartment);
	}
	private Department toEntity(DepartmentRequest request) {

	    Department department = new Department();

	    department.setName(request.getName());
	    department.setDescription(request.getDescription());

	    return department;
	}
	private DepartmentResponse toResponse(Department department) {

	    DepartmentResponse response = new DepartmentResponse();

	    response.setId(department.getId());
	    response.setName(department.getName());
	    response.setDescription(department.getDescription());

	    return response;
	}
}
