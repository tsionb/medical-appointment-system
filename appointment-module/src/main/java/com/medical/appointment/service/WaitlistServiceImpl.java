package com.medical.appointment.service;

import com.medical.appointment.dto.request.JoinWaitlistRequest;
import com.medical.appointment.dto.response.WaitlistResponse;
import com.medical.appointment.entity.Waitlist;
import com.medical.appointment.repository.WaitlistRepository;
import com.medical.common.enums.WaitlistStatus;
import com.medical.common.exception.custom.AlreadyOnWaitlistException;
import com.medical.common.exception.custom.InvalidAppointmentStatusException;
import com.medical.common.exception.custom.ResourceNotFoundException;
import com.medical.common.exception.custom.UnauthorizedAccessException;
import com.medical.doctor.entity.Doctor;
import com.medical.doctor.repository.DoctorRepository;
import com.medical.patient.entity.Patient;
import com.medical.patient.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WaitlistServiceImpl implements WaitlistService {

    private final WaitlistRepository waitlistRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public WaitlistServiceImpl(WaitlistRepository waitlistRepository,
                               PatientRepository patientRepository,
                               DoctorRepository doctorRepository) {
        this.waitlistRepository = waitlistRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }


    @Override
    @Transactional
    public WaitlistResponse joinWaitlist(JoinWaitlistRequest request) {


        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Patient", "id", request.getPatientId()));


        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Doctor", "id", request.getDoctorId()));


        if (waitlistRepository.existsByPatientIdAndDoctorIdAndRequestedDateAndStatus(
                request.getPatientId(),
                request.getDoctorId(),
                request.getRequestedDate(),
                WaitlistStatus.WAITING)) {
            throw new AlreadyOnWaitlistException(
                "You are already on the waitlist for this doctor on " +
                request.getRequestedDate()
            );
        }

        Waitlist waitlist = new Waitlist();
        waitlist.setPatient(patient);
        waitlist.setDoctor(doctor);
        waitlist.setRequestedDate(request.getRequestedDate());
        waitlist.setStatus(WaitlistStatus.WAITING);

        return WaitlistResponse.fromEntity(waitlistRepository.save(waitlist));
    }


    @Override
    @Transactional(readOnly = true)
    public List<WaitlistResponse> getWaitlistByPatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient", "id", patientId);
        }
        return waitlistRepository.findByPatientId(patientId)
                .stream()
                .map(WaitlistResponse::fromEntity)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<WaitlistResponse> getWaitlistByDoctor(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor", "id", doctorId);
        }
        return waitlistRepository.findByDoctorId(doctorId)
                .stream()
                .map(WaitlistResponse::fromEntity)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void cancelWaitlistEntry(Long waitlistId, Long patientId) {

        Waitlist waitlist = waitlistRepository.findById(waitlistId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Waitlist", "id", waitlistId));


        if (!waitlist.getPatient().getId().equals(patientId)) {
            throw new UnauthorizedAccessException(
                "You are not authorized to cancel this waitlist entry"
            );
        }

        if (waitlist.getStatus() != WaitlistStatus.WAITING) {
            throw new InvalidAppointmentStatusException(
                "Only WAITING entries can be cancelled. " +
                "Current status: " + waitlist.getStatus()
            );
        }

        waitlist.setStatus(WaitlistStatus.CANCELLED);
        waitlistRepository.save(waitlist);
    }

    @Override
    @Transactional(readOnly = true)
    public int getQueuePosition(Long waitlistId) {

        Waitlist waitlist = waitlistRepository.findById(waitlistId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Waitlist", "id", waitlistId));

        if (waitlist.getStatus() != WaitlistStatus.WAITING) {
            return -1; // not in queue
        }


        List<Waitlist> queue = waitlistRepository
                .findByDoctorIdAndRequestedDateAndStatusOrderByCreatedAtAsc(
                    waitlist.getDoctor().getId(),
                    waitlist.getRequestedDate(),
                    WaitlistStatus.WAITING
                );

        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i).getId().equals(waitlistId)) {
                return i + 1;
            }
        }

        return -1;
    }
}
