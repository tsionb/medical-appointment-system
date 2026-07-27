package com.medical.department.service;
import java.util.List;

import com.medical.department.entity.Department;
import com.medical.department.entity.DepartmentSchedule;

public interface DepartmentScheduleService {
	DepartmentSchedule saveSchedule(DepartmentSchedule schedule);

    List<DepartmentSchedule> getAllSchedules();

    DepartmentSchedule getScheduleById(Long id);

    DepartmentSchedule updateSchedule(Long id, DepartmentSchedule schedule);

    void deleteSchedule(Long id);
}
