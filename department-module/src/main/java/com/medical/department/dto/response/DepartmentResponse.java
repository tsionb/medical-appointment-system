package com.medical.department.dto.response;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.medical.department.entity.Department;

public class DepartmentResponse {
    
	private Long id;
    private String name;
    private String description;
    private List<DepartmentScheduleResponse> operatingHours;
    
    public DepartmentResponse() {}

    public static DepartmentResponse fromEntity(Department department) {
        DepartmentResponse response = new DepartmentResponse();
        response.setId(department.getId());
        response.setName(department.getName());
        response.setDescription(department.getDescription());

        // operatingHours is LAZY — might not be loaded
        // Null check prevents NullPointerException
        if (department.getOperatingHours() != null) {
            response.setOperatingHours(
                department.getOperatingHours()
                    .stream()
                    .map(DepartmentScheduleResponse::fromEntity)
                    .collect(Collectors.toList())
            );
        } else {
            response.setOperatingHours(Collections.emptyList());
        }

        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public List<DepartmentScheduleResponse> getOperatingHours() { 
    	return operatingHours; 
    } 
    
    public void setOperatingHours(List<DepartmentScheduleResponse> operatingHours) {
        this.operatingHours = operatingHours;
    }
}
