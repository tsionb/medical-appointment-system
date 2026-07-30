package com.medical.department.service;
import org.springframework.stereotype.Service;

import java.util.List;

import com.medical.department.repository.*;
import com.medical.department.entity.*;
import com.medical.department.dto.*;

@Service
public class DepartmentScheduleServiceImpl implements DepartmentScheduleService{

	private final DepartmentRepository departmentRepository;

	private final DepartmentScheduleRepository departmentScheduleRepository;
	
	public DepartmentScheduleServiceImpl(DepartmentRepository departmentRepository, DepartmentScheduleRepository departmentScheduleRepository) {
		this.departmentRepository=departmentRepository;
		this.departmentScheduleRepository=departmentScheduleRepository;
	}
	
	@Override
	public DepartmentScheduleResponse saveSchedule(DepartmentScheduleRequest request) {

	    Department department = departmentRepository.findById(request.getDepartmentId())
	            .orElseThrow(() ->
	                    new RuntimeException("Department not found"));

	    DepartmentSchedule schedule = toEntity(request, department);

	    DepartmentSchedule savedSchedule = departmentScheduleRepository.save(schedule);

	    return toResponse(savedSchedule);
	}

	@Override
	public List<DepartmentScheduleResponse> getAllSchedules() {

	    return departmentScheduleRepository.findAll()
	            .stream()
	            .map(this::toResponse)
	            .toList();
	}

	@Override
	public DepartmentScheduleResponse getScheduleById(Long id) {

	    DepartmentSchedule schedule = departmentScheduleRepository.findById(id)
	            .orElseThrow(() ->
	                    new RuntimeException("Schedule not found"));

	    return toResponse(schedule);
	}

	@Override
	public DepartmentScheduleResponse updateSchedule(Long id,
	                                                 DepartmentScheduleRequest request) {

	    DepartmentSchedule existingSchedule =
	            departmentScheduleRepository.findById(id)
	            .orElseThrow(() ->
	                    new RuntimeException("Schedule not found with id " + id));

	    Department department =
	            departmentRepository.findById(request.getDepartmentId())
	            .orElseThrow(() ->
	                    new RuntimeException("Department not found with id " + request.getDepartmentId()));

	    existingSchedule.setDepartment(department);
	    existingSchedule.setDayOfWeek(request.getDayOfWeek());
	    existingSchedule.setOpenTime(request.getOpenTime());
	    existingSchedule.setCloseTime(request.getCloseTime());

	    DepartmentSchedule updatedSchedule =
	            departmentScheduleRepository.save(existingSchedule);

	    return toResponse(updatedSchedule);
	}

	@Override
	public void deleteSchedule(Long id) {
		DepartmentSchedule schedule = departmentScheduleRepository.findById(id).orElseThrow(()->
        new RuntimeException("Schedule not found with id " + id));
		
		departmentScheduleRepository.delete(schedule);
		
	}
	private DepartmentSchedule toEntity(
	        DepartmentScheduleRequest request,
	        Department department) {
		DepartmentSchedule schedule = new DepartmentSchedule();

		schedule.setDepartment(department);
		schedule.setDayOfWeek(request.getDayOfWeek());
		schedule.setOpenTime(request.getOpenTime());
		schedule.setCloseTime(request.getCloseTime());

		return schedule;
	}
	private DepartmentScheduleResponse toResponse(DepartmentSchedule schedule) {

	    DepartmentScheduleResponse response =
	            new DepartmentScheduleResponse();

	    response.setId(schedule.getId());
	    response.setDepartmentId(schedule.getDepartment().getId());
	    response.setDayOfWeek(schedule.getDayOfWeek());
	    response.setOpenTime(schedule.getOpenTime());
	    response.setCloseTime(schedule.getCloseTime());

	    return response;
	}
	
}
