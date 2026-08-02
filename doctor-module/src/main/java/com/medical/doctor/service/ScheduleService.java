package com.medical.doctor.service;

import com.medical.doctor.dto.request.CreateScheduleRequest;
import com.medical.doctor.dto.response.ScheduleResponse;
import com.medical.doctor.entity.Schedule;

import java.time.LocalDate;
import java.util.List;


public interface ScheduleService {

    ScheduleResponse createSchedule(Long doctorId, CreateScheduleRequest request);

    List<ScheduleResponse> getSchedulesByDoctor(Long doctorId);

    List<ScheduleResponse> getAvailableSchedules(Long doctorId);

    List<ScheduleResponse> getSchedulesByDoctorAndDate(Long doctorId, LocalDate date);

    List<ScheduleResponse> getAvailableSchedulesByDoctorAndDate(Long doctorId, LocalDate date);

    ScheduleResponse getScheduleById(Long scheduleId);

    void deleteSchedule(Long doctorId, Long scheduleId);

    Schedule getScheduleEntityById(Long scheduleId);
}
