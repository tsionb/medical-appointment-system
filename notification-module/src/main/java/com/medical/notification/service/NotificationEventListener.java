package com.medical.notification.service;

import com.medical.appointment.entity.Appointment;
import com.medical.appointment.repository.AppointmentRepository;
import com.medical.common.event.AppointmentBookedEvent;
import com.medical.common.event.AppointmentCancelledEvent;
import com.medical.common.event.AppointmentCompletedEvent;
import com.medical.common.event.WaitlistPromotedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class NotificationEventListener {

    private final EmailService emailService;
    private final AppointmentRepository appointmentRepository;

    public NotificationEventListener(EmailService emailService,
                                     AppointmentRepository appointmentRepository) {
        this.emailService = emailService;
        this.appointmentRepository = appointmentRepository;
    }

    @EventListener
    public void onAppointmentBooked(AppointmentBookedEvent event) {
        appointmentRepository.findById(event.getAppointmentId())
                .ifPresent(emailService::sendBookingConfirmation);
    }

    @EventListener
    public void onAppointmentCancelled(AppointmentCancelledEvent event) {
        appointmentRepository.findById(event.getAppointmentId())
                .ifPresent(emailService::sendCancellationNotification);
    }

    @EventListener
    public void onAppointmentCompleted(AppointmentCompletedEvent event) {
        appointmentRepository.findById(event.getAppointmentId())
                .ifPresent(emailService::sendAppointmentCompleted);
    }

    @EventListener
    public void onWaitlistPromoted(WaitlistPromotedEvent event) {
        appointmentRepository.findById(event.getAppointmentId())
                .ifPresent(emailService::sendWaitlistPromotion);
    }
}