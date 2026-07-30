package com.medical.department.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.medical.department.dto.*;
import com.medical.department.service.DepartmentScheduleService;

@RestController
@RequestMapping("/department-schedules")
public class DepartmentScheduleController {

	private final DepartmentScheduleService scheduleService;
	
	public DepartmentScheduleController(DepartmentScheduleService scheduleService) {
	    this.scheduleService = scheduleService;
	}
	
	@GetMapping
	public List<DepartmentScheduleResponse> getAllDepartmentSchedules() {
	    return scheduleService.getAllSchedules();
	}
	
	@GetMapping("/{id}")
	public DepartmentScheduleResponse getDepartmentSchedule(
	        @PathVariable Long id) {

	    return scheduleService.getScheduleById(id);
	}
	
	@PostMapping
	public DepartmentScheduleResponse saveDepartmentSchedule(
	        @RequestBody DepartmentScheduleRequest request) {

	    return scheduleService.saveSchedule(request);
	}
	
	@PutMapping("/{id}")
	public DepartmentScheduleResponse updateDepartmentSchedule(
	        @PathVariable Long id,
	        @RequestBody DepartmentScheduleRequest request) {

	    return scheduleService.updateSchedule(id, request);
	}
	
	@DeleteMapping("/{id}")
	public void deleteSchedule(@PathVariable Long id) {

	    scheduleService.deleteSchedule(id);

	}
}
