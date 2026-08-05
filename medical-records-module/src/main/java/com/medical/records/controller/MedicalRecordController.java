package com.medical.records.controller;

import com.medical.records.dto.MedicalRecordCreateDto;
import com.medical.records.dto.MedicalRecordResponseDto;
import com.medical.records.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    public ResponseEntity<MedicalRecordResponseDto> createRecord(@Valid @RequestBody MedicalRecordCreateDto createDto) {
        MedicalRecordResponseDto response = medicalRecordService.createMedicalRecord(createDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordResponseDto> getRecordById(@PathVariable Long id) {
        MedicalRecordResponseDto response = medicalRecordService.getMedicalRecordById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<MedicalRecordResponseDto> getRecordByAppointmentId(@PathVariable Long appointmentId) {
        MedicalRecordResponseDto response = medicalRecordService.getMedicalRecordByAppointmentId(appointmentId);
        return ResponseEntity.ok(response);
    }
}