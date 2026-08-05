package com.medical.records.repository;

import com.medical.records.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    Optional<MedicalRecord> findByAppointmentId(Long appointmentId);
    boolean existsByAppointmentId(Long appointmentId);
}