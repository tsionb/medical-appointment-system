package com.medical.department.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.medical.department.entity.Department;
import com.medical.department.repository.DepartmentRepository;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	private final DepartmentRepository departmentRepository;
	
	public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }
	
	@Override
	public Department saveDepartment(Department department) {
		return departmentRepository.save(department);
	}

	@Override
	public List<Department> getAllDepartments() {
		return departmentRepository.findAll();
	}

	@Override
	public Department getDepartmentById(Long id) {
		return departmentRepository.findById(id).orElseThrow(() ->
        new RuntimeException("Department not found with id " + id));
	}

	@Override
	public Department updateDepartment(Long id, Department department) {
		Department existingDepartment = departmentRepository.findById(id)
	            .orElseThrow(() ->
	                    new RuntimeException("Department not found with id " + id));

	    existingDepartment.setName(department.getName());
	    existingDepartment.setDescription(department.getDescription());

	    return departmentRepository.save(existingDepartment);
	}

	@Override
	public void deleteDepartment(Long id) {
		Department existingDepartment = departmentRepository.findById(id)
	            .orElseThrow(() ->
	                    new RuntimeException("Department not found with id " + id));

	    departmentRepository.delete(existingDepartment);
	}

}
