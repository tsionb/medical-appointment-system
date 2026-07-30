package com.medical.department.service;
import java.util.List;

import com.medical.department.dto.*;

public interface DepartmentScheduleService {
	DepartmentScheduleResponse saveSchedule(DepartmentScheduleRequest request);

	List<DepartmentScheduleResponse> getAllSchedules();

	DepartmentScheduleResponse getScheduleById(Long id);

	DepartmentScheduleResponse updateSchedule(Long id,
	                                          DepartmentScheduleRequest request);

	void deleteSchedule(Long id);
}
