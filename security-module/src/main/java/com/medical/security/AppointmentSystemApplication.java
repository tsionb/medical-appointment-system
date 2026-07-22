package com.medical.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
        "com.medical.common", "com.medical.department", "com.medical.doctor",
        "com.medical.patient", "com.medical.appointment", "com.medical.records",
        "com.medical.notification", "com.medical.security"
})
@EntityScan(basePackages = {
        "com.medical.department.entity", "com.medical.doctor.entity",
        "com.medical.patient.entity", "com.medical.appointment.entity",
        "com.medical.records.entity", "com.medical.notification.entity",
        "com.medical.security.entity"
})
@EnableJpaRepositories(basePackages = {
        "com.medical.department.repository", "com.medical.doctor.repository",
        "com.medical.patient.repository", "com.medical.appointment.repository",
        "com.medical.records.repository", "com.medical.notification.repository",
        "com.medical.security.repository"
})
@EnableScheduling
public class AppointmentSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppointmentSystemApplication.class, args);
    }
}
