package com.medical.records.dto.response;

import com.medical.records.entity.Medication;

public class MedicationResponse {

    private Long id;
    private String name;
    private String description;
    private String category;

    public MedicationResponse() {}

    public static MedicationResponse fromEntity(Medication medication) {
        MedicationResponse response = new MedicationResponse();
        response.setId(medication.getId());
        response.setName(medication.getName());
        response.setDescription(medication.getDescription());
        response.setCategory(medication.getCategory());
        return response;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}