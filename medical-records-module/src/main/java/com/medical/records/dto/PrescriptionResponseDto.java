package com.medical.records.dto;

public class PrescriptionResponseDto {

    private Long id;
    private Long medicationId;
    private String medicationName;
    private String dosage;
    private String frequency;
    private String duration;
    private String instructions;

    public PrescriptionResponseDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMedicationId() { return medicationId; }
    public void setMedicationId(Long medicationId) { this.medicationId = medicationId; }

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