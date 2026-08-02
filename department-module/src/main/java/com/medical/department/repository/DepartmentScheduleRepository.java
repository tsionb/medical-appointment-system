package com.medical.department.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.medical.department.entity.DepartmentSchedule;

public interface DepartmentScheduleRepository extends JpaRepository<DepartmentSchedule, Long>{
	
	List<DepartmentSchedule> findByDepartmentId(Long departmentId);
	
	boolean existsByDepartmentIdAndDayOfWeek(Long departmentId, String dayOfWeek);
	
	void deleteByDepartmentId(Long departmentId);

}
