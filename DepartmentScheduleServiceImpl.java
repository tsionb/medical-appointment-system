package com.medical.department.service;
import org.springframework.stereotype.Service;
import java.util.List;

import com.medical.department.repository.*;
import com.medical.department.entity.*;

@Service
public class DepartmentScheduleServiceImpl implements DepartmentScheduleService{

	private final DepartmentRepository departmentRepository;

	private final DepartmentScheduleRepository departmentScheduleRepository;
	
	public DepartmentScheduleServiceImpl(DepartmentRepository departmentRepository, DepartmentScheduleRepository departmentScheduleRepository) {
		this.departmentRepository=departmentRepository;
		this.departmentScheduleRepository=departmentScheduleRepository;
	}
	
	@Override
	public DepartmentSchedule saveSchedule(DepartmentSchedule schedule) {
		Long departmentId = schedule.getDepartment().getId();
		
		Department department = departmentRepository.findById(
		        departmentId)
		    .orElseThrow(() ->
		        new RuntimeException("Department not found"));
		schedule.setDepartment(department);
		return departmentScheduleRepository.save(schedule);
	}

	@Override
	public List<DepartmentSchedule> getAllSchedules() {
		return departmentScheduleRepository.findAll();
	}

	@Override
	public DepartmentSchedule getScheduleById(Long id) {
		DepartmentSchedule schedule = departmentScheduleRepository.findById(id).orElseThrow(() ->
        new RuntimeException("Schedule not found with id " + id));
				
		return schedule;
	}

	@Override
	public DepartmentSchedule updateSchedule(Long id, DepartmentSchedule schedule) {
		
		DepartmentSchedule existingSchedule = departmentScheduleRepository.findById(id).orElseThrow(()->
        new RuntimeException("Schedule not found with id " + id));
		
		Department existingDepartment = departmentRepository.findById(schedule.getDepartment().getId()).orElseThrow(()->
        new RuntimeException("Department not found with id " + id));
		
		existingSchedule.setDayOfWeek(schedule.getDayOfWeek());
		existingSchedule.setOpenTime(schedule.getOpenTime());
		existingSchedule.setCloseTime(schedule.getCloseTime());
		existingSchedule.setDepartment(existingDepartment);
		
		return departmentScheduleRepository.save(existingSchedule);
	}

	@Override
	public void deleteSchedule(Long id) {
		DepartmentSchedule schedule = departmentScheduleRepository.findById(id).orElseThrow(()->
        new RuntimeException("Schedule not found with id " + id));
		
		departmentScheduleRepository.delete(schedule);
		
	}
	
	
}
