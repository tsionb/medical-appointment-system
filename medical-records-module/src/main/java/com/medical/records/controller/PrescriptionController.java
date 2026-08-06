package com.medical.records.controller;

import com.medical.records.dto.request.CreatePrescriptionRequest;
import com.medical.records.dto.response.PrescriptionResponse;
import com.medical.records.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/medical-records/{recordId}/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }


    @PostMapping
    public ResponseEntity<PrescriptionResponse> addPrescription(
            @PathVariable Long recordId,
            @Valid @RequestBody CreatePrescriptionRequest request) {
        return new ResponseEntity<>(
                prescriptionService.addPrescription(recordId, request),
                HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<PrescriptionResponse>> getPrescriptions(
            @PathVariable Long recordId) {
        return ResponseEntity.ok(
                prescriptionService.getPrescriptionsByMedicalRecord(recordId));
    }


    @GetMapping("/{prescriptionId}")
    public ResponseEntity<PrescriptionResponse> getPrescriptionById(
            @PathVariable Long recordId,
            @PathVariable Long prescriptionId) {
        return ResponseEntity.ok(
                prescriptionService.getPrescriptionById(prescriptionId));
    }


    @DeleteMapping("/{prescriptionId}")
    public ResponseEntity<Void> deletePrescription(
            @PathVariable Long recordId,
            @PathVariable Long prescriptionId) {
        prescriptionService.deletePrescription(recordId, prescriptionId);
        return ResponseEntity.noContent().build();
    }
}