package com.medical.doctor.repository;

import com.medical.doctor.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    
    boolean existsByEmail(String email);

    Optional<Doctor> findByEmail(String email);

    List<Doctor> findByDepartmentId(Long departmentId);

    List<Doctor> findBySpecialization(String specialization);

    boolean existsByIdAndDepartmentId(Long doctorId, Long departmentId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.doctor.id = :doctorId")
    Double findAverageRatingByDoctorId(@Param("doctorId") Long doctorId);
}