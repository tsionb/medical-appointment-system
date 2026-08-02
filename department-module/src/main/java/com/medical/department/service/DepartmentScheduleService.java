package com.medical.department.service;
import com.medical.department.dto.request.CreateDepartmentScheduleRequest;
import com.medical.department.dto.response.DepartmentScheduleResponse;

import java.util.List;

public interface DepartmentScheduleService {
	DepartmentScheduleResponse addOperatingHours(Long departmentId, CreateDepartmentScheduleRequest request);

	List<DepartmentScheduleResponse> getOperatingHours(Long departmentId);

	DepartmentScheduleResponse getOperatingHoursById(Long scheduleId);

	void deleteOperatingHours(Long departmentId, Long scheduleId);
}
