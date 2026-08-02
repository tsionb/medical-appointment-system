package com.medical.department.service;

import com.medical.common.exception.custom.DuplicateResourceException;
import com.medical.common.exception.custom.ResourceNotFoundException;
import com.medical.department.dto.request.CreateDepartmentRequest;
import com.medical.department.dto.request.UpdateDepartmentRequest;
import com.medical.department.dto.response.DepartmentResponse;
import com.medical.department.entity.Department;
import com.medical.department.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    
    @Override
    @Transactional
    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {

        
        if (departmentRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException(
                "Department with name '" + request.getName() + "' already exists"
            );
        }

       
        Department department = new Department();
        department.setName(request.getName());
        department.setDescription(request.getDescription());

       
        Department saved = departmentRepository.save(department);

       
        return DepartmentResponse.fromEntity(saved);
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {
        
        return departmentRepository.findAll()
                .stream()
                .map(DepartmentResponse::fromEntity)
                .collect(Collectors.toList());
    }

  
    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long id) {
       
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        return DepartmentResponse.fromEntity(department);
    }

    
    @Override
    @Transactional
    public DepartmentResponse updateDepartment(Long id, UpdateDepartmentRequest request) {

        
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        
        if (request.getName() != null && !request.getName().isBlank()) {

            
            boolean nameChanged = !request.getName().equals(department.getName());
            if (nameChanged && departmentRepository.existsByName(request.getName())) {
                throw new DuplicateResourceException(
                    "Department with name '" + request.getName() + "' already exists"
                );
            }
            department.setName(request.getName());
        }

        
        if (request.getDescription() != null) {
            department.setDescription(request.getDescription());
        }

       
        Department updated = departmentRepository.save(department);
        return DepartmentResponse.fromEntity(updated);
    }

   
    @Override
    @Transactional
    public void deleteDepartment(Long id) {
       
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department", "id", id);
        }
        
        departmentRepository.deleteById(id);
    }

   
    @Override
    @Transactional(readOnly = true)
    public Department getDepartmentEntityById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
    }
}