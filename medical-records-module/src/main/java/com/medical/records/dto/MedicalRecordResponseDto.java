package com.medical.records.dto;

import java.time.LocalDateTime;
import java.util.List;

public class MedicalRecordResponseDto {

    private Long id;
    private Long appointmentId;
    private String diagnosis;
    private String notes;
    private String followUpRecommendation;
    private LocalDateTime createdAt;
    private List<PrescriptionResponseDto> prescriptions;

    public MedicalRecordResponseDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getFollowUpRecommendation() { return followUpRecommendation; }
    public void setFollowUpRecommendation(String followUpRecommendation) { this.followUpRecommendation = followUpRecommendation; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<PrescriptionResponseDto> getPrescriptions() { return prescriptions; }
    public void setPrescriptions(List<PrescriptionResponseDto> prescriptions) { this.prescriptions = prescriptions; }
}