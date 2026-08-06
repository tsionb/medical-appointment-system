package com.medical.records.dto.response;

import com.medical.records.entity.Prescription;

public class PrescriptionResponse {

    private Long id;
    private Long medicalRecordId;
    private Long medicationId;
    private String medicationName;
    private String dosage;
    private String frequency;
    private String duration;
    private String instructions;

    public PrescriptionResponse() {}
    
    public static PrescriptionResponse fromEntity(Prescription prescription) {
        PrescriptionResponse response = new PrescriptionResponse();
        response.setId(prescription.getId());
        response.setDosage(prescription.getDosage());
        response.setFrequency(prescription.getFrequency());
        response.setDuration(prescription.getDuration());
        response.setInstructions(prescription.getInstructions());

        if (prescription.getMedicalRecord() != null) {
            response.setMedicalRecordId(prescription.getMedicalRecord().getId());
        }

        
        if (prescription.getMedication() != null) {
            response.setMedicationId(prescription.getMedication().getId());
            response.setMedicationName(prescription.getMedication().getName());
        }

        return response;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMedicationId() { return medicationId; }
    public void setMedicationId(Long medicationId) { this.medicationId = medicationId; }
    

    public Long getMedicalRecordId() {
		return medicalRecordId;
	}

	public void setMedicalRecordId(Long medicalRecordId) {
		this.medicalRecordId = medicalRecordId;
	}

	public String getMedicationName() { return medicationName; }
    public void setMedicationName(String medicationName) { this.medicationName = medicationName; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
}