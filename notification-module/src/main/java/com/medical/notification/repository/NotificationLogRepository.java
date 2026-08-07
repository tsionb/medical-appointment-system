package com.medical.notification.repository;

import com.medical.notification.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {

    List<NotificationLog> findByPatientId(Long patientId);

    List<NotificationLog> findByAppointmentId(Long appointmentId);

    List<NotificationLog> findByNotificationType(String notificationType);

    List<NotificationLog> findBySuccessFalse();

    List<NotificationLog> findByPatientIdAndNotificationType(Long patientId, String notificationType);
}

