package com.medical.department.controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.medical.department.service.DepartmentService;
import com.medical.department.entity.Department;


@RestController
@RequestMapping("/departments")
public class DepartmentController {
	
	private final DepartmentService departmentService;
	
	public DepartmentController(DepartmentService departmentService) {
	    this.departmentService = departmentService;
	}
	
	@GetMapping
	public List<Department> getAllDepartments() {
	    return departmentService.getAllDepartments();
	}
	
	@PostMapping
	public Department saveDepartment(@RequestBody Department department) {

	    return departmentService.saveDepartment(department);

	}
	
	@PutMapping("/{id}")
	public Department updateDepartment(@PathVariable Long id,
	                                   @RequestBody Department department) {

	    return departmentService.updateDepartment(id, department);
	}
	
	@DeleteMapping("/{id}")
	public void deleteDepartment(@PathVariable Long id) {

	    departmentService.deleteDepartment(id);

	}
}
