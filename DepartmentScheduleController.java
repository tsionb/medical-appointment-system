package com.medical.department.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.medical.department.entity.DepartmentSchedule;
import com.medical.department.service.DepartmentScheduleService;

@RestController
@RequestMapping("/department-schedules")
public class DepartmentScheduleController {

	private final DepartmentScheduleService scheduleService;
	
	public DepartmentScheduleController(DepartmentScheduleService scheduleService) {
	    this.scheduleService = scheduleService;
	}
	
	@GetMapping
	public List<DepartmentSchedule> getAllSchedules() {
	    return scheduleService.getAllSchedules();
	}
	
	@GetMapping("/{id}")
	public DepartmentSchedule getSchedule(@PathVariable Long id) {
		return scheduleService.getScheduleById(id);
	}
	@PostMapping
	public DepartmentSchedule saveSchedule(@RequestBody DepartmentSchedule schedule) {

	    return scheduleService.saveSchedule(schedule);

	}
	
	@PutMapping("/{id}")
	public DepartmentSchedule updateSchedule(@PathVariable Long id,
	                                   @RequestBody DepartmentSchedule schedule) {

	    return scheduleService.updateSchedule(id, schedule);
	}
	
	@DeleteMapping("/{id}")
	public void deleteSchedule(@PathVariable Long id) {

	    scheduleService.deleteSchedule(id);

	}
}
