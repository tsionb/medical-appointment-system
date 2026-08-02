package com.medical.doctor.dto.response;

import com.medical.doctor.entity.Review;

import java.time.LocalDateTime;


public class ReviewResponse {

    private Long id;
    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private Long appointmentId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    public ReviewResponse() {}

    public static ReviewResponse fromEntity(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setPatientId(review.getPatientId());
        response.setAppointmentId(review.getAppointmentId());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());

        if (review.getDoctor() != null) {
            response.setDoctorId(review.getDoctor().getId());
            response.setDoctorName(
                review.getDoctor().getFirstName() + " " +
                review.getDoctor().getLastName()
            );
        }

        return response;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
