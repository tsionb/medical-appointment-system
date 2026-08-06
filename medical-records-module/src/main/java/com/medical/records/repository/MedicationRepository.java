package com.medical.records.repository;

import com.medical.records.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
	
    Optional<Medication> findByNameIgnoreCase(String name);
    
    boolean existsByName(String name);
    
    List<Medication> findByCategory(String category);
    
    
}