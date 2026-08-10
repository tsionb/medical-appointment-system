package com.medical.common.event;

import org.springframework.context.ApplicationEvent;

public class WaitlistPromotedEvent extends ApplicationEvent {

    private final Long appointmentId;

    public WaitlistPromotedEvent(Object source, Long appointmentId) {
        super(source);
        this.appointmentId = appointmentId;
    }

    public Long getAppointmentId() { return appointmentId; }
}
