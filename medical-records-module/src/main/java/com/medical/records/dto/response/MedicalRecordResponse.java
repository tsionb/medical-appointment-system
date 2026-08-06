package com.medical.records.dto.response;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.medical.records.entity.MedicalRecord;

public class MedicalRecordResponse {

    private Long id;
    private Long appointmentId;
    private String patientName;
    private String doctorName;
    private String appointmentDate;
    
    private String diagnosis;
    private String notes;
    private String followUpRecommendation;
    private LocalDateTime createdAt;
    private List<PrescriptionResponse> prescriptions;

    public MedicalRecordResponse() {}
    
    public static MedicalRecordResponse fromEntity(MedicalRecord record) {
        MedicalRecordResponse response = new MedicalRecordResponse();
        response.setId(record.getId());
        response.setDiagnosis(record.getDiagnosis());
        response.setNotes(record.getNotes());
        response.setFollowUpRecommendation(record.getFollowUpRecommendation());
        response.setCreatedAt(record.getCreatedAt());

        // Flatten appointment details
        if (record.getAppointment() != null) {
            response.setAppointmentId(record.getAppointment().getId());

            if (record.getAppointment().getPatient() != null) {
                response.setPatientName(
                    record.getAppointment().getPatient().getFirstName() + " " +
                    record.getAppointment().getPatient().getLastName()
                );
            }

            if (record.getAppointment().getDoctor() != null) {
                response.setDoctorName(
                    record.getAppointment().getDoctor().getFirstName() + " " +
                    record.getAppointment().getDoctor().getLastName()
                );
            }

            if (record.getAppointment().getSchedule() != null) {
                response.setAppointmentDate(
                    record.getAppointment().getSchedule().getDate().toString()
                );
            }
        }

        
        if (record.getPrescriptions() != null) {
            response.setPrescriptions(
                record.getPrescriptions()
                    .stream()
                    .map(PrescriptionResponse::fromEntity)
                    .collect(Collectors.toList())
            );
        } else {
            response.setPrescriptions(Collections.emptyList());
        }

        return response;
    }

    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }
    
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getFollowUpRecommendation() { return followUpRecommendation; }
    public void setFollowUpRecommendation(String followUpRecommendation) { this.followUpRecommendation = followUpRecommendation; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<PrescriptionResponse> getPrescriptions() { return prescriptions; }
    public void setPrescriptions(List<PrescriptionResponse> prescriptions) { this.prescriptions = prescriptions; }
}