package com.medical.appointment.repository;

import com.medical.appointment.entity.Appointment;
import com.medical.common.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    
    List<Appointment> findByPatientId(Long patientId);
    
    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByPatientIdAndStatus(Long patientId, AppointmentStatus status);

    List<Appointment> findByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);

    boolean existsByScheduleId(Long scheduleId);

    @Query("SELECT a FROM Appointment a " +
           "WHERE a.reminderSent = false " +
           "AND a.status = 'CONFIRMED' " +
           "AND a.schedule.date = CURRENT_DATE + 1")
    List<Appointment> findAppointmentsNeedingReminder();

    java.util.Optional<Appointment> findByScheduleId(Long scheduleId);
}