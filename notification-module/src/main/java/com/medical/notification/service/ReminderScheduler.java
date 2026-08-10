package com.medical.notification.service;

import com.medical.appointment.entity.Appointment;
import com.medical.appointment.repository.AppointmentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Component
public class ReminderScheduler {

    private final AppointmentRepository appointmentRepository;
    private final EmailService emailService;

    public ReminderScheduler(AppointmentRepository appointmentRepository,
                             EmailService emailService) {
        this.appointmentRepository = appointmentRepository;
        this.emailService = emailService;
    }


    @Scheduled(cron = "0 0 8 * * ?")
    @Transactional
    public void sendDailyReminders() {

    	LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<Appointment> appointmentsNeedingReminder =
                appointmentRepository.findAppointmentsNeedingReminder(tomorrow);

        for (Appointment appointment : appointmentsNeedingReminder) {
            try {
                emailService.sendAppointmentReminder(appointment);
                appointment.setReminderSent(true);
                appointmentRepository.save(appointment);

            } catch (Exception e) {
                System.err.println(
                    "Failed to send reminder for appointment ID " +
                    appointment.getId() + ": " + e.getMessage()
                );
            }
        }
    }
}
