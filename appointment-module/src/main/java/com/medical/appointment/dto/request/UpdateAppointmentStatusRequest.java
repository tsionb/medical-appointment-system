package com.medical.appointment.dto.request;

import com.medical.common.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;


public class UpdateAppointmentStatusRequest {

    @NotNull(message = "Status is required")
    private AppointmentStatus status;

    public UpdateAppointmentStatusRequest() {}

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
}
