package com.medical.notification.service;

import com.medical.appointment.entity.Appointment;
import com.medical.notification.dto.response.NotificationLogResponse;
import com.medical.notification.entity.NotificationLog;
import com.medical.notification.repository.NotificationLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class NotificationService {

    private final EmailService emailService;
    private final NotificationLogRepository notificationLogRepository;

    public NotificationService(EmailService emailService,
                               NotificationLogRepository notificationLogRepository) {
        this.emailService = emailService;
        this.notificationLogRepository = notificationLogRepository;
    }

    public void notifyBookingConfirmation(Appointment appointment) {
        emailService.sendBookingConfirmation(appointment);
    }

    public void notifyCancellation(Appointment appointment) {
        emailService.sendCancellationNotification(appointment);
    }

    public void notifyWaitlistPromotion(Appointment appointment) {
        emailService.sendWaitlistPromotion(appointment);
    }

    public void notifyAppointmentCompleted(Appointment appointment) {
        emailService.sendAppointmentCompleted(appointment);
    }

    @Transactional(readOnly = true)
    public List<NotificationLogResponse> getLogsByPatient(Long patientId) {
        return notificationLogRepository.findByPatientId(patientId)
                .stream()
                .map(NotificationLogResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationLogResponse> getLogsByAppointment(Long appointmentId) {
        return notificationLogRepository.findByAppointmentId(appointmentId)
                .stream()
                .map(NotificationLogResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationLogResponse> getFailedNotifications() {
        return notificationLogRepository.findBySuccessFalse()
                .stream()
                .map(NotificationLogResponse::fromEntity)
                .collect(Collectors.toList());
    }
}