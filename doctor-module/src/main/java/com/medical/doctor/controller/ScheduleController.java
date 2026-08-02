package com.medical.doctor.controller;

import com.medical.doctor.dto.request.CreateScheduleRequest;
import com.medical.doctor.dto.response.ScheduleResponse;
import com.medical.doctor.service.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/doctors/{doctorId}/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }


    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(
            @PathVariable Long doctorId,
            @Valid @RequestBody CreateScheduleRequest request) {
        return new ResponseEntity<>(
                scheduleService.createSchedule(doctorId, request),
                HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getSchedulesByDoctor(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(scheduleService.getSchedulesByDoctor(doctorId));
    }


    @GetMapping("/available")
    public ResponseEntity<List<ScheduleResponse>> getAvailableSchedules(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(scheduleService.getAvailableSchedules(doctorId));
    }


    @GetMapping("/date/{date}")
    public ResponseEntity<List<ScheduleResponse>> getSchedulesByDate(
            @PathVariable Long doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(
                scheduleService.getSchedulesByDoctorAndDate(doctorId, date));
    }


    @GetMapping("/available/date/{date}")
    public ResponseEntity<List<ScheduleResponse>> getAvailableSchedulesByDate(
            @PathVariable Long doctorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(
                scheduleService.getAvailableSchedulesByDoctorAndDate(doctorId, date));
    }


    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> getScheduleById(
            @PathVariable Long doctorId,
            @PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.getScheduleById(scheduleId));
    }


    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long doctorId,
            @PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(doctorId, scheduleId);
        return ResponseEntity.noContent().build();
    }
}
