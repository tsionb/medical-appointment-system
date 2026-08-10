package com.medical.records.controller;

import com.medical.records.dto.request.CreateMedicalRecordRequest;
import com.medical.records.dto.request.UpdateMedicalRecordRequest;
import com.medical.records.dto.response.MedicalRecordResponse;
import com.medical.records.service.MedicalRecordServiceImpl;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordServiceImpl medicalRecordService;

    public MedicalRecordController(MedicalRecordServiceImpl medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<MedicalRecordResponse> createRecord(@Valid @RequestBody CreateMedicalRecordRequest createDto) {
        MedicalRecordResponse response = medicalRecordService.createMedicalRecord(createDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordResponse> getRecordById(@PathVariable Long id) {
        MedicalRecordResponse response = medicalRecordService.getMedicalRecordById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<MedicalRecordResponse> getRecordByAppointmentId(@PathVariable Long appointmentId) {
        MedicalRecordResponse response = medicalRecordService.getMedicalRecordByAppointmentId(appointmentId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalRecordResponse>> getByPatient(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(
                medicalRecordService.getMedicalRecordsByPatient(patientId));
    }


    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<MedicalRecordResponse>> getByDoctor(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(
                medicalRecordService.getMedicalRecordsByDoctor(doctorId));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<MedicalRecordResponse> updateMedicalRecord(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMedicalRecordRequest request) {
        return ResponseEntity.ok(
                medicalRecordService.updateMedicalRecord(id, request));
    }

 
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.noContent().build();
    }
}