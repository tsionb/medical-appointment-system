package com.medical.doctor.repository;

import com.medical.doctor.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {


    List<Schedule> findByDoctorId(Long doctorId);

    List<Schedule> findByDoctorIdAndIsBookedFalse(Long doctorId);

    List<Schedule> findByDoctorIdAndDate(Long doctorId, LocalDate date);

    List<Schedule> findByDoctorIdAndDateAndIsBookedFalse(Long doctorId, LocalDate date);

    @Query("SELECT COUNT(s) > 0 FROM Schedule s " +
           "WHERE s.doctor.id = :doctorId " +
           "AND s.date = :date " +
           "AND s.startTime < :endTime " +
           "AND s.endTime > :startTime")
    boolean existsOverlappingSchedule(
            @Param("doctorId") Long doctorId,
            @Param("date") LocalDate date,
            @Param("startTime") java.time.LocalTime startTime,
            @Param("endTime") java.time.LocalTime endTime
    );

    boolean existsByIdAndDoctorId(Long scheduleId, Long doctorId);
}
