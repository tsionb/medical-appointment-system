package com.medical.doctor.repository;

import com.medical.doctor.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

 
    List<Review> findByDoctorId(Long doctorId);

    List<Review> findByPatientId(Long patientId);

    boolean existsByAppointmentId(Long appointmentId);

    boolean existsByIdAndPatientId(Long reviewId, Long patientId);
}
