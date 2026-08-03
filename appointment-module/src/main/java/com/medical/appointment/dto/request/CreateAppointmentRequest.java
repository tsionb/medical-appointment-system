package com.medical.appointment.dto.request;

import jakarta.validation.constraints.NotNull;


public class CreateAppointmentRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    @NotNull(message = "Schedule ID is required")
    private Long scheduleId;

    private String notes;

    public CreateAppointmentRequest() {}

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public Long getScheduleId() { return scheduleId; }
    public void setScheduleId(Long scheduleId) { this.scheduleId = scheduleId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
