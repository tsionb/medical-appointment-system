package com.medical.appointment.controller;

import com.medical.appointment.dto.request.CreateAppointmentRequest;
import com.medical.appointment.dto.request.UpdateAppointmentStatusRequest;
import com.medical.appointment.dto.response.AppointmentResponse;
import com.medical.appointment.service.AppointmentService;
import com.medical.common.enums.AppointmentStatus;
import com.medical.common.exception.custom.ResourceNotFoundException;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import com.medical.patient.entity.Patient;
import com.medical.patient.repository.PatientRepository;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final PatientRepository patientRepository;

    public AppointmentController(
            AppointmentService appointmentService,
            PatientRepository patientRepository) {

        this.appointmentService = appointmentService;
        this.patientRepository = patientRepository;
    }


    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AppointmentResponse> bookAppointment(
            @Valid @RequestBody CreateAppointmentRequest request) {
        return new ResponseEntity<>(
                appointmentService.bookAppointment(request),
                HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(
            @PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }


    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentResponse>> getByPatient(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(
                appointmentService.getAppointmentsByPatient(patientId));
    }


    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentResponse>> getByDoctor(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(
                appointmentService.getAppointmentsByDoctor(doctorId));
    }


    @GetMapping("/patient/{patientId}/status/{status}")
    public ResponseEntity<List<AppointmentResponse>> getByPatientAndStatus(
            @PathVariable Long patientId,
            @PathVariable AppointmentStatus status) {
        return ResponseEntity.ok(
                appointmentService.getAppointmentsByPatientAndStatus(
                    patientId, status));
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentStatusRequest request) {
        return ResponseEntity.ok(
                appointmentService.updateAppointmentStatus(id, request));
    }


    @DeleteMapping("/{id}/cancel")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AppointmentResponse> cancelAppointment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {


        Patient patient = patientRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Patient", "email", userDetails.getUsername()));

        return ResponseEntity.ok(
                appointmentService.cancelAppointment(id, patient.getId()));
    }
}
