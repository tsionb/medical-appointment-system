package com.medical.doctor.service;

import com.medical.common.exception.custom.DuplicateResourceException;
import com.medical.common.exception.custom.ResourceNotFoundException;
import com.medical.doctor.dto.request.CreateScheduleRequest;
import com.medical.doctor.dto.response.ScheduleResponse;
import com.medical.doctor.entity.Doctor;
import com.medical.doctor.entity.Schedule;
import com.medical.doctor.repository.DoctorRepository;
import com.medical.doctor.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository,
                               DoctorRepository doctorRepository) {
        this.scheduleRepository = scheduleRepository;
        this.doctorRepository = doctorRepository;
    }

    
    @Override
    @Transactional
    public ScheduleResponse createSchedule(Long doctorId, CreateScheduleRequest request) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", doctorId));

        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new IllegalArgumentException(
                "Start time (" + request.getStartTime() + ") must be before " +
                "end time (" + request.getEndTime() + ")"
            );
        }


        if (scheduleRepository.existsOverlappingSchedule(
                doctorId,
                request.getDate(),
                request.getStartTime(),
                request.getEndTime())) {
            throw new DuplicateResourceException(
                "A schedule slot already exists for this doctor that overlaps " +
                "with " + request.getStartTime() + " - " + request.getEndTime() +
                " on " + request.getDate()
            );
        }


        Schedule schedule = new Schedule();
        schedule.setDoctor(doctor);
        schedule.setDate(request.getDate());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        // isBooked defaults to false — slot is available

        Schedule saved = scheduleRepository.save(schedule);
        return ScheduleResponse.fromEntity(saved);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ScheduleResponse> getSchedulesByDoctor(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor", "id", doctorId);
        }
        return scheduleRepository.findByDoctorId(doctorId)
                .stream()
                .map(ScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<ScheduleResponse> getAvailableSchedules(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor", "id", doctorId);
        }
        return scheduleRepository.findByDoctorIdAndIsBookedFalse(doctorId)
                .stream()
                .map(ScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<ScheduleResponse> getSchedulesByDoctorAndDate(
            Long doctorId, LocalDate date) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor", "id", doctorId);
        }
        return scheduleRepository.findByDoctorIdAndDate(doctorId, date)
                .stream()
                .map(ScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<ScheduleResponse> getAvailableSchedulesByDoctorAndDate(
            Long doctorId, LocalDate date) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor", "id", doctorId);
        }
        return scheduleRepository
                .findByDoctorIdAndDateAndIsBookedFalse(doctorId, date)
                .stream()
                .map(ScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public ScheduleResponse getScheduleById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Schedule", "id", scheduleId));
        return ScheduleResponse.fromEntity(schedule);
    }


    @Override
    @Transactional
    public void deleteSchedule(Long doctorId, Long scheduleId) {

        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor", "id", doctorId);
        }

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Schedule", "id", scheduleId));

        if (!schedule.getDoctor().getId().equals(doctorId)) {
            throw new ResourceNotFoundException("Schedule", "id", scheduleId);
        }


        if (schedule.isBooked()) {
            throw new IllegalStateException(
                "Cannot delete a schedule slot that is already booked. " +
                "Cancel the appointment first."
            );
        }

        scheduleRepository.deleteById(scheduleId);
    }


    @Override
    @Transactional(readOnly = true)
    public Schedule getScheduleEntityById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Schedule", "id", scheduleId));
    }
}
