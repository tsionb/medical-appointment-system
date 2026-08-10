package com.medical.appointment.controller;

import com.medical.appointment.entity.Appointment;
import com.medical.appointment.repository.AppointmentRepository;
import com.medical.common.enums.AppointmentStatus;
import com.medical.common.exception.custom.AppointmentNotCompletedException;
import com.medical.common.exception.custom.ResourceNotFoundException;
import com.medical.common.exception.custom.UnauthorizedAccessException;
import com.medical.doctor.dto.request.CreateReviewRequest;
import com.medical.doctor.dto.response.ReviewResponse;
import com.medical.doctor.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/appointments/{appointmentId}/reviews")
public class AppointmentReviewController {

    private final ReviewService reviewService;
    private final AppointmentRepository appointmentRepository;

    public AppointmentReviewController(ReviewService reviewService,
                            AppointmentRepository appointmentRepository) {
        this.reviewService = reviewService;
        this.appointmentRepository = appointmentRepository;
    }


    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<ReviewResponse> createReview(
            @PathVariable Long appointmentId,
            @RequestParam Long patientId,
            @Valid @RequestBody CreateReviewRequest request) {


        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Appointment", "id", appointmentId));


        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new AppointmentNotCompletedException(
                "Reviews can only be submitted for COMPLETED appointments. " +
                "Current status: " + appointment.getStatus()
            );
        }


        if (!appointment.getPatient().getId().equals(patientId)) {
            throw new UnauthorizedAccessException(
                "You can only review appointments that belong to you"
            );
        }


        request.setAppointmentId(appointmentId);
        return new ResponseEntity<>(
                reviewService.createReview(
                    appointment.getDoctor().getId(), patientId, request),
                HttpStatus.CREATED);
    }

    
    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getReviewsForAppointment(
            @PathVariable Long appointmentId) {

       
        if (!appointmentRepository.existsById(appointmentId)) {
            throw new ResourceNotFoundException("Appointment", "id", appointmentId);
        }

        return ResponseEntity.ok(
                reviewService.getReviewsByAppointment(appointmentId));
    }
}