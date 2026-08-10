package com.medical.common.event;

import org.springframework.context.ApplicationEvent;

public class AppointmentBookedEvent extends ApplicationEvent {

    private final Long appointmentId;

    public AppointmentBookedEvent(Object source, Long appointmentId) {
        super(source);
        this.appointmentId = appointmentId;
    }

    public Long getAppointmentId() { return appointmentId; }
}