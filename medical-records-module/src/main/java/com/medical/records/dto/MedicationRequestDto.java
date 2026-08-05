package com.medical.records.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MedicationRequestDto {

    @NotBlank(message = "Medication name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    private String description;

    @Size(max = 50, message = "Category must not exceed 50 characters")
    private String category;

    public MedicationRequestDto() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}