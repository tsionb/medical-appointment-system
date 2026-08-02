package com.medical.department.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalTime;

public class CreateDepartmentScheduleRequest {

    
    @NotBlank(message = "Day of week is required")
    @Pattern(
        regexp = "^(MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY)$",
        message = "Day must be one of: MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY"
    )
    private String dayOfWeek;

    @NotNull(message = "Open time is required")
    private LocalTime openTime;

    @NotNull(message = "Close time is required")
    private LocalTime closeTime;

    public CreateDepartmentScheduleRequest() {}

    public String getDayOfWeek() { return dayOfWeek; }

    
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek != null ? dayOfWeek.toUpperCase() : null;
    }

    public LocalTime getOpenTime() { return openTime; }
    public void setOpenTime(LocalTime openTime) { this.openTime = openTime; }

    public LocalTime getCloseTime() { return closeTime; }
    public void setCloseTime(LocalTime closeTime) { this.closeTime = closeTime; }
}