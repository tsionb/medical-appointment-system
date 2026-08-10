package com.medical.notification.service;

import com.medical.appointment.entity.Appointment;
import com.medical.notification.entity.NotificationLog;
import com.medical.notification.repository.NotificationLogRepository;
import com.medical.patient.entity.Patient;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final NotificationLogRepository notificationLogRepository;

    public EmailService(JavaMailSender mailSender,
                        NotificationLogRepository notificationLogRepository) {
        this.mailSender = mailSender;
        this.notificationLogRepository = notificationLogRepository;
    }


    public void sendBookingConfirmation(Appointment appointment) {

        Patient patient = appointment.getPatient();
        String doctorName = appointment.getDoctor().getFirstName() + " " +
                            appointment.getDoctor().getLastName();
        String date = appointment.getSchedule().getDate().toString();
        String time = appointment.getSchedule().getStartTime().toString();
        String department = appointment.getDoctor().getDepartment().getName();

        String subject = "Appointment Confirmation — " + date;

        String body = buildEmailBody(
            "Dear " + patient.getFirstName() + ",",
            "Your appointment has been successfully booked.",
            new String[]{
                "Doctor: Dr. " + doctorName,
                "Department: " + department,
                "Date: " + date,
                "Time: " + time,
                "Status: " + appointment.getStatus()
            },
            "Please arrive 10 minutes early. If you need to cancel, " +
            "please do so at least 24 hours in advance.",
            "We look forward to seeing you."
        );

        sendEmail(patient, appointment, patient.getEmail(),
                 subject, body, "BOOKING_CONFIRMATION");
    }


    public void sendAppointmentReminder(Appointment appointment) {

        Patient patient = appointment.getPatient();
        String doctorName = appointment.getDoctor().getFirstName() + " " +
                            appointment.getDoctor().getLastName();
        String date = appointment.getSchedule().getDate().toString();
        String time = appointment.getSchedule().getStartTime().toString();

        String subject = "Reminder: Your appointment is tomorrow — " + date;

        String body = buildEmailBody(
            "Dear " + patient.getFirstName() + ",",
            "This is a reminder that you have an appointment tomorrow.",
            new String[]{
                "Doctor: Dr. " + doctorName,
                "Date: " + date,
                "Time: " + time
            },
            "Please remember to bring any relevant medical documents or " +
            "test results. If you need to cancel, please contact us as soon as possible.",
            "We look forward to seeing you tomorrow."
        );

        sendEmail(patient, appointment, patient.getEmail(),
                 subject, body, "REMINDER");
    }


    public void sendCancellationNotification(Appointment appointment) {

        Patient patient = appointment.getPatient();
        String doctorName = appointment.getDoctor().getFirstName() + " " +
                            appointment.getDoctor().getLastName();
        String date = appointment.getSchedule().getDate().toString();
        String time = appointment.getSchedule().getStartTime().toString();

        String subject = "Appointment Cancelled — " + date;

        String body = buildEmailBody(
            "Dear " + patient.getFirstName() + ",",
            "Your appointment has been cancelled.",
            new String[]{
                "Doctor: Dr. " + doctorName,
                "Date: " + date,
                "Time: " + time
            },
            "If you did not request this cancellation, please contact us immediately. " +
            "You are welcome to book a new appointment at your convenience.",
            "We hope to see you again soon."
        );

        sendEmail(patient, appointment, patient.getEmail(),
                 subject, body, "CANCELLATION");
    }


    public void sendWaitlistPromotion(Appointment appointment) {

        Patient patient = appointment.getPatient();
        String doctorName = appointment.getDoctor().getFirstName() + " " +
                            appointment.getDoctor().getLastName();
        String date = appointment.getSchedule().getDate().toString();
        String time = appointment.getSchedule().getStartTime().toString();

        String subject = "Great news — You've been moved off the waitlist!";

        String body = buildEmailBody(
            "Dear " + patient.getFirstName() + ",",
            "A slot has become available and you have been automatically " +
            "promoted from the waitlist.",
            new String[]{
                "Doctor: Dr. " + doctorName,
                "Date: " + date,
                "Time: " + time,
                "Status: CONFIRMED"
            },
            "Your appointment is now confirmed. Please arrive 10 minutes early.",
            "Congratulations and we look forward to seeing you!"
        );

        sendEmail(patient, appointment, patient.getEmail(),
                 subject, body, "WAITLIST_PROMOTION");
    }


    public void sendAppointmentCompleted(Appointment appointment) {

        Patient patient = appointment.getPatient();
        String doctorName = appointment.getDoctor().getFirstName() + " " +
                            appointment.getDoctor().getLastName();

        String subject = "How was your appointment with Dr. " + doctorName + "?";

        String body = buildEmailBody(
            "Dear " + patient.getFirstName() + ",",
            "Your appointment with Dr. " + doctorName + " has been completed.",
            new String[]{
                "We hope your visit went well."
            },
            "We would love to hear your feedback. Please take a moment to " +
            "rate and review your experience — it helps other patients make " +
            "informed decisions.",
            "Thank you for choosing our healthcare facility."
        );

        sendEmail(patient, appointment, patient.getEmail(),
                 subject, body, "APPOINTMENT_COMPLETED");
    }


    private void sendEmail(Patient patient, Appointment appointment,
                           String recipientEmail, String subject,
                           String body, String notificationType) {

        NotificationLog log = new NotificationLog();
        log.setPatient(patient);
        log.setAppointment(appointment);
        log.setNotificationType(notificationType);
        log.setRecipientEmail(recipientEmail);
        log.setMessage(subject + "\n\n" + body);

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                message, true, "UTF-8");

            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(body, true); // true = isHtml
            helper.setFrom("noreply@medicalappointments.com");

            mailSender.send(message);

            log.setSuccess(true);

        } catch (MessagingException e) {
            log.setSuccess(false);
            log.setErrorMessage(e.getMessage());
        }


        notificationLogRepository.save(log);
    }


    private String buildEmailBody(String greeting, String intro,
                                  String[] details, String bodyText,
                                  String closing) {

        StringBuilder detailRows = new StringBuilder();
        for (String detail : details) {
            detailRows.append("<tr><td style='padding: 4px 0;'>")
                      .append(detail)
                      .append("</td></tr>");
        }

        return "<!DOCTYPE html>" +
            "<html><body style='font-family: Arial, sans-serif; " +
            "color: #333; max-width: 600px; margin: 0 auto;'>" +
            "<div style='background: #2196F3; padding: 20px; " +
            "text-align: center;'>" +
            "<h2 style='color: white; margin: 0;'>Medical Appointment System</h2>" +
            "</div>" +
            "<div style='padding: 30px; background: #f9f9f9;'>" +
            "<p>" + greeting + "</p>" +
            "<p>" + intro + "</p>" +
            "<table style='background: white; padding: 15px; " +
            "border-radius: 5px; width: 100%;'>" +
            detailRows +
            "</table>" +
            "<p style='margin-top: 20px;'>" + bodyText + "</p>" +
            "<p>" + closing + "</p>" +
            "</div>" +
            "<div style='background: #eee; padding: 15px; " +
            "text-align: center; font-size: 12px;'>" +
            "<p>Medical Appointment Management System</p>" +
            "</div>" +
            "</body></html>";
    }
}

