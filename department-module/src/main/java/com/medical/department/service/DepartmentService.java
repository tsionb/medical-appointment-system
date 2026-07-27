package com.medical.department.service;
import com.medical.department.entity.Department;
import java.util.List;


public interface DepartmentService {
	Department saveDepartment(Department department);

    List<Department> getAllDepartments();

    Department getDepartmentById(Long id);

    Department updateDepartment(Long id, Department department);

    void deleteDepartment(Long id);
}
