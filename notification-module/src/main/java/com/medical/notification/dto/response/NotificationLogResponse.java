package com.medical.notification.dto.response;

import com.medical.notification.entity.NotificationLog;

import java.time.LocalDateTime;

public class NotificationLogResponse {

    private Long id;
    private Long patientId;
    private String patientName;
    private Long appointmentId;
    private String notificationType;
    private String recipientEmail;
    private String message;
    private LocalDateTime sentAt;
    private boolean success;
    private String errorMessage;

    public NotificationLogResponse() {}

    public static NotificationLogResponse fromEntity(NotificationLog log) {
        NotificationLogResponse response = new NotificationLogResponse();
        response.setId(log.getId());
        response.setNotificationType(log.getNotificationType());
        response.setRecipientEmail(log.getRecipientEmail());
        response.setMessage(log.getMessage());
        response.setSentAt(log.getSentAt());
        response.setSuccess(log.isSuccess());
        response.setErrorMessage(log.getErrorMessage());

        if (log.getPatient() != null) {
            response.setPatientId(log.getPatient().getId());
            response.setPatientName(
                log.getPatient().getFirstName() + " " +
                log.getPatient().getLastName()
            );
        }

        if (log.getAppointment() != null) {
            response.setAppointmentId(log.getAppointment().getId());
        }

        return response;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getNotificationType() { return notificationType; }
    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

