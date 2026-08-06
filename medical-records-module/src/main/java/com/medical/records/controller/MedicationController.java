package com.medical.records.controller;

import com.medical.records.dto.request.CreateMedicationRequest;
import com.medical.records.dto.response.MedicationResponse;
import com.medical.records.service.MedicationServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
public class MedicationController {

    private final MedicationServiceImpl medicationService;

    public MedicationController(MedicationServiceImpl medicationService) {
        this.medicationService = medicationService;
    }

    @PostMapping
    public ResponseEntity<MedicationResponse> createMedication(@Valid @RequestBody CreateMedicationRequest requestDto) {
        MedicationResponse created = medicationService.createMedication(requestDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MedicationResponse>> getAllMedications() {
        return ResponseEntity.ok(medicationService.getAllMedications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicationResponse> getMedicationById(@PathVariable Long id) {
        return ResponseEntity.ok(medicationService.getMedicationById(id));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<MedicationResponse>> searchByName(
            @RequestParam String name) {
        return ResponseEntity.ok(medicationService.searchByName(name));
    }


    @GetMapping("/category/{category}")
    public ResponseEntity<List<MedicationResponse>> getByCategory(
            @PathVariable String category) {
        return ResponseEntity.ok(medicationService.getByCategory(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicationResponse> updateMedication(
            @PathVariable Long id,
            @Valid @RequestBody CreateMedicationRequest requestDto) {
        return ResponseEntity.ok(medicationService.updateMedication(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        medicationService.deleteMedication(id);
        return ResponseEntity.noContent().build();
    }
}