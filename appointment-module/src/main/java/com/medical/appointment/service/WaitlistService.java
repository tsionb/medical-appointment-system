package com.medical.appointment.service;

import com.medical.appointment.dto.request.JoinWaitlistRequest;
import com.medical.appointment.dto.response.WaitlistResponse;
import com.medical.common.enums.WaitlistStatus;

import java.util.List;

public interface WaitlistService {

    WaitlistResponse joinWaitlist(JoinWaitlistRequest request);

    List<WaitlistResponse> getWaitlistByPatient(Long patientId);

    List<WaitlistResponse> getWaitlistByDoctor(Long doctorId);

    void cancelWaitlistEntry(Long waitlistId, Long patientId);

    int getQueuePosition(Long waitlistId);
}
