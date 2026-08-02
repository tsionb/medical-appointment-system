package com.medical.doctor.service;

import com.medical.common.exception.custom.AppointmentNotCompletedException;
import com.medical.common.exception.custom.DuplicateResourceException;
import com.medical.common.exception.custom.ResourceNotFoundException;
import com.medical.common.exception.custom.UnauthorizedAccessException;
import com.medical.common.enums.AppointmentStatus;
import com.medical.doctor.dto.request.CreateReviewRequest;
import com.medical.doctor.dto.response.ReviewResponse;
import com.medical.doctor.entity.Doctor;
import com.medical.doctor.entity.Review;
import com.medical.doctor.repository.DoctorRepository;
import com.medical.doctor.repository.ReviewRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final DoctorRepository doctorRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             DoctorRepository doctorRepository) {
        this.reviewRepository = reviewRepository;
        this.doctorRepository = doctorRepository;
    }


    @Override
    @Transactional
    public ReviewResponse createReview(Long doctorId, Long patientId, CreateReviewRequest request) {


        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Doctor", "id", doctorId));


        if (reviewRepository.existsByAppointmentId(request.getAppointmentId())) {
            throw new DuplicateResourceException(
                "A review already exists for appointment ID " +
                request.getAppointmentId()
            );
        }


        Review review = new Review();
        review.setDoctor(doctor);
        review.setPatientId(patientId);
        review.setAppointmentId(request.getAppointmentId());
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review saved = reviewRepository.save(review);
        return ReviewResponse.fromEntity(saved);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByDoctor(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor", "id", doctorId);
        }
        return reviewRepository.findByDoctorId(doctorId)
                .stream()
                .map(ReviewResponse::fromEntity)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByPatient(Long patientId) {
        return reviewRepository.findByPatientId(patientId)
                .stream()
                .map(ReviewResponse::fromEntity)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public ReviewResponse getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Review", "id", reviewId));
        return ReviewResponse.fromEntity(review);
    }

    
    @Override
    @Transactional
    public void deleteReview(Long reviewId, Long patientId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Review", "id", reviewId));


        if (!review.getPatientId().equals(patientId)) {
            throw new UnauthorizedAccessException(
                "You are not authorized to delete this review"
            );
        }

        reviewRepository.deleteById(reviewId);
    }
}