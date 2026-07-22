package com.medical.doctor.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/*
 * A patient's rating and comment for a doctor after a completed appointment.
 *
 * Table: reviews
 * Relationships:
 *   - Many reviews belong to one Doctor (FK: doctor_id)
 *   - Linked to one Appointment (by ID only — see explanation below)
 *   - Written by one Patient (by ID only — see explanation below)
 *
 * WHY patientId AND appointmentId ARE PLAIN LONGS, NOT @ManyToOne:
 *
 * If Review had @ManyToOne Patient, then doctor-module would need to depend
 * on patient-module. But appointment-module already depends on BOTH
 * doctor-module and patient-module. That would create:
 *
 *   appointment-module → doctor-module → patient-module  ✓
 *   appointment-module → patient-module                  ✓
 *
 * But if patient-module ever needed doctor-module, you'd get a CIRCULAR
 * dependency which Maven cannot resolve and the build fails.
 *
 * Storing IDs as plain Longs breaks the coupling while still maintaining
 * the logical relationship. The service layer resolves the full objects
 * when needed using the patient-module and appointment-module repositories.
 */
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Plain Long — not a @ManyToOne.
     * Stores the patient's ID without importing the Patient class.
     * This keeps doctor-module free of patient-module dependency.
     */
    @Column(nullable = false)
    private Long patientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    /*
     * unique = true → a patient can only leave one review per appointment.
     * Enforced at both application and database level.
     */
    @Column(nullable = false, unique = true)
    private Long appointmentId;

    /*
     * Rating from 1 to 5.
     * Range validated on the DTO (@Min(1) @Max(5)), not the entity.
     * Entities don't validate — DTOs do.
     */
    @Column(nullable = false)
    private int rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Review() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
