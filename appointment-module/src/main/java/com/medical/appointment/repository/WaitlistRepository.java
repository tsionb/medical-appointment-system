package com.medical.appointment.repository;

import com.medical.appointment.entity.Waitlist;
import com.medical.common.enums.WaitlistStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {

    List<Waitlist> findByPatientId(Long patientId);

    List<Waitlist> findByDoctorId(Long doctorId);

    List<Waitlist> findByDoctorIdAndRequestedDateAndStatusOrderByCreatedAtAsc(
            Long doctorId, LocalDate requestedDate, WaitlistStatus status);

    boolean existsByPatientIdAndDoctorIdAndRequestedDateAndStatus(
            Long patientId, Long doctorId, LocalDate requestedDate, WaitlistStatus status);

    List<Waitlist> findByPatientIdAndStatus(Long patientId, WaitlistStatus status);
}