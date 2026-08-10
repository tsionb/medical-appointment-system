package com.medical.doctor.service;

import com.medical.doctor.dto.request.CreateReviewRequest;
import com.medical.doctor.dto.response.ReviewResponse;

import java.util.List;


public interface ReviewService {

    ReviewResponse createReview(Long doctorId, Long patientId, CreateReviewRequest request);

    List<ReviewResponse> getReviewsByDoctor(Long doctorId);

    List<ReviewResponse> getReviewsByPatient(Long patientId);

    ReviewResponse getReviewById(Long reviewId);
    
    List<ReviewResponse> getReviewsByAppointment(Long appointmentId);

    void deleteReview(Long reviewId, Long patientId);
}
