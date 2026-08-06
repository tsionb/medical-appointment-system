package com.medical.records.repository;

import com.medical.records.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
	
    List<Prescription> findByMedicalRecordId(Long medicalRecordId);
    
    List<Prescription> findByMedicationId(Long medicationId);
    
    boolean existsByIdAndMedicalRecordId(Long prescriptionId, Long medicalRecordId);

}