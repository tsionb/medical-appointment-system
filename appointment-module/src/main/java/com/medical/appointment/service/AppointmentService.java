package com.medical.appointment.service;

import com.medical.appointment.dto.request.CreateAppointmentRequest;
import com.medical.appointment.dto.request.UpdateAppointmentStatusRequest;
import com.medical.appointment.dto.response.AppointmentResponse;
import com.medical.appointment.entity.Appointment;
import com.medical.common.enums.AppointmentStatus;

import java.util.List;

public interface AppointmentService {


    AppointmentResponse bookAppointment(CreateAppointmentRequest request);

    List<AppointmentResponse> getAppointmentsByPatient(Long patientId);

    List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId);

    List<AppointmentResponse> getAppointmentsByPatientAndStatus(
            Long patientId, AppointmentStatus status);

    AppointmentResponse getAppointmentById(Long id);

    AppointmentResponse updateAppointmentStatus(Long id,
            UpdateAppointmentStatusRequest request);

    AppointmentResponse cancelAppointment(Long id, Long patientId);

    Appointment getAppointmentEntityById(Long id);
}