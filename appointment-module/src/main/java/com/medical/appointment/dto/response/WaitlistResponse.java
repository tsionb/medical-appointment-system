package com.medical.appointment.dto.response;

import com.medical.appointment.entity.Waitlist;
import com.medical.common.enums.WaitlistStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WaitlistResponse {

    private Long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private LocalDate requestedDate;
    private WaitlistStatus status;
    private LocalDateTime createdAt;

    public WaitlistResponse() {}

    public static WaitlistResponse fromEntity(Waitlist waitlist) {
        WaitlistResponse response = new WaitlistResponse();
        response.setId(waitlist.getId());
        response.setRequestedDate(waitlist.getRequestedDate());
        response.setStatus(waitlist.getStatus());
        response.setCreatedAt(waitlist.getCreatedAt());

        if (waitlist.getPatient() != null) {
            response.setPatientId(waitlist.getPatient().getId());
            response.setPatientName(
                waitlist.getPatient().getFirstName() + " " +
                waitlist.getPatient().getLastName()
            );
        }

        if (waitlist.getDoctor() != null) {
            response.setDoctorId(waitlist.getDoctor().getId());
            response.setDoctorName(
                waitlist.getDoctor().getFirstName() + " " +
                waitlist.getDoctor().getLastName()
            );
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

    public LocalDate getRequestedDate() { return requestedDate; }
    public void setRequestedDate(LocalDate requestedDate) { this.requestedDate = requestedDate; }

    public WaitlistStatus getStatus() { return status; }
    public void setStatus(WaitlistStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
