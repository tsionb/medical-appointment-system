package com.medical.department.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.medical.department.entity.DepartmentSchedule;

public interface DepartmentScheduleRepository extends JpaRepository<DepartmentSchedule, Long>{

}
