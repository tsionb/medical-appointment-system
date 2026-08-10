package com.medical.appointment.service;

import com.medical.appointment.dto.request.CreateAppointmentRequest;
import com.medical.appointment.dto.request.UpdateAppointmentStatusRequest;
import com.medical.appointment.dto.response.AppointmentResponse;
import com.medical.appointment.entity.Appointment;
import com.medical.appointment.entity.Waitlist;
import com.medical.appointment.repository.AppointmentRepository;
import com.medical.appointment.repository.WaitlistRepository;
import com.medical.common.enums.AppointmentStatus;
import com.medical.common.enums.WaitlistStatus;
import com.medical.common.exception.custom.InvalidAppointmentStatusException;
import com.medical.common.exception.custom.OutsideOperatingHoursException;
import com.medical.common.exception.custom.ResourceNotFoundException;
import com.medical.common.exception.custom.SlotNotAvailableException;
import com.medical.common.exception.custom.UnauthorizedAccessException;
import com.medical.department.entity.DepartmentSchedule;
import com.medical.department.repository.DepartmentScheduleRepository;
import com.medical.doctor.entity.Doctor;
import com.medical.doctor.entity.Schedule;
import com.medical.doctor.repository.DoctorRepository;
import com.medical.doctor.repository.ScheduleRepository;
import com.medical.patient.entity.Patient;
import com.medical.patient.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import com.medical.common.event.AppointmentBookedEvent;
import com.medical.common.event.AppointmentCancelledEvent;
import com.medical.common.event.AppointmentCompletedEvent;
import com.medical.common.event.WaitlistPromotedEvent;


import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final WaitlistRepository waitlistRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ScheduleRepository scheduleRepository;
    private final DepartmentScheduleRepository departmentScheduleRepository;
    private final ApplicationEventPublisher eventPublisher;

    public AppointmentServiceImpl(
            AppointmentRepository appointmentRepository,
            WaitlistRepository waitlistRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            ScheduleRepository scheduleRepository,
            DepartmentScheduleRepository departmentScheduleRepository,
            ApplicationEventPublisher eventPublisher) {
        this.appointmentRepository = appointmentRepository;
        this.waitlistRepository = waitlistRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.scheduleRepository = scheduleRepository;
        this.departmentScheduleRepository = departmentScheduleRepository;
        this.eventPublisher = eventPublisher;
    }


    @Override
    @Transactional
    public AppointmentResponse bookAppointment(CreateAppointmentRequest request) {


        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Patient", "id", request.getPatientId()));


        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Doctor", "id", request.getDoctorId()));


        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Schedule", "id", request.getScheduleId()));


        if (!schedule.getDoctor().getId().equals(request.getDoctorId())) {
            throw new ResourceNotFoundException(
                "Schedule", "id", request.getScheduleId());
        }


        if (schedule.isBooked()) {
            throw new SlotNotAvailableException(
                "This time slot is already booked. " +
                "Please choose a different slot or join the waitlist."
            );
        }


        String dayOfWeek = schedule.getDate()
                .getDayOfWeek()
                .name(); 


        List<DepartmentSchedule> departmentHours = departmentScheduleRepository
                .findByDepartmentId(doctor.getDepartment().getId());


        DepartmentSchedule todayHours = departmentHours.stream()
                .filter(ds -> ds.getDayOfWeek().equals(dayOfWeek))
                .findFirst()
                .orElseThrow(() -> new OutsideOperatingHoursException(
                    "The " + doctor.getDepartment().getName() +
                    " department is not open on " + dayOfWeek
                ));

        boolean startsAfterOpen = !schedule.getStartTime()
                .isBefore(todayHours.getOpenTime());
        boolean endsBeforeClose = !schedule.getEndTime()
                .isAfter(todayHours.getCloseTime());

        if (!startsAfterOpen || !endsBeforeClose) {
            throw new OutsideOperatingHoursException(
                "Appointment time " + schedule.getStartTime() +
                " - " + schedule.getEndTime() +
                " is outside the department's operating hours (" +
                todayHours.getOpenTime() + " - " + todayHours.getCloseTime() + ")"
            );
        }
        
        if (appointmentRepository.existsByScheduleIdAndStatusIn(
                request.getScheduleId(),
                List.of(AppointmentStatus.PENDING, AppointmentStatus.CONFIRMED))) {
            throw new SlotNotAvailableException("This time slot is already booked");
        }


        schedule.setBooked(true);
        scheduleRepository.save(schedule);

        // Create the appointment
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setSchedule(schedule);
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setNotes(request.getNotes());

        Appointment saved = appointmentRepository.save(appointment);
        eventPublisher.publishEvent(new AppointmentBookedEvent(this, saved.getId()));
        return AppointmentResponse.fromEntity(saved);
    }


    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByPatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient", "id", patientId);
        }
        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .map(AppointmentResponse::fromEntity)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor", "id", doctorId);
        }
        return appointmentRepository.findByDoctorId(doctorId)
                .stream()
                .map(AppointmentResponse::fromEntity)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByPatientAndStatus(
            Long patientId, AppointmentStatus status) {
        return appointmentRepository
                .findByPatientIdAndStatus(patientId, status)
                .stream()
                .map(AppointmentResponse::fromEntity)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(Long id) {
        return AppointmentResponse.fromEntity(
                appointmentRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                            "Appointment", "id", id))
        );
    }


    @Override
    @Transactional
    public AppointmentResponse updateAppointmentStatus(
            Long id, UpdateAppointmentStatusRequest request) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Appointment", "id", id));

        AppointmentStatus current = appointment.getStatus();
        AppointmentStatus requested = request.getStatus();

        if (current == AppointmentStatus.COMPLETED) {
            throw new InvalidAppointmentStatusException(
                "Cannot change status of a COMPLETED appointment"
            );
        }
        if (current == AppointmentStatus.CANCELLED) {
            throw new InvalidAppointmentStatusException(
                "Cannot change status of a CANCELLED appointment"
            );
        }


        boolean validTransition =
            (current == AppointmentStatus.PENDING &&
                (requested == AppointmentStatus.CONFIRMED ||
                 requested == AppointmentStatus.CANCELLED)) ||
            (current == AppointmentStatus.CONFIRMED &&
                (requested == AppointmentStatus.COMPLETED ||
                 requested == AppointmentStatus.CANCELLED));

        if (!validTransition) {
            throw new InvalidAppointmentStatusException(
                "Cannot transition from " + current + " to " + requested
            );
        }

        if (requested == AppointmentStatus.CANCELLED) {
            freeSlotAndPromoteWaitlist(appointment);
        }
        
        
        appointment.setStatus(requested);
        Appointment updated = appointmentRepository.save(appointment);
        
        if (requested == AppointmentStatus.CANCELLED) {
            eventPublisher.publishEvent(
                new AppointmentCancelledEvent(this, updated.getId()));
        } else if (requested == AppointmentStatus.COMPLETED) {
            eventPublisher.publishEvent(
                new AppointmentCompletedEvent(this, updated.getId()));
        }

        return AppointmentResponse.fromEntity(updated);
    }

    @Override
    @Transactional
    public AppointmentResponse cancelAppointment(Long id, Long patientId) {

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Appointment", "id", id));

        if (!appointment.getPatient().getId().equals(patientId)) {
            throw new UnauthorizedAccessException(
                "You are not authorized to cancel this appointment"
            );
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new InvalidAppointmentStatusException(
                "Cannot cancel a COMPLETED appointment"
            );
        }

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new InvalidAppointmentStatusException(
                "Appointment is already CANCELLED"
            );
        }

        freeSlotAndPromoteWaitlist(appointment);

        appointment.setStatus(AppointmentStatus.CANCELLED);
        Appointment cancelled = appointmentRepository.save(appointment);

        eventPublisher.publishEvent(new AppointmentCancelledEvent(this, cancelled.getId()));
        	return AppointmentResponse.fromEntity(cancelled);
    }

    @Override
    @Transactional(readOnly = true)
    public Appointment getAppointmentEntityById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Appointment", "id", id));
    }


    private void freeSlotAndPromoteWaitlist(Appointment appointment) {


        Schedule schedule = appointment.getSchedule();
        schedule.setBooked(false);
        scheduleRepository.save(schedule);


        List<Waitlist> waitingQueue = waitlistRepository
                .findByDoctorIdAndRequestedDateAndStatusOrderByCreatedAtAsc(
                        appointment.getDoctor().getId(),
                        schedule.getDate(),
                        WaitlistStatus.WAITING
                );


        if (!waitingQueue.isEmpty()) {

            Waitlist nextInQueue = waitingQueue.get(0); 

            schedule.setBooked(true);
            scheduleRepository.save(schedule);

            Appointment promotedAppointment = new Appointment();
            promotedAppointment.setPatient(nextInQueue.getPatient());
            promotedAppointment.setDoctor(appointment.getDoctor());
            promotedAppointment.setSchedule(schedule);
            promotedAppointment.setStatus(AppointmentStatus.CONFIRMED);
            promotedAppointment.setNotes(
                    "Automatically promoted from waitlist. " +
                    "Original appointment was cancelled."
            );

            Appointment promoted = appointmentRepository.save(promotedAppointment);

            nextInQueue.setStatus(WaitlistStatus.PROMOTED);
            waitlistRepository.save(nextInQueue);

            eventPublisher.publishEvent(new WaitlistPromotedEvent(this, promoted.getId()));
        }

    }
}
