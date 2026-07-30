package com.medical.department.controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.medical.department.service.DepartmentService;
import com.medical.department.dto.*;


@RestController
@RequestMapping("/departments")
public class DepartmentController {
	
	private final DepartmentService departmentService;
	
	public DepartmentController(DepartmentService departmentService) {
	    this.departmentService = departmentService;
	}
	
	@GetMapping
	public List<DepartmentResponse> getAllDepartments() {
	    return departmentService.getAllDepartments();
	}
	
	@GetMapping("/{id}")
	public DepartmentResponse getDepartment(@PathVariable Long id) {
	    return departmentService.getDepartmentById(id);
	}
	
	@PostMapping
	public DepartmentResponse saveDepartment(
	        @RequestBody DepartmentRequest request) {

	    return departmentService.saveDepartment(request);
	}
	
	@PutMapping("/{id}")
	public DepartmentResponse updateDepartment(
	        @PathVariable Long id,
	        @RequestBody DepartmentRequest request) {

	    return departmentService.updateDepartment(id, request);
	}
	
	@DeleteMapping("/{id}")
	public void deleteDepartment(@PathVariable Long id) {

	    departmentService.deleteDepartment(id);

	}
}
