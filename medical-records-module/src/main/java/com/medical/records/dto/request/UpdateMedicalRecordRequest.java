package com.medical.records.dto.request;

import jakarta.validation.constraints.Size;


public class UpdateMedicalRecordRequest {

    @Size(max = 2000, message = "Diagnosis cannot exceed 2000 characters")
    private String diagnosis;

    @Size(max = 5000, message = "Notes cannot exceed 5000 characters")
    private String notes;

    @Size(max = 2000, message = "Follow-up recommendation cannot exceed 2000 characters")
    private String followUpRecommendation;

    public UpdateMedicalRecordRequest() {}

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getFollowUpRecommendation() { return followUpRecommendation; }
    public void setFollowUpRecommendation(String followUpRecommendation) {
        this.followUpRecommendation = followUpRecommendation;
    }
}