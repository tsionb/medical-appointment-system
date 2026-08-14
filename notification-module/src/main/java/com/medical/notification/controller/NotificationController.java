package com.medical.notification.controller;

import com.medical.notification.dto.response.NotificationLogResponse;
import com.medical.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<NotificationLogResponse>> getByPatient(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(
                notificationService.getLogsByPatient(patientId));
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<NotificationLogResponse>> getByAppointment(
            @PathVariable Long appointmentId) {
        return ResponseEntity.ok(
                notificationService.getLogsByAppointment(appointmentId));
    }


    @GetMapping("/failed")
    public ResponseEntity<List<NotificationLogResponse>> getFailedNotifications() {
        return ResponseEntity.ok(
                notificationService.getFailedNotifications());
    }
}