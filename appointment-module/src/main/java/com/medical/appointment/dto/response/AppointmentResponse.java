package com.medical.appointment.dto.response;

import com.medical.appointment.entity.Appointment;
import com.medical.common.enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class AppointmentResponse {

    private Long id;

    // Patient details
    private Long patientId;
    private String patientName;

    // Doctor details
    private Long doctorId;
    private String doctorName;
    private String doctorSpecialization;

    // Schedule details 
    private Long scheduleId;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;

    // Department details
    private String departmentName;

    private AppointmentStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private boolean reminderSent;

    public AppointmentResponse() {}

    public static AppointmentResponse fromEntity(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setStatus(appointment.getStatus());
        response.setNotes(appointment.getNotes());
        response.setCreatedAt(appointment.getCreatedAt());
        response.setReminderSent(appointment.isReminderSent());

        // Patient details
        if (appointment.getPatient() != null) {
            response.setPatientId(appointment.getPatient().getId());
            response.setPatientName(
                appointment.getPatient().getFirstName() + " " +
                appointment.getPatient().getLastName()
            );
        }

        // Doctor details
        if (appointment.getDoctor() != null) {
            response.setDoctorId(appointment.getDoctor().getId());
            response.setDoctorName(
                appointment.getDoctor().getFirstName() + " " +
                appointment.getDoctor().getLastName()
            );
            response.setDoctorSpecialization(appointment.getDoctor().getSpecialization());

            // Department name through doctor
            if (appointment.getDoctor().getDepartment() != null) {
                response.setDepartmentName(
                    appointment.getDoctor().getDepartment().getName()
                );
            }
        }

        // Schedule details
        if (appointment.getSchedule() != null) {
            response.setScheduleId(appointment.getSchedule().getId());
            response.setAppointmentDate(appointment.getSchedule().getDate());
            response.setStartTime(appointment.getSchedule().getStartTime());
            response.setEndTime(appointment.getSchedule().getEndTime());
        }

        return response;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getDoctorSpecialization() { return doctorSpecialization; }
    public void setDoctorSpecialization(String s) { this.doctorSpecialization = s; }

    public Long getScheduleId() { return scheduleId; }
    public void setScheduleId(Long scheduleId) { this.scheduleId = scheduleId; }

    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isReminderSent() { return reminderSent; }
    public void setReminderSent(boolean reminderSent) { this.reminderSent = reminderSent; }
}
