package com.medical.department.service;

import com.medical.common.exception.custom.DuplicateResourceException;
import com.medical.common.exception.custom.ResourceNotFoundException;
import com.medical.department.dto.request.CreateDepartmentScheduleRequest;
import com.medical.department.dto.response.DepartmentScheduleResponse;
import com.medical.department.entity.Department;
import com.medical.department.entity.DepartmentSchedule;
import com.medical.department.repository.DepartmentRepository;
import com.medical.department.repository.DepartmentScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class DepartmentScheduleServiceImpl implements DepartmentScheduleService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentScheduleRepository scheduleRepository;

    public DepartmentScheduleServiceImpl(
            DepartmentRepository departmentRepository,
            DepartmentScheduleRepository scheduleRepository) {
        this.departmentRepository = departmentRepository;
        this.scheduleRepository = scheduleRepository;
    }

   
    @Override
    @Transactional
    public DepartmentScheduleResponse addOperatingHours(
            Long departmentId,
            CreateDepartmentScheduleRequest request) {

        
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Department", "id", departmentId));

        
        if (scheduleRepository.existsByDepartmentIdAndDayOfWeek(
                departmentId, request.getDayOfWeek())) {
            throw new DuplicateResourceException(
                "Operating hours for " + request.getDayOfWeek() +
                " are already defined for this department. " +
                "Delete the existing entry first if you want to change the hours."
            );
        }

       
        if (!request.getOpenTime().isBefore(request.getCloseTime())) {
            throw new IllegalArgumentException(
                "Open time (" + request.getOpenTime() + ") must be before " +
                "close time (" + request.getCloseTime() + ")"
            );
        }

        
        DepartmentSchedule schedule = new DepartmentSchedule();
        schedule.setDepartment(department);
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setOpenTime(request.getOpenTime());
        schedule.setCloseTime(request.getCloseTime());

        DepartmentSchedule saved = scheduleRepository.save(schedule);
        return DepartmentScheduleResponse.fromEntity(saved);
    }

  
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentScheduleResponse> getOperatingHours(Long departmentId) {
        
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department", "id", departmentId);
        }

        return scheduleRepository.findByDepartmentId(departmentId)
                .stream()
                .map(DepartmentScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }

    
    @Override
    @Transactional(readOnly = true)
    public DepartmentScheduleResponse getOperatingHoursById(Long scheduleId) {
        DepartmentSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "DepartmentSchedule", "id", scheduleId));
        return DepartmentScheduleResponse.fromEntity(schedule);
    }

   
    @Override
    @Transactional
    public void deleteOperatingHours(Long departmentId, Long scheduleId) {

        
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department", "id", departmentId);
        }

        DepartmentSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "DepartmentSchedule", "id", scheduleId));

       
        if (!schedule.getDepartment().getId().equals(departmentId)) {
            throw new ResourceNotFoundException(
                "DepartmentSchedule", "id", scheduleId);
        }

        scheduleRepository.deleteById(scheduleId);
    }
}