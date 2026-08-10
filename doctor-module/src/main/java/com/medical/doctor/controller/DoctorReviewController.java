package com.medical.doctor.controller;

import com.medical.doctor.dto.request.CreateReviewRequest;
import com.medical.doctor.dto.response.ReviewResponse;
import com.medical.doctor.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors/{doctorId}/reviews")
public class DoctorReviewController {

    private final ReviewService reviewService;

    public DoctorReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @PathVariable Long doctorId,
            @RequestParam Long patientId,
            @Valid @RequestBody CreateReviewRequest request) {
        return new ResponseEntity<>(
                reviewService.createReview(doctorId, patientId, request),
                HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getReviewsByDoctor(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(reviewService.getReviewsByDoctor(doctorId));
    }


    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getReviewById(
            @PathVariable Long doctorId,
            @PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getReviewById(reviewId));
    }

   
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long doctorId,
            @PathVariable Long reviewId,
            @RequestParam Long patientId) {
        reviewService.deleteReview(reviewId, patientId);
        return ResponseEntity.noContent().build();
    }
}
