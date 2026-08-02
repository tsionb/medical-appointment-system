package com.medical.department.dto.response;

import java.time.LocalTime;

import com.medical.department.entity.DepartmentSchedule;

public class DepartmentScheduleResponse {

    private Long id;
    private Long departmentId;
    private String dayOfWeek;
    private LocalTime openTime;
    private LocalTime closeTime;

    public DepartmentScheduleResponse() {}

    
    public static DepartmentScheduleResponse fromEntity(DepartmentSchedule schedule) {
        DepartmentScheduleResponse response = new DepartmentScheduleResponse();
        response.setId(schedule.getId());
        response.setDayOfWeek(schedule.getDayOfWeek());
        response.setOpenTime(schedule.getOpenTime());
        response.setCloseTime(schedule.getCloseTime());
        return response;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalTime closeTime) {
        this.closeTime = closeTime;
    }
}