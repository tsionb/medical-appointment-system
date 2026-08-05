package com.medical.records.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class MedicalRecordCreateDto {

    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;

    @NotBlank(message = "Diagnosis is required")
    private String diagnosis;

    private String notes;
    private String followUpRecommendation;

    @Valid
    private List<PrescriptionRequestDto> prescriptions;

    public MedicalRecordCreateDto() {}

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getFollowUpRecommendation() { return followUpRecommendation; }
    public void setFollowUpRecommendation(String followUpRecommendation) { this.followUpRecommendation = followUpRecommendation; }

    public List<PrescriptionRequestDto> getPrescriptions() { return prescriptions; }
    public void setPrescriptions(List<PrescriptionRequestDto> prescriptions) { this.prescriptions = prescriptions; }
}