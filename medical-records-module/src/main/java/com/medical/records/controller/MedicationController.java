package com.medical.records.controller;

import com.medical.records.dto.MedicationRequestDto;
import com.medical.records.dto.MedicationResponseDto;
import com.medical.records.service.MedicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medications")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @PostMapping
    public ResponseEntity<MedicationResponseDto> createMedication(@Valid @RequestBody MedicationRequestDto requestDto) {
        MedicationResponseDto created = medicationService.createMedication(requestDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MedicationResponseDto>> getAllMedications() {
        return ResponseEntity.ok(medicationService.getAllMedications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicationResponseDto> getMedicationById(@PathVariable Long id) {
        return ResponseEntity.ok(medicationService.getMedicationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicationResponseDto> updateMedication(
            @PathVariable Long id,
            @Valid @RequestBody MedicationRequestDto requestDto) {
        return ResponseEntity.ok(medicationService.updateMedication(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        medicationService.deleteMedication(id);
        return ResponseEntity.noContent().build();
    }
}