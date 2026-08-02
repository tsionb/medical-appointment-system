package com.medical.department.controller;

import com.medical.department.dto.request.CreateDepartmentScheduleRequest;
import com.medical.department.dto.response.DepartmentScheduleResponse;
import com.medical.department.service.DepartmentScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments/{departmentId}/schedules")
public class DepartmentScheduleController {

	private final DepartmentScheduleService scheduleService;
	
	public DepartmentScheduleController(DepartmentScheduleService scheduleService) {
	    this.scheduleService = scheduleService;
	}
	
	@GetMapping
	public ResponseEntity<List<DepartmentScheduleResponse>> getOperatingHours(
            @PathVariable Long departmentId) {

        return ResponseEntity.ok(scheduleService.getOperatingHours(departmentId));
    }
	
	@GetMapping("/{scheduleId}")
	public ResponseEntity<DepartmentScheduleResponse> getOperatingHoursById(
            @PathVariable Long departmentId,
            @PathVariable Long scheduleId) {

        return ResponseEntity.ok(scheduleService.getOperatingHoursById(scheduleId));
    }
	
	@PostMapping
	public ResponseEntity<DepartmentScheduleResponse> addOperatingHours(
            @PathVariable Long departmentId,
            @Valid @RequestBody CreateDepartmentScheduleRequest request) {

        DepartmentScheduleResponse response =
                scheduleService.addOperatingHours(departmentId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
	
	
	
	@DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteOperatingHours(
            @PathVariable Long departmentId,
            @PathVariable Long scheduleId) {

        scheduleService.deleteOperatingHours(departmentId, scheduleId);
        return ResponseEntity.noContent().build();
    }
}
